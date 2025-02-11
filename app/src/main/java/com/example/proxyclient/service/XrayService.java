package com.example.proxyclient.service;

import android.util.Log;
import com.example.proxyclient.model.XrayConfig;

public class XrayService {
    private static final String TAG = "XrayService";
    private static XrayService instance;
    
    static {
        System.loadLibrary("xray");
    }
    
    // native 方法声明
    private native int initXray(String config);
    private native int startXray();
    private native void stopXray();
    private native boolean isXrayRunning();
    
    private XrayService() {
        // 私有构造函数
    }
    
    public static XrayService getInstance() {
        if (instance == null) {
            instance = new XrayService();
        }
        return instance;
    }
    
    public void connect(XrayConfig config) throws Exception {
        try {
            String configJson = config.toJson();
            Log.d(TAG, "Connecting with config: " + configJson);
            
            int result = initXray(configJson);
            if (result != 0) {
                throw new Exception("Failed to init Xray: " + result);
            }
            
            result = startXray();
            if (result != 0) {
                throw new Exception("Failed to start Xray: " + result);
            }
            
            Log.i(TAG, "Xray started successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in connect", e);
            throw e;
        }
    }
} 