package com.example.proxyclient.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import java.net.URLDecoder;

@Entity(tableName = "trojan_config")
public class TrojanConfig {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String serverAddress;
    private int serverPort;
    private String password;
    private String remark;
    private String region;
    private String peer;
    private String sni;
    private boolean allowInsecure;

    public TrojanConfig() {
        this.serverPort = 443;
        this.allowInsecure = false;
    }

    @Ignore
    public TrojanConfig(String serverAddress, int serverPort, String password, String remark, String region) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.password = password;
        this.remark = remark;
        this.region = region;
    }

    @Ignore
    public TrojanConfig(String serverAddress, int serverPort, String password, String sni, boolean allowInsecure) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.password = password;
        this.sni = sni;
        this.allowInsecure = allowInsecure;
    }

    // Getters
    public String getServerAddress() { return serverAddress; }
    public int getServerPort() { return serverPort; }
    public String getPassword() { return password; }
    public String getRemark() { return remark; }
    public String getRegion() { return region; }
    public String getPeer() { return peer; }
    public String getSni() { return sni; }
    public boolean isAllowInsecure() { return allowInsecure; }
    public int getId() { return id; }
    public String getName() {
        return name;
    }

    // Setters
    public void setServerAddress(String serverAddress) { this.serverAddress = serverAddress; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }
    public void setPassword(String password) { this.password = password; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setRegion(String region) { this.region = region; }
    public void setPeer(String peer) { this.peer = peer; }
    public void setSni(String sni) { this.sni = sni; }
    public void setAllowInsecure(boolean allowInsecure) { this.allowInsecure = allowInsecure; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name != null ? name : (serverAddress + ":" + serverPort);
    }

    public static TrojanConfig fromUrl(String url) throws Exception {
        // trojan://password@server:port#name
        String cleanUrl = url.trim();
        if (!cleanUrl.startsWith("trojan://")) {
            throw new IllegalArgumentException("Invalid trojan URL");
        }

        TrojanConfig config = new TrojanConfig();

        // 处理节点名称（在 # 后面）
        String[] urlAndName = cleanUrl.split("#", 2);
        if (urlAndName.length > 1) {
            config.setName(URLDecoder.decode(urlAndName[1], "UTF-8"));
        }

        // 移除 "trojan://" 和名称部分
        String mainPart = urlAndName[0].substring(9);

        // 分离密码和服务器信息
        String[] parts = mainPart.split("@", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Missing password or server");
        }

        config.setPassword(URLDecoder.decode(parts[0], "UTF-8"));

        // 处理服务器部分
        String serverPart = parts[1];
        String[] serverAndParams = serverPart.split("\\?", 2);
        
        // 处理服务器地址和端口
        String[] hostPort = serverAndParams[0].split(":", 2);
        if (hostPort.length != 2) {
            throw new IllegalArgumentException("Invalid server address format");
        }

        config.setServerAddress(hostPort[0]);
        config.setServerPort(Integer.parseInt(hostPort[1]));

        // 处理可选参数
        if (serverAndParams.length > 1) {
            String[] params = serverAndParams[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    switch (keyValue[0]) {
                        case "sni":
                            config.setSni(URLDecoder.decode(keyValue[1], "UTF-8"));
                            break;
                        case "allowInsecure":
                            config.setAllowInsecure("1".equals(keyValue[1]));
                            break;
                    }
                }
            }
        }

        // 如果没有设置 SNI，使用服务器地址作为默认值
        if (config.getSni() == null || config.getSni().isEmpty()) {
            config.setSni(config.getServerAddress());
        }

        // 如果没有名称，尝试从服务器地址中提取
        if (config.getName() == null || config.getName().isEmpty()) {
            // 尝试从域名中提取地理位置信息
            String domain = config.getServerAddress();
            String[] parts2 = domain.split("\\.");
            if (parts2.length > 0) {
                // 通常地理位置信息在子域名中
                String location = parts2[0].toLowerCase();
                // 常见地理位置代码转换
                switch (location) {
                    case "hk":
                        config.setName("香港");
                        break;
                    case "sg":
                        config.setName("新加坡");
                        break;
                    case "jp":
                        config.setName("日本");
                        break;
                    case "us":
                        config.setName("美国");
                        break;
                    case "kr":
                        config.setName("韩国");
                        break;
                    case "tw":
                        config.setName("台湾");
                        break;
                    default:
                        // 如果无法识别，使用原始域名
                        config.setName(domain);
                }
            }
        }

        return config;
    }
}