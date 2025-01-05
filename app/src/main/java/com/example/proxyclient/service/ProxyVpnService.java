package com.example.proxyclient.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.proxyclient.MainActivity;
import com.example.proxyclient.R;
import com.example.proxyclient.model.TrojanConfig;
import android.content.pm.ServiceInfo;
import android.content.Context;
import com.example.proxyclient.Constants;

public class ProxyVpnService extends VpnService {
    private static final String TAG = "ProxyVpnService";
    private static final String CHANNEL_ID = "vpn_channel";
    private static final int NOTIFICATION_ID = 1;
    
    private ParcelFileDescriptor vpnInterface;
    private String serverAddress;
    private int serverPort;
    private String password;
    private boolean isRunning;
    private Intent intent;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "VPN Service onCreate");
        broadcastVpnState(true);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.d(TAG, "VPN Service onStartCommand");
            this.intent = intent;
            
            if (intent == null) {
                Log.e(TAG, "Intent is null");
                return START_NOT_STICKY;
            }

            serverAddress = intent.getStringExtra("server");
            serverPort = intent.getIntExtra("port", 443);
            password = intent.getStringExtra("password");
            
            Log.d(TAG, "Server: " + serverAddress + ":" + serverPort);
            
            startForeground(NOTIFICATION_ID, 
                           createNotification("正在连接..."),
                           ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            
            if (establishVpn()) {
                broadcastVpnState(true);
                return START_STICKY;
            } else {
                broadcastVpnState(false);
                stopSelf();
                return START_NOT_STICKY;
            }
            
        } catch (Exception e) {
            Log.e(TAG, "VPN Service start failed", e);
            broadcastVpnState(false);
            cleanup();
            stopSelf();
            return START_NOT_STICKY;
        }
    }
    
    private boolean establishVpn() {
        try {
            Log.d(TAG, "Building VPN interface");
            
            Builder builder = new Builder()
                .setSession("ProxyClient")
                .addAddress("10.0.0.2", 24)
                .addDnsServer("8.8.8.8")
                .addDnsServer("8.8.4.4")  // 添加备用 DNS
                .addRoute("0.0.0.0", 0);  // 所有流量经过 VPN
                
            // 允许绕过的应用
            if (intent != null && intent.getStringArrayExtra("bypass_packages") != null) {
                for (String packageName : intent.getStringArrayExtra("bypass_packages")) {
                    builder.addDisallowedApplication(packageName);
                }
            }

            // 设置 MTU
            builder.setMtu(1500);
            
            // 建立 VPN 接口
            vpnInterface = builder.establish();
            if (vpnInterface == null) {
                Log.e(TAG, "VPN interface is null");
                updateNotification("VPN 接口创建失败");
                return false;
            }

            Log.d(TAG, "VPN interface established successfully");
            updateNotification("VPN 已连接");
            
            // 启动代理
            startTrojanProxy();
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "establishVpn failed", e);
            updateNotification("VPN 连接失败: " + e.getMessage());
            return false;
        }
    }
    
    private void startTrojanProxy() {
        try {
            if (vpnInterface == null) {
                throw new IllegalStateException("VPN interface not established");
            }

            Log.d(TAG, "Starting Trojan proxy");
            updateNotification("正在启动代理...");

            new Thread(() -> {
                try {
                    int fd = vpnInterface.getFd();
                    Log.d(TAG, "VPN interface fd: " + fd);
                    
                    // 创建 Trojan 配置
                    String sni = intent.getStringExtra("sni");
                    if (sni == null || sni.isEmpty()) {
                        sni = serverAddress; // 如果没有设置 SNI，使用服务器地址
                    }
                    
                    TrojanConfig config = new TrojanConfig(
                        serverAddress,
                        serverPort,
                        password,
                        "VPN Connection",
                        "Default"
                    );
                    
                    // 设置其他选项
                    config.setSni(sni);
                    config.setAllowInsecure(intent.getBooleanExtra("allowInsecure", false));
                    
                    // 添加日志
                    Log.d(TAG, "Trojan config: " + 
                          "\nServer: " + config.getServerAddress() + ":" + config.getServerPort() +
                          "\nSNI: " + config.getSni() +
                          "\nAllow Insecure: " + config.isAllowInsecure());
                    
                    // 启动 Trojan
                    TrojanService.getInstance().connect(config);
                    
                    updateNotification("代理已启动");
                    Log.d(TAG, "Trojan proxy started successfully");
                    
                } catch (Exception e) {
                    Log.e(TAG, "Proxy thread failed", e);
                    updateNotification("代理启动失败: " + e.getMessage());
                    cleanup();
                }
            }).start();
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to start proxy", e);
            updateNotification("代理启动失败: " + e.getMessage());
            cleanup();
        }
    }
    
    private String getStringExtra(Intent intent, String key) {
        return intent != null ? intent.getStringExtra(key) : null;
    }
    
    private boolean getBooleanExtra(Intent intent, String key) {
        return intent != null && intent.getBooleanExtra(key, false);
    }
    
    private void cleanup() {
        Log.d(TAG, "VPN cleanup started");
        
        if (vpnInterface != null) {
            try {
                vpnInterface.close();
                vpnInterface = null;
            } catch (Exception e) {
                Log.e(TAG, "Error closing VPN interface", e);
            }
        }
        
        try {
            TrojanService.getInstance().disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error stopping Trojan service", e);
        }
        
        Log.d(TAG, "VPN cleanup completed");
    }
    
    private void updateNotification(String status) {
        startForeground(NOTIFICATION_ID, createNotification(status));
    }
    
    private Notification createNotification(String status) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 
            PendingIntent.FLAG_IMMUTABLE);
            
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ProxyClient VPN")
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_vpn)
            .setContentIntent(pendingIntent)
            .build();
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "VPN Status",
                NotificationManager.IMPORTANCE_LOW);
                
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "VPN Service onDestroy");
        broadcastVpnState(false);
        cleanup();
        stopForeground(true);
    }
    
    @Override
    public void onRevoke() {
        Log.d(TAG, "VPN permission revoked");
        // VPN 权限被撤销时也要清理
        cleanup();
        stopSelf();
        super.onRevoke();
    }
    
    private void broadcastVpnState(boolean isConnected) {
        try {
            Log.d(TAG, "Broadcasting VPN state: " + isConnected);
            Intent intent = new Intent(Constants.ACTION_VPN_STATE_CHANGED);
            intent.setPackage(getPackageName());
            intent.putExtra(Constants.EXTRA_VPN_STATE, isConnected);
            sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error broadcasting state", e);
        }
    }
} 