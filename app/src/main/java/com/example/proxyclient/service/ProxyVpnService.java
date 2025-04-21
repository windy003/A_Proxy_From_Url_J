package com.example.proxyclient.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.proxyclient.MainActivity;
import com.example.proxyclient.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ProxyVpnService extends VpnService {
    private static final String TAG = "ProxyVpnService";
    private static final String CHANNEL_ID = "VPN_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
    
    private ParcelFileDescriptor vpnInterface;
    private boolean isRunning = false;
    
    private static final int VPN_MTU = 1500;
    private static final String VPN_ADDRESS = "10.0.0.2";
    private static final String VPN_ROUTE = "0.0.0.0";
    private static final String VPN_DNS = "8.8.8.8";

    private int trojanPort = 1080; // Trojan默认的本地SOCKS5端口
    
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建通知和设置为前台服务
        createNotificationChannel();
        startForeground(1, buildNotification());
        
        // 获取Trojan服务的SOCKS5端口
        trojanPort = intent.getIntExtra("trojanPort", 1080);
        
        // 启动VPN
        establishVpn();
        
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        closeVpnInterface();
        super.onDestroy();
    }
    
    private void establishVpn() {
        try {
            // 配置VPN
            Builder builder = new Builder();
            builder.addAddress(VPN_ADDRESS, 32);
            builder.addRoute(VPN_ROUTE, 0);
            builder.addDnsServer(VPN_DNS);
            builder.setMtu(VPN_MTU);
            builder.setSession("Proxy Client VPN");
            
            // 允许所有应用使用VPN
            builder.addDisallowedApplication(getPackageName()); // 排除自己的应用
            
            // 建立VPN接口
            vpnInterface = builder.establish();
            
            if (vpnInterface == null) {
                Log.e(TAG, "VPN接口建立失败");
                stopSelf();
                return;
            }
            
            // 启动数据处理线程
            startVpnThread();
            
            Log.i(TAG, "VPN服务启动成功");
        } catch (Exception e) {
            Log.e(TAG, "建立VPN失败", e);
            stopSelf();
        }
    }
    
    private void startVpnThread() {
        // 这里创建线程处理VPN数据并转发到本地Trojan代理
        new Thread(() -> {
            try {
                // 使用当前线程的Looper
                Looper.prepare();
                
                FileInputStream in = new FileInputStream(vpnInterface.getFileDescriptor());
                FileOutputStream out = new FileOutputStream(vpnInterface.getFileDescriptor());
                
                // 将本地VPN流量转发到SOCKS5代理
                // 这里通常需要使用JNI调用本地代码或使用Java实现的SOCKS5客户端
                // 简化版代码：
                byte[] buffer = new byte[VPN_MTU];
                ProxySocks5Handler proxySocks5Handler = new ProxySocks5Handler("127.0.0.1", trojanPort);
                
                while (!Thread.interrupted()) {
                    // 读取VPN数据
                    int length = in.read(buffer);
                    if (length > 0) {
                        // 处理VPN数据包并转发到SOCKS5代理
                        proxySocks5Handler.handleVpnPacket(buffer, length, out);
                    }
                }
                
                Looper.loop();
            } catch (Exception e) {
                Log.e(TAG, "VPN线程异常", e);
            } finally {
                closeVpnInterface();
            }
        }).start();
    }
    
    private void closeVpnInterface() {
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "关闭VPN接口失败", e);
        }
    }
    
    // 构建通知，使服务可以在前台运行
    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
                
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("VPN 服务")
                .setContentText("代理服务正在运行")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "VPN Service",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("VPN service status notification");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
} 