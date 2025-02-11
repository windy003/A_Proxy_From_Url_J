package com.example.proxyclient.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "xray_configs")
public class XrayConfig {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String address;
    private int port;
    private String protocol;
    private String uuid;
    
    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            JSONObject outbound = new JSONObject();
            JSONObject settings = new JSONObject();
            JSONObject vnext = new JSONObject();
            
            // 配置基本连接信息
            vnext.put("address", address);
            vnext.put("port", port);
            
            // 配置用户信息
            JSONObject user = new JSONObject();
            user.put("id", uuid);
            user.put("security", "auto");
            vnext.put("users", new JSONObject[]{user});
            
            // 配置协议设置
            settings.put("vnext", new JSONObject[]{vnext});
            outbound.put("protocol", protocol);
            outbound.put("settings", settings);
            
            // 配置主结构
            json.put("outbounds", new JSONObject[]{outbound});
            
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
} 