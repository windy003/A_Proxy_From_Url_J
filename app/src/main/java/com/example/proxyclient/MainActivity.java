package com.example.proxyclient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import java.nio.charset.StandardCharsets;
import com.example.proxyclient.adapter.TrojanConfigAdapter;
import com.example.proxyclient.model.TrojanConfig;
import com.example.proxyclient.model.TrojanNode;
import com.example.proxyclient.service.TrojanService;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.example.proxyclient.database.TrojanConfigDatabase;
import com.example.proxyclient.database.TrojanConfigDao;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private TrojanConfigAdapter adapter;
    private Handler mainHandler;
    private static final String PREFS_NAME = "TrojanPrefs";
    private static final String KEY_LAST_CONFIG = "last_config";
    private List<TrojanConfig> configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());
        
        // 初始化配置列表
        configs = new ArrayList<>();
        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrojanConfigAdapter(configs);
        recyclerView.setAdapter(adapter);

        // 首先加载所有配置
        loadConfigs();
        
        // 然后加载上次保存的配置
        loadLastConfig();

        // 设置按钮点击事件
        Button fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(v -> {
            EditText urlEditText = findViewById(R.id.urlEditText);
            String url = urlEditText.getText().toString().trim();
            if (!url.isEmpty()) {
                fetchAndParseUrl(url);
            }
        });
    }

    private void fetchAndParseUrl(String url) {
        new Thread(() -> {
            try {
                String base64Content = fetchUrlContent(url);
                if (base64Content == null) return;

                byte[] decodedBytes = Base64.decode(base64Content, Base64.DEFAULT);
                String decodedContent = new String(decodedBytes);

                List<TrojanNode> nodes = parseTrojanUrls(decodedContent);
                List<TrojanConfig> configs = new ArrayList<>();
                
                for (TrojanNode node : nodes) {
                    TrojanConfig config = new TrojanConfig(
                            node.getServer(),
                            node.getPort(),
                            node.getPassword(),
                            node.getName(),
                            node.getRegion()
                    );
                    configs.add(config);
                    
                    // 保存到数据库
                    TrojanConfigDatabase database = TrojanConfigDatabase.getInstance(this);
                    database.trojanConfigDao().insertConfig(config);
                }

                // 更新UI
                runOnUiThread(() -> {
                    this.configs.clear();
                    this.configs.addAll(configs);
                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "获取配置失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String fetchUrlContent(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        }
    }

    private List<TrojanNode> parseTrojanUrls(String content) {
        List<TrojanNode> nodes = new ArrayList<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("trojan://")) {
                nodes.add(new TrojanNode(line.trim()));
            }
        }
        return nodes;
    }

    private void connectToTrojan(TrojanConfig config) {
        try {
            showLoading("正在连接...");
            String serverAddress = config.getServerAddress();
            int port = config.getServerPort();
            String password = config.getPassword();
            
            TrojanService.getInstance().connect(serverAddress, port, password);
            showSuccess("连接成功");
            
            // 连接成功后保存配置
            saveLastConfig(config);
            
        } catch (Exception e) {
            showError("连接失败：" + e.getMessage());
        }
    }

    private void showLoading(String message) {
        // 使用 ProgressDialog 或 Toast 显示加载状态
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadConfigs() {
        new Thread(() -> {
            try {
                TrojanConfigDatabase database = TrojanConfigDatabase.getInstance(this);
                List<TrojanConfig> loadedConfigs = database.trojanConfigDao().getAllConfigs();
                
                runOnUiThread(() -> {
                    configs.clear();
                    configs.addAll(loadedConfigs);
                    adapter.notifyDataSetChanged();
                    
                    // 如果有保存的上次使用的配置，则加载它
                    loadLastConfig();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载配置失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void loadLastConfig() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastConfigJson = prefs.getString(KEY_LAST_CONFIG, null);
        
        if (lastConfigJson != null) {
            try {
                Gson gson = new Gson();
                TrojanConfig lastConfig = gson.fromJson(lastConfigJson, TrojanConfig.class);
                
                // 在配置列表中找到匹配的配置
                int position = findConfigPosition(lastConfig);
                if (position != -1) {
                    // 更新UI显示选中状态
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetChanged();
                    
                    // 自动连接
                    connectToTrojan(lastConfig);
                }
            } catch (Exception e) {
                showError("加载上次配置失败：" + e.getMessage());
            }
        }
    }
    
    // 查找配置在列表中的位置
    private int findConfigPosition(TrojanConfig config) {
        for (int i = 0; i < configs.size(); i++) {
            TrojanConfig item = configs.get(i);
            if (item.getServerAddress().equals(config.getServerAddress()) 
                && item.getServerPort() == config.getServerPort()) {
                return i;
            }
        }
        return -1;
    }
    
    // 在成功连接时保存配置
    private void saveLastConfig(TrojanConfig config) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        Gson gson = new Gson();
        String configJson = gson.toJson(config);
        
        editor.putString(KEY_LAST_CONFIG, configJson);
        editor.apply();
        
        // 确保配置也保存在数据库中
        new Thread(() -> {
            try {
                TrojanConfigDatabase database = TrojanConfigDatabase.getInstance(this);
                database.trojanConfigDao().insertConfig(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
