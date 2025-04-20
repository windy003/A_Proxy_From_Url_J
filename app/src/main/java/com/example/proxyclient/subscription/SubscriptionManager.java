package com.example.proxyclient.subscription;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.example.proxyclient.database.AppDatabase;
import com.example.proxyclient.database.XrayConfigDao;
import com.example.proxyclient.database.TrojanConfigDao;
import com.example.proxyclient.model.XrayConfig;
import com.example.proxyclient.model.TrojanConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订阅管理器，用于处理代理服务器订阅
 */
public class SubscriptionManager {
    private static final String TAG = "SubscriptionManager";
    private static SubscriptionManager instance;
    private final ExecutorService executorService;
    private final AppDatabase database;
    private final Context context;

    private SubscriptionManager(Context context, AppDatabase database) {
        this.context = context.getApplicationContext();
        this.database = database;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static synchronized SubscriptionManager getInstance(Context context, AppDatabase database) {
        if (instance == null) {
            instance = new SubscriptionManager(context, database);
        }
        return instance;
    }

    /**
     * 更新订阅
     * @param subscriptionUrl 订阅URL
     * @param callback 回调
     */
    public void updateSubscription(String subscriptionUrl, SubscriptionCallback callback) {
        executorService.execute(() -> {
            try {
                // 下载订阅内容
                String content = downloadSubscription(subscriptionUrl);
                if (content == null || content.isEmpty()) {
                    callback.onError("下载订阅内容失败");
                    return;
                }

                // 解析订阅内容
                List<Object> configs = parseSubscription(content);
                if (configs.isEmpty()) {
                    callback.onError("订阅内容为空或格式错误");
                    return;
                }

                // 更新数据库
                updateDatabase(subscriptionUrl, configs);

                // 回调成功
                callback.onSuccess(configs.size());
            } catch (Exception e) {
                Log.e(TAG, "更新订阅失败", e);
                callback.onError("更新订阅失败: " + e.getMessage());
            }
        });
    }

    /**
     * 下载订阅内容
     */
    private String downloadSubscription(String subscriptionUrl) throws Exception {
        URL url = new URL(subscriptionUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP错误代码: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * 解析订阅内容
     */
    private List<Object> parseSubscription(String content) {
        List<Object> configs = new ArrayList<>();

        try {
            // 尝试Base64解码
            byte[] decodedBytes = Base64.decode(content, Base64.DEFAULT);
            String decodedContent = new String(decodedBytes, StandardCharsets.UTF_8);

            // 按行分割
            String[] lines = decodedContent.split("\\r?\\n");

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 解析各种协议
                if (line.startsWith("trojan://")) {
                    TrojanConfig config = TrojanConfig.fromUrl(line);
                    configs.add(config);
                } else if (line.startsWith("vmess://") || 
                          line.startsWith("vless://") || 
                          line.startsWith("ss://")) {
                    // 这里可以添加其他协议的解析逻辑
                    XrayConfig config = parseXrayLink(line);
                    if (config != null) {
                        configs.add(config);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "解析订阅内容失败", e);
        }

        return configs;
    }

    /**
     * 解析Xray链接（vmess, vless, ss等）
     */
    private XrayConfig parseXrayLink(String link) {
        // 这里实现各种协议链接的解析
        // 根据不同协议的链接格式，解析出对应的配置
        // 为简化示例，这里只返回null
        return null; // 完整实现请根据实际需求添加
    }

    /**
     * 更新数据库
     */
    private void updateDatabase(String subscriptionUrl, List<Object> configs) {
        // 清除旧的订阅配置
        TrojanConfigDao trojanConfigDao = database.trojanConfigDao();
        XrayConfigDao xrayConfigDao = database.xrayConfigDao();
        
        trojanConfigDao.deleteConfigsBySubscription(subscriptionUrl);
        xrayConfigDao.deleteConfigsBySubscription(subscriptionUrl);

        // 添加新的配置
        for (Object obj : configs) {
            if (obj instanceof TrojanConfig) {
                TrojanConfig config = (TrojanConfig) obj;
                config.setSubscriptionUrl(subscriptionUrl);
                trojanConfigDao.insert(config);
            } else if (obj instanceof XrayConfig) {
                XrayConfig config = (XrayConfig) obj;
                config.setSubscriptionUrl(subscriptionUrl);
                xrayConfigDao.insert(config);
            }
        }
    }

    /**
     * 订阅回调接口
     */
    public interface SubscriptionCallback {
        void onSuccess(int count);
        void onError(String message);
    }
} 