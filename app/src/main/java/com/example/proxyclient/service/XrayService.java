package com.example.proxyclient.service;

import android.content.Context;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.proxyclient.model.XrayConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XrayService {
    private static final String TAG = "XrayService";
    private static XrayService instance;
    private ExecutorService executorService;
    private Context appContext;
    private Handler mainHandler;
    private boolean isConnecting = false;
    
    static {
        System.loadLibrary("xray");
    }
    
    // native 方法声明
    private native int initXray(String config);
    private native int startXray();
    private native void stopXray();
    private native boolean isXrayRunning();
    
    private XrayService() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    public static XrayService getInstance() {
        if (instance == null) {
            synchronized (XrayService.class) {
                if (instance == null) {
                    instance = new XrayService();
                }
            }
        }
        return instance;
    }
    
    public void init(Context context) {
        appContext = context.getApplicationContext();
        // 准备必要的资源文件
        prepareResources();
    }
    
    private void prepareResources() {
        // 如果需要从assets中复制任何配置文件，可以在这里实现
        Log.d(TAG, "Resources prepared");
    }
    
    public void connect(XrayConfig config) {
        if (isConnecting || isXrayRunning()) {
            Log.w(TAG, "Xray is already running or connecting");
            return;
        }
        
        isConnecting = true;
        executorService.execute(() -> {
            try {
                // 生成配置JSON
                String configJson = config.generateConfigJson();
                Log.d(TAG, "Generated config: " + configJson);
                
                // 初始化Xray
                int initResult = initXray(configJson);
                if (initResult != 0) {
                    throw new RuntimeException("Failed to initialize Xray: " + initResult);
                }
                
                // 启动Xray
                int startResult = startXray();
                if (startResult != 0) {
                    throw new RuntimeException("Failed to start Xray: " + startResult);
                }
                
                showToast("Xray连接成功");
                Log.i(TAG, "Xray connected successfully");
            } catch (Exception e) {
                Log.e(TAG, "Failed to connect Xray", e);
                showToast("Xray连接失败: " + e.getMessage());
            } finally {
                isConnecting = false;
            }
        });
    }
    
    public void disconnect() {
        if (!isXrayRunning()) {
            Log.w(TAG, "Xray is not running");
            return;
        }
        
        executorService.execute(() -> {
            try {
                stopXray();
                showToast("Xray已断开连接");
                Log.i(TAG, "Xray disconnected");
            } catch (Exception e) {
                Log.e(TAG, "Failed to disconnect Xray", e);
                showToast("Xray断开连接失败: " + e.getMessage());
            }
        });
    }
    
    public boolean isRunning() {
        return isXrayRunning();
    }
    
    private void showToast(final String message) {
        mainHandler.post(() -> {
            if (appContext != null) {
                Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    // 在应用退出时调用，清理资源
    public void destroy() {
        if (isXrayRunning()) {
            stopXray();
        }
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 