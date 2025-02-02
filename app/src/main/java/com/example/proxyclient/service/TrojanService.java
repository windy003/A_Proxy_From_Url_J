package com.example.proxyclient.service;

import android.util.Log;
import com.example.proxyclient.model.TrojanConfig;  // 修改为正确的包名

public class TrojanService {
    private static final String TAG = "TrojanService";
    private static TrojanService instance;
    
    static {
        System.loadLibrary("trojan");  // 加载 libtrojan.so
    }
    
    // native 方法声明
    private native int initTrojan(String config);
    private native int startTrojan();
    private native void stopTrojan();
    private native boolean isTrojanRunning();
    
    private TrojanService() {
        // 私有构造函数
    }
    
    public static TrojanService getInstance() {
        if (instance == null) {
            instance = new TrojanService();
        }
        return instance;
    }
    
    public void connect(TrojanConfig config) throws Exception {
        try {
            // 使用现有 TrojanConfig 的方法生成配置
            String configJson = String.format(
                "{\"run_type\":\"client\"," +
                "\"local_addr\":\"127.0.0.1\"," +
                "\"local_port\":1080," +
                "\"remote_addr\":\"%s\"," +
                "\"remote_port\":%d," +
                "\"password\":[\"%s\"]," +
                "\"log_level\":0," +
                "\"ssl\":{" +
                "\"verify\":false," +
                "\"verify_hostname\":true," +
                "\"cert\":\"\",\"cipher\":\"\"," +
                "\"cipher_tls13\":\"\"," +
                "\"sni\":\"%s\"," +
                "\"alpn\":[\"h2\",\"http/1.1\"]," +
                "\"reuse_session\":true," +
                "\"session_ticket\":true," +
                "\"curves\":\"\"}}",
                config.getServerAddress(),
                config.getServerPort(),
                config.getPassword(),
                config.getSni()
            );
            
            Log.d(TAG, "Connecting with config: " + configJson);
            
            int result = initTrojan(configJson);
            if (result != 0) {
                throw new Exception("Failed to init Trojan: " + result);
            }
            
            result = startTrojan();
            if (result != 0) {
                throw new Exception("Failed to start Trojan: " + result);
            }
            
            Log.i(TAG, "Trojan started successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in connect", e);
            throw e;
        }
    }
    
    public void disconnect() {
        try {
            Log.d(TAG, "Disconnecting Trojan");
            stopTrojan();
        } catch (Exception e) {
            Log.e(TAG, "Error in disconnect", e);
        }
    }
} 