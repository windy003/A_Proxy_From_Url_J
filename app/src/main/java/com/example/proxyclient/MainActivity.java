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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private TrojanConfigAdapter adapter;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());
        
        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrojanConfigAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

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
                // 获取base64内容
                String base64Content = fetchUrlContent(url);
                if (base64Content == null) return;

                // 解码base64
                byte[] decodedBytes = Base64.decode(base64Content, Base64.DEFAULT);
                String decodedContent = new String(decodedBytes);

                // 解析trojan链接
                List<TrojanNode> nodes = parseTrojanUrls(decodedContent);

                // 转换为TrojanConfig列表
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
                }

                // 在主线程更新UI
                mainHandler.post(() -> {
                    adapter.updateData(configs);
                    Toast.makeText(MainActivity.this, 
                        "成功获取 " + configs.size() + " 个节点", 
                        Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                Log.e(TAG, "Error fetching or parsing URL", e);
                mainHandler.post(() -> 
                    Toast.makeText(MainActivity.this, 
                        "错误: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show()
                );
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
}
