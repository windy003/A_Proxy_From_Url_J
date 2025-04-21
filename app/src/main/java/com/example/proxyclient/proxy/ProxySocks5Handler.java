package com.example.proxyclient.proxy;

import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 处理VPN和SOCKS5代理之间的通信
 */
public class ProxySocks5Handler {
    private static final String TAG = "ProxySocks5Handler";
    private final String proxyHost;
    private final int proxyPort;
    
    public ProxySocks5Handler(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
    
    /**
     * 处理VPN数据包并转发到SOCKS5代理
     */
    public void handleVpnPacket(byte[] packet, int length, FileOutputStream vpnOutput) {
        // 1. 解析IP包头，获取目标地址和端口
        try {
            ByteBuffer buffer = ByteBuffer.wrap(packet, 0, length);
            
            // 检查是否是IPv4包
            if ((buffer.get(0) & 0xF0) != 0x40) {
                return; // 不处理非IPv4数据包
            }
            
            int protocol = buffer.get(9) & 0xFF;
            if (protocol != 6 && protocol != 17) {
                return; // 只处理TCP和UDP
            }
            
            int sourcePort = (buffer.get(20) & 0xFF) << 8 | (buffer.get(21) & 0xFF);
            int destPort = (buffer.get(22) & 0xFF) << 8 | (buffer.get(23) & 0xFF);
            
            byte[] sourceIP = new byte[4];
            byte[] destIP = new byte[4];
            
            buffer.position(12);
            buffer.get(sourceIP);
            buffer.get(destIP);
            
            InetAddress destAddr = InetAddress.getByAddress(destIP);
            
            // 2. 创建到SOCKS5代理的连接
            Socket proxySocket = new Socket(proxyHost, proxyPort);
            
            // 3. 进行SOCKS5握手
            performSocks5Handshake(proxySocket, destAddr.getHostAddress(), destPort);
            
            // 4. 转发数据
            // 从VPN接收数据并发送到代理
            byte[] dataToProxy = new byte[length - 20]; // 去除IP头
            System.arraycopy(packet, 20, dataToProxy, 0, dataToProxy.length);
            proxySocket.getOutputStream().write(dataToProxy);
            
            // 从代理接收数据并发送到VPN
            byte[] dataFromProxy = new byte[VPN_MTU];
            int bytesRead = proxySocket.getInputStream().read(dataFromProxy);
            
            if (bytesRead > 0) {
                // 构建返回数据包
                ByteBuffer responseBuffer = ByteBuffer.allocate(bytesRead + 20);
                // 构建IP头...
                responseBuffer.put(dataFromProxy, 0, bytesRead);
                
                // 写回VPN
                vpnOutput.write(responseBuffer.array(), 0, responseBuffer.position());
            }
            
            proxySocket.close();
            
        } catch (IOException e) {
            Log.e(TAG, "处理VPN数据包失败", e);
        }
    }
    
    /**
     * 执行SOCKS5协议握手
     */
    private void performSocks5Handshake(Socket socket, String targetHost, int targetPort) throws IOException {
        // SOCKS5握手第一步：验证方法协商
        socket.getOutputStream().write(new byte[]{
            0x05, // SOCKS版本
            0x01, // 认证方法数量
            0x00  // 不需要认证
        });
        
        byte[] response = new byte[2];
        socket.getInputStream().read(response);
        
        if (response[0] != 0x05 || response[1] != 0x00) {
            throw new IOException("SOCKS5握手失败");
        }
        
        // SOCKS5握手第二步：连接请求
        byte[] hostBytes = targetHost.getBytes();
        ByteBuffer request = ByteBuffer.allocate(7 + hostBytes.length);
        
        request.put((byte) 0x05); // SOCKS版本
        request.put((byte) 0x01); // TCP连接
        request.put((byte) 0x00); // 保留字段
        request.put((byte) 0x03); // 域名类型
        request.put((byte) hostBytes.length); // 域名长度
        request.put(hostBytes); // 目标域名
        request.put((byte) ((targetPort >> 8) & 0xFF)); // 目标端口高位字节
        request.put((byte) (targetPort & 0xFF)); // 目标端口低位字节
        
        socket.getOutputStream().write(request.array());
        
        // 读取响应
        byte[] connectResponse = new byte[10];
        socket.getInputStream().read(connectResponse);
        
        if (connectResponse[0] != 0x05 || connectResponse[1] != 0x00) {
            throw new IOException("SOCKS5连接请求失败: " + connectResponse[1]);
        }
        
        // 握手完成，现在可以通过这个Socket传输数据
    }
    
    private static final int VPN_MTU = 1500;
} 