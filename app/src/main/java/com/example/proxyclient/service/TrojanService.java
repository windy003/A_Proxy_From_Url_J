package com.example.proxyclient.service;

import android.content.Context;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.proxyclient.model.TrojanConfig;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrojanService {
    private static final String TAG = "TrojanService";
    private static TrojanService instance;
    private ExecutorService executorService;
    private Context appContext;
    private Handler mainHandler;
    private boolean isConnecting = false;
    
    static {
        System.loadLibrary("trojan");
    }
    
    // native 方法声明
    private native int initTrojan(String config);
    private native int startTrojan();
    private native void stopTrojan();
    
    private TrojanService() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    public static TrojanService getInstance() {
        if (instance == null) {
            synchronized (TrojanService.class) {
                if (instance == null) {
                    instance = new TrojanService();
                }
            }
        }
        return instance;
    }
    
    public void init(Context context) {
        appContext = context.getApplicationContext();
    }
    
    /**
     * 使用Trojan链接直接连接
     */
    public void connectWithUrl(String trojanUrl, ConnectCallback callback) {
        if (isConnecting) {
            if (callback != null) {
                callback.onError("已有连接正在进行");
            }
            return;
        }
        
        isConnecting = true;
        executorService.execute(() -> {
            try {
                // 解析Trojan URL
                TrojanConfig config = TrojanConfig.fromUrl(trojanUrl);
                connect(config, callback);
            } catch (Exception e) {
                Log.e(TAG, "解析Trojan URL失败", e);
                isConnecting = false;
                if (callback != null) {
                    callback.onError("解析Trojan URL失败: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * 连接到Trojan服务器
     */
    public void connect(TrojanConfig config, ConnectCallback callback) {
        if (isConnecting) {
            if (callback != null) {
                callback.onError("已有连接正在进行");
            }
            return;
        }
        
        isConnecting = true;
        executorService.execute(() -> {
            try {
                // 生成配置JSON
                String configJson = config.generateConfigJson();
                Log.d(TAG, "生成的Trojan配置: " + configJson);
                
                // 初始化Trojan
                int initResult = initTrojan(configJson);
                if (initResult != 0) {
                    throw new RuntimeException("初始化Trojan失败: " + initResult);
                }
                
                // 启动Trojan
                int startResult = startTrojan();
                if (startResult != 0) {
                    throw new RuntimeException("启动Trojan失败: " + startResult);
                }
                
                // 更新最后连接时间
                config.setLastConnected(System.currentTimeMillis());
                
                // 回调成功
                if (callback != null) {
                    callback.onSuccess();
                }
                showToast("已连接到 " + config.getName());
                Log.i(TAG, "已连接到 " + config.getName());
            } catch (Exception e) {
                Log.e(TAG, "连接Trojan失败", e);
                if (callback != null) {
                    callback.onError("连接失败: " + e.getMessage());
                }
                showToast("连接失败: " + e.getMessage());
            } finally {
                isConnecting = false;
            }
        });
    }
    
    /**
     * 断开连接
     */
    public void disconnect() {
        executorService.execute(() -> {
            try {
                stopTrojan();
                showToast("已断开连接");
                Log.i(TAG, "已断开连接");
            } catch (Exception e) {
                Log.e(TAG, "断开连接失败", e);
                showToast("断开连接失败: " + e.getMessage());
            }
        });
    }
    
    private void showToast(final String message) {
        mainHandler.post(() -> {
            if (appContext != null) {
                Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * 连接回调接口
     */
    public interface ConnectCallback {
        void onSuccess();
        void onError(String message);
    }
} 