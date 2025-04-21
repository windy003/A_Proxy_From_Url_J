package com.example.proxyclient.utils;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * 网络工具类
 */
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    
    /**
     * 测试代理连接
     */
    public static void testProxyConnection(String proxyHost, int proxyPort, TestCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            private String errorMessage;
            
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    // 创建代理
                    Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));
                    
                    // 使用代理连接到测试网站
                    URL url = new URL("https://www.google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    
                    int responseCode = connection.getResponseCode();
                    return responseCode == 200;
                } catch (IOException e) {
                    errorMessage = e.getMessage();
                    Log.e(TAG, "测试代理连接失败", e);
                    return false;
                }
            }
            
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(errorMessage);
                }
            }
        }.execute();
    }
    
    public interface TestCallback {
        void onSuccess();
        void onFailure(String message);
    }
} 