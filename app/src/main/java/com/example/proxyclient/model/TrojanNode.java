package com.example.proxyclient.model;

import android.net.Uri;
import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;

public class TrojanNode {
    private String url;        // 完整的trojan URL
    private String name;       // 节点名称
    private String region;     // 地区
    private String server;     // 服务器地址
    private int port;         // 端口
    private String password;  // 密码
    private String peer;      // SNI
    private String sni;       // SNI
    private boolean allowInsecure; // 是否允许不安全连接

    public TrojanNode(String url) {
        this.url = url;
        parseUrl(url);
    }

    private void parseUrl(String url) {
        try {
            // 移除 "trojan://" 前缀
            String cleanUrl = url.substring(9);
            
            // 使用 Uri 解析 URL
            Uri uri = Uri.parse(url);
            
            // 获取用户信息（密码）和服务器信息
            String userInfo = uri.getUserInfo();
            if (userInfo != null) {
                this.password = userInfo;
            }
            
            this.server = uri.getHost();
            this.port = uri.getPort();
            
            // 获取查询参数
            this.peer = uri.getQueryParameter("peer");
            this.sni = uri.getQueryParameter("sni");
            this.allowInsecure = "true".equals(uri.getQueryParameter("allowInsecure"));

            // 解析节点名称（Fragment部分）
            String fragment = uri.getFragment();
            if (fragment != null) {
                this.name = URLDecoder.decode(fragment, StandardCharsets.UTF_8);
                // 解析地区（通常是名称中的表情符号后面的部分）
                if (name.contains("🇭🇰")) {
                    this.region = "香港";
                } else if (name.contains("🇨🇳")) {
                    this.region = "台湾";
                } else if (name.contains("🇸🇬")) {
                    this.region = "新加坡";
                } else if (name.contains("🇯🇵")) {
                    this.region = "日本";
                } else if (name.contains("🇺🇲")) {
                    this.region = "美国";
                } else {
                    this.region = "其他";
                }
            }

        } catch (Exception e) {
            this.name = "未知节点";
            this.region = "未知地区";
        }
    }

    // Getters
    public String getUrl() { return url; }
    public String getName() { return name != null ? name : server; }
    public String getRegion() { return region != null ? region : "未知地区"; }
    public String getServer() { return server; }
    public int getPort() { return port; }
    public String getPassword() { return password; }
    public String getPeer() { return peer; }
    public String getSni() { return sni; }
    public boolean isAllowInsecure() { return allowInsecure; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s:%d)", region, name, server, port);
    }
} 