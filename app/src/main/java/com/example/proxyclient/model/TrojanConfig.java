package com.example.proxyclient.model;

public class TrojanConfig {
    private String serverAddress;
    private int serverPort;
    private String password;
    private String remark;
    private String region;
    private String peer;
    private String sni;
    private boolean allowInsecure;

    public TrojanConfig(String serverAddress, int serverPort, String password, String remark, String region) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.password = password;
        this.remark = remark;
        this.region = region;
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

    // Setters
    public void setServerAddress(String serverAddress) { this.serverAddress = serverAddress; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }
    public void setPassword(String password) { this.password = password; }
    public void setRemark(String remark) { this.remark = remark; }
    public void setRegion(String region) { this.region = region; }
    public void setPeer(String peer) { this.peer = peer; }
    public void setSni(String sni) { this.sni = sni; }
    public void setAllowInsecure(boolean allowInsecure) { this.allowInsecure = allowInsecure; }
}