package com.example.proxyclient.service;

import com.example.proxyclient.model.TrojanConfig;

public class TrojanService {
    private static TrojanService instance;
    
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
        if (config.getServerAddress() == null || config.getServerAddress().isEmpty()) {
            throw new IllegalArgumentException("服务器地址不能为空");
        }
        
        if (config.getServerPort() <= 0 || config.getServerPort() > 65535) {
            throw new IllegalArgumentException("端口号无效");
        }
        
        if (config.getPassword() == null || config.getPassword().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // TODO: 调用 native 方法启动 Trojan
        // 这里需要实现 JNI 调用
    }
    
    public void disconnect() {
        // TODO: 实现断开连接的逻辑
    }
} 