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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import android.content.pm.PackageManager;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.net.Proxy;

public class ProxyVpnService extends VpnService {
    private static final String TAG = "ProxyVpnService";
    private static final String ACTION_DISCONNECT = "com.example.proxyclient.DISCONNECT";
    private static final String CHANNEL_ID = "VPN_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
    
    private volatile boolean running = false;
    private ParcelFileDescriptor vpnInterface = null;
    private Thread vpnThread = null;

    @Override
    public void onCreate() {
        super.onCreate();
        
        // 创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "vpn_service",
                "VPN Service",
                NotificationManager.IMPORTANCE_DEFAULT);
            
            channel.setDescription("VPN Service Running");
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null, null);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null && ACTION_DISCONNECT.equals(intent.getAction())) {
                stopVpn();
                stopSelf();
                return START_NOT_STICKY;
            }

            if (running) {
                Log.i(TAG, "VPN Service already running");
                return START_STICKY;
            }

            // 创建通知
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(this, "vpn_service");
            } else {
                builder = new Notification.Builder(this);
            }
            
            Notification notification = builder
                .setContentTitle("VPN Service")
                .setContentText("VPN Service is running")
                .setSmallIcon(R.drawable.ic_notification)  // 确保这个图标存在
                .setOngoing(true)
                .build();

            // 启动前台服务
            startForeground(1, notification);
            
            running = true;
            vpnThread = new Thread(this::runVpn, "VpnThread");
            vpnThread.start();
            
            Log.i(TAG, "VPN Service started");
            return START_STICKY;
            
        } catch (Exception e) {
            Log.e(TAG, "Error in onStartCommand", e);
            stopSelf();
            return START_NOT_STICKY;
        }
    }

    private void runVpn() {
        try {
            // 设置 VPN 参数
            Builder builder = new Builder()
                .addAddress("10.0.0.1", 24)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("8.8.8.8")
                .setSession("MyVpnService");
            
            // 建立 VPN 接口
            ParcelFileDescriptor vpnInterface = builder.establish();
            Log.i(TAG, "VPN interface established successfully");
            
            FileInputStream in = new FileInputStream(vpnInterface.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(vpnInterface.getFileDescriptor());
            
            // 创建一个新的 Socket 而不是使用代理
            Socket tunnel = new Socket();
            tunnel.connect(new InetSocketAddress("8.8.8.8", 53));
            
            // 在这里处理数据转发
            byte[] packet = new byte[32767];
            int len;
            
            while ((len = in.read(packet)) != -1) {
                // 直接转发数据包而不使用代理
                tunnel.getOutputStream().write(packet, 0, len);
                tunnel.getOutputStream().flush();
                
                // 读取响应
                len = tunnel.getInputStream().read(packet);
                if (len > 0) {
                    out.write(packet, 0, len);
                    out.flush();
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "VPN error: " + e.getMessage());
        }
    }

    private String getIPString(byte[] ip) {
        return String.format("%d.%d.%d.%d",
            ip[0] & 0xFF, ip[1] & 0xFF, ip[2] & 0xFF, ip[3] & 0xFF);
    }

    private ParcelFileDescriptor establish() {
        try {
            Log.i(TAG, "Setting up VPN configuration");
            
            Builder builder = new Builder()
                .setSession("ProxyVPN")
                .addAddress("10.0.0.2", 24)
                .addDnsServer("8.8.8.8")
                .addDnsServer("8.8.4.4")  // 添加 Google 备用 DNS
                .addRoute("0.0.0.0", 0)   // 所有流量通过 VPN
                .setMtu(1500)
                .setBlocking(true);

            // 添加允许的应用
            try {
                builder.addAllowedApplication("com.android.chrome");
                builder.addAllowedApplication("com.google.android.gms");
                builder.addAllowedApplication("com.google.android.gsf");
                builder.addAllowedApplication("com.google.android.apps.maps");
                builder.addAllowedApplication("com.google.android.youtube");
                builder.addAllowedApplication("com.android.vending");  // Play Store
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, "Some Google apps not found", e);
            }

            ParcelFileDescriptor vpnInterface = builder.establish();
            if (vpnInterface == null) {
                Log.e(TAG, "Failed to establish VPN interface");
                return null;
            }

            Log.i(TAG, "VPN interface established successfully");
            return vpnInterface;

        } catch (Exception e) {
            Log.e(TAG, "Error establishing VPN", e);
            return null;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "VPN Service",
                NotificationManager.IMPORTANCE_DEFAULT);
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification(String status) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VPN Service")
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build();
    }

    private void updateNotification(String status) {
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, createNotification(status));
        }
    }

    private void stopVpn() {
        Log.i(TAG, "Stopping VPN");
        running = false;
        
        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing VPN interface", e);
            }
            vpnInterface = null;
        }

        updateNotification("VPN Disconnected");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "VPN Service being destroyed");
        stopVpn();
        stopForeground(true);
        super.onDestroy();
    }
} 