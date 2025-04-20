package com.example.proxyclient.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Xray配置实体类
 */
@Entity(tableName = "xray_configs")
public class XrayConfig {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;              // 配置名称
    private String protocol;          // 协议类型 (vmess, vless, shadowsocks等)
    private String serverAddress;     // 服务器地址
    private int serverPort;           // 服务器端口
    private String uuid;              // UUID或密码
    private String alterId = "0";     // alterID (VMess)
    private String security = "auto"; // 加密方式
    private String network = "tcp";   // 传输协议
    private int localPort = 1080;     // 本地端口
    private String remark = "";       // 备注
    private long lastConnected = 0;   // 上次连接时间
    private String subscriptionUrl = ""; // 订阅URL来源
    
    // 扩展设置
    private String path = "";           // WebSocket路径
    private String host = "";           // HTTP Host
    private String tls = "none";        // TLS设置
    private String sni = "";            // TLS SNI
    private String alpn = "";           // ALPN
    private boolean enableUdp = false;  // 是否启用UDP
    private String encryption = "";     // VLESS加密设置
    private String method = "chacha20-poly1305"; // ShadowSocks加密方法

    // 无参构造函数
    public XrayConfig() {
    }
    
    // 带参数的构造函数
    @Ignore
    public XrayConfig(String name, String protocol, String serverAddress, int serverPort, String uuid) {
        this.name = name;
        this.protocol = protocol;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.uuid = uuid;
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
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
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
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAlterId() {
        return alterId;
    }

    public void setAlterId(String alterId) {
        this.alterId = alterId;
    }
    
    public String getSecurity() {
        return security;
    }
    
    public void setSecurity(String security) {
        this.security = security;
    }
    
    public String getNetwork() {
        return network;
    }
    
    public void setNetwork(String network) {
        this.network = network;
    }
    
    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(long lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public String getTls() {
        return tls;
    }
    
    public void setTls(String tls) {
        this.tls = tls;
    }
    
    public String getSni() {
        return sni;
    }
    
    public void setSni(String sni) {
        this.sni = sni;
    }
    
    public String getAlpn() {
        return alpn;
    }

    public void setAlpn(String alpn) {
        this.alpn = alpn;
    }

    public boolean isEnableUdp() {
        return enableUdp;
    }

    public void setEnableUdp(boolean enableUdp) {
        this.enableUdp = enableUdp;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 生成Xray配置JSON
     */
    public String generateConfigJson() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 基本结构
        JSONObject log = new JSONObject();
        log.put("loglevel", "warning");
        json.put("log", log);

        // 入站设置 (本地SOCKS5代理)
        JSONObject inbound = new JSONObject();
        inbound.put("protocol", "socks");
        inbound.put("listen", "127.0.0.1");
        inbound.put("port", localPort);
        
        JSONObject inboundSettings = new JSONObject();
        inboundSettings.put("udp", enableUdp);
        inbound.put("settings", inboundSettings);
        
        JSONArray inbounds = new JSONArray();
        inbounds.put(inbound);
        json.put("inbounds", inbounds);

        // 出站设置 (远程服务器)
        JSONObject outbound = new JSONObject();
        outbound.put("protocol", protocol);
        outbound.put("tag", "proxy");
        
        JSONObject outboundSettings = new JSONObject();
        
        // 根据不同协议设置特定配置
        if ("vmess".equals(protocol)) {
            JSONArray vmess = new JSONArray();
            JSONObject server = new JSONObject();
            server.put("address", serverAddress);
            server.put("port", serverPort);
            server.put("users", new JSONArray().put(
                    new JSONObject()
                            .put("id", uuid)
                            .put("alterId", Integer.parseInt(alterId))
                            .put("security", security)
            ));
            vmess.put(server);
            outboundSettings.put("vnext", vmess);
        } else if ("vless".equals(protocol)) {
            JSONArray vless = new JSONArray();
            JSONObject server = new JSONObject();
            server.put("address", serverAddress);
            server.put("port", serverPort);
            server.put("users", new JSONArray().put(
                    new JSONObject()
                            .put("id", uuid)
                            .put("encryption", encryption.isEmpty() ? "none" : encryption)
            ));
            vless.put(server);
            outboundSettings.put("vnext", vless);
        } else if ("shadowsocks".equals(protocol)) {
            JSONArray servers = new JSONArray();
            JSONObject server = new JSONObject();
            server.put("address", serverAddress);
            server.put("port", serverPort);
            server.put("method", method);
            server.put("password", uuid);
            servers.put(server);
            outboundSettings.put("servers", servers);
        } else if ("trojan".equals(protocol)) {
            JSONArray servers = new JSONArray();
            JSONObject server = new JSONObject();
            server.put("address", serverAddress);
            server.put("port", serverPort);
            server.put("password", uuid);
            servers.put(server);
            outboundSettings.put("servers", servers);
        }
        
        outbound.put("settings", outboundSettings);
        
        // 流设置
        JSONObject streamSettings = new JSONObject();
        streamSettings.put("network", network);
        
        // TLS设置
        if (!"none".equals(tls)) {
            JSONObject tlsSettings = new JSONObject();
            if (!sni.isEmpty()) {
                tlsSettings.put("serverName", sni);
            }
            if (!alpn.isEmpty()) {
                JSONArray alpnArray = new JSONArray();
                for (String a : alpn.split(",")) {
                    alpnArray.put(a.trim());
                }
                tlsSettings.put("alpn", alpnArray);
            }
            streamSettings.put("security", tls);
            streamSettings.put("tlsSettings", tlsSettings);
        }
        
        // 传输设置
        if ("ws".equals(network)) {
            JSONObject wsSettings = new JSONObject();
            if (!path.isEmpty()) {
                wsSettings.put("path", path);
            }
            if (!host.isEmpty()) {
                JSONObject headers = new JSONObject();
                headers.put("Host", host);
                wsSettings.put("headers", headers);
            }
            streamSettings.put("wsSettings", wsSettings);
        } else if ("http".equals(network)) {
            JSONObject httpSettings = new JSONObject();
            if (!path.isEmpty()) {
                httpSettings.put("path", path);
            }
            if (!host.isEmpty()) {
                JSONArray hosts = new JSONArray();
                hosts.put(host);
                httpSettings.put("host", hosts);
            }
            streamSettings.put("httpSettings", httpSettings);
        } else if ("quic".equals(network)) {
            JSONObject quicSettings = new JSONObject();
            if (!path.isEmpty()) {
                quicSettings.put("key", path);
            }
            if (!host.isEmpty()) {
                quicSettings.put("security", host);
            }
            if (!security.isEmpty()) {
                quicSettings.put("header", new JSONObject().put("type", security));
            }
            streamSettings.put("quicSettings", quicSettings);
        }
        
        outbound.put("streamSettings", streamSettings);
        
        // 添加出站连接
        JSONArray outbounds = new JSONArray();
        outbounds.put(outbound);
        
        // 添加直连出站
        JSONObject directOutbound = new JSONObject();
        directOutbound.put("protocol", "freedom");
        directOutbound.put("tag", "direct");
        outbounds.put(directOutbound);
        
        json.put("outbounds", outbounds);
        
        return json.toString();
    }

    // 从URL解析配置的方法可以根据需要添加
} 