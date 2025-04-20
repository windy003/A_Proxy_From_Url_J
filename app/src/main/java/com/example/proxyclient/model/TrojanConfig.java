package com.example.proxyclient.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用于表示Trojan代理配置的实体类
 */
@Entity(tableName = "trojan_configs")
public class TrojanConfig {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;            // 配置名称
    private String serverAddress;   // 服务器地址
    private int serverPort;         // 服务器端口
    private String password;        // 密码
    private int localPort = 1080;   // 本地端口，默认1080
    private boolean verifyCert = true; // 是否验证证书
    private String sni = "";        // SNI
    private boolean enableUdp = false; // 是否启用UDP
    private long lastConnected = 0; // 上次连接时间
    private String remark = "";     // 备注
    private String subscriptionUrl = ""; // 订阅URL来源

    // 无参构造函数，Room会使用这个
    public TrojanConfig() {
    }

    // 带参数的构造函数，使用@Ignore注解告诉Room不要使用这个
    @Ignore
    public TrojanConfig(String name, String serverAddress, int serverPort, String password) {
        this.name = name;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.password = password;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public boolean isVerifyCert() {
        return verifyCert;
    }

    public void setVerifyCert(boolean verifyCert) {
        this.verifyCert = verifyCert;
    }

    public String getSni() {
        return sni;
    }

    public void setSni(String sni) {
        this.sni = sni;
    }

    public boolean isEnableUdp() {
        return enableUdp;
    }

    public void setEnableUdp(boolean enableUdp) {
        this.enableUdp = enableUdp;
    }

    public long getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(long lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    /**
     * 生成Trojan配置的JSON字符串
     */
    public String generateConfigJson() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 基本配置
        json.put("run_type", "client");
        json.put("local_addr", "127.0.0.1");
        json.put("local_port", localPort);
        json.put("remote_addr", serverAddress);
        json.put("remote_port", serverPort);
        json.put("password", new String[]{password});
        
        // 日志设置
        JSONObject log = new JSONObject();
        log.put("level", 1);
        json.put("log", log);
        
        // TLS设置
        JSONObject ssl = new JSONObject();
        ssl.put("verify", verifyCert);
        ssl.put("verify_hostname", verifyCert);
        if (!sni.isEmpty()) {
            ssl.put("sni", sni);
        }
        ssl.put("alpn", new String[]{"h2", "http/1.1"});
        ssl.put("reuse_session", true);
        ssl.put("session_ticket", false);
        ssl.put("curves", "");
        json.put("ssl", ssl);
        
        // TCP设置
        JSONObject tcp = new JSONObject();
        tcp.put("no_delay", true);
        tcp.put("keep_alive", true);
        tcp.put("reuse_port", false);
        tcp.put("fast_open", false);
        tcp.put("fast_open_qlen", 20);
        json.put("tcp", tcp);
        
        return json.toString();
    }

    /**
     * 从Trojan URL解析配置
     * 格式: trojan://password@server:port?sni=domain&allowInsecure=0#remark
     */
    public static TrojanConfig fromUrl(String url) {
        try {
            // 去除协议前缀
            if (!url.startsWith("trojan://")) {
                throw new IllegalArgumentException("不是有效的Trojan URL");
            }
            
            String content = url.substring(9); // 去掉 "trojan://"
            
            // 提取remark (如果有)
            String remark = "";
            int sharpIndex = content.indexOf('#');
            if (sharpIndex > 0) {
                remark = content.substring(sharpIndex + 1);
                content = content.substring(0, sharpIndex);
            }
            
            // 提取查询参数
            String queryParams = "";
            int questionIndex = content.indexOf('?');
            if (questionIndex > 0) {
                queryParams = content.substring(questionIndex + 1);
                content = content.substring(0, questionIndex);
            }
            
            // 提取密码、服务器和端口
            String[] parts = content.split("@");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Trojan URL格式错误");
            }
            
            String password = parts[0];
            
            String[] hostPort = parts[1].split(":");
            if (hostPort.length != 2) {
                throw new IllegalArgumentException("服务器地址格式错误");
            }
            
            String serverAddress = hostPort[0];
            int serverPort = Integer.parseInt(hostPort[1]);
            
            // 创建配置对象
            TrojanConfig config = new TrojanConfig();
            config.setServerAddress(serverAddress);
            config.setServerPort(serverPort);
            config.setPassword(password);
            config.setRemark(remark);
            
            // 解析查询参数
            if (!queryParams.isEmpty()) {
                String[] params = queryParams.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];
                        
                        switch (key) {
                            case "sni":
                                config.setSni(value);
                                break;
                            case "allowInsecure":
                                config.setVerifyCert(!"1".equals(value));
                                break;
                            case "udp":
                                config.setEnableUdp("1".equals(value));
                                break;
                        }
                    }
                }
            }
            
            // 设置名称
            if (remark.isEmpty()) {
                config.setName(serverAddress + ":" + serverPort);
            } else {
                config.setName(remark);
            }
            
            return config;
        } catch (Exception e) {
            throw new IllegalArgumentException("解析Trojan URL失败: " + e.getMessage());
        }
    }

    /**
     * 将配置转换为Trojan URL
     */
    public String toUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("trojan://")
          .append(password)
          .append("@")
          .append(serverAddress)
          .append(":")
          .append(serverPort);
        
        // 添加查询参数
        boolean hasParams = false;
        
        if (!sni.isEmpty()) {
            sb.append(hasParams ? "&" : "?");
            sb.append("sni=").append(sni);
            hasParams = true;
        }
        
        if (!verifyCert) {
            sb.append(hasParams ? "&" : "?");
            sb.append("allowInsecure=1");
            hasParams = true;
        }
        
        if (enableUdp) {
            sb.append(hasParams ? "&" : "?");
            sb.append("udp=1");
            hasParams = true;
        }
        
        // 添加备注
        if (!remark.isEmpty()) {
            sb.append("#").append(remark);
        }
        
        return sb.toString();
    }
} 