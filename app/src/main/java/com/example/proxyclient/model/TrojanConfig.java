package com.example.proxyclient.model;

public class TrojanConfig {
    private String serverAddress;
    private int serverPort;
    private String password;
    private String remark;

    public TrojanConfig(String serverAddress, int serverPort, String password, String remark) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.password = password;
        this.remark = remark;
    }

    // Getters
    public String getServerAddress() { return serverAddress; }
    public int getServerPort() { return serverPort; }
    public String getPassword() { return password; }
    public String getRemark() { return remark; }

    // Setters
    public void setServerAddress(String serverAddress) { this.serverAddress = serverAddress; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }
    public void setPassword(String password) { this.password = password; }
    public void setRemark(String remark) { this.remark = remark; }
}