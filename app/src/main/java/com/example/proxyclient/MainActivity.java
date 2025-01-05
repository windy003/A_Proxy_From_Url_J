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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private EditText urlEditText;
    private Button fetchButton;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化视图
        urlEditText = findViewById(R.id.urlEditText);
        fetchButton = findViewById(R.id.fetchButton);
        recyclerView = findViewById(R.id.recyclerView);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        
        // 设置按钮点击事件
        fetchButton.setOnClickListener(v -> fetchAndParseUrl());
        
        // 设置列表项点击事件
        adapter.setOnItemClickListener(trojanUrl -> {
            Toast.makeText(MainActivity.this, 
                "正在连接: " + trojanUrl, 
                Toast.LENGTH_SHORT).show();
            connectToTrojan(trojanUrl);
        });
    }

    private void fetchAndParseUrl() {
        String url = urlEditText.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入URL", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // 获取base64内容
                String base64Content = fetchUrlContent(url);
                if (base64Content == null) return;

                // 解码base64
                byte[] decodedBytes = Base64.decode(base64Content, Base64.DEFAULT);
                String decodedContent = new String(decodedBytes);

                // 解析trojan链接
                List<String> trojanUrls = parseTrojanUrls(decodedContent);

                // 在主线程更新UI
                mainHandler.post(() -> {
                    adapter.updateData(trojanUrls);
                    Toast.makeText(MainActivity.this, 
                        "成功获取 " + trojanUrls.size() + " 个节点", 
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

    private List<String> parseTrojanUrls(String content) {
        List<String> urls = new ArrayList<>();
        // 按行分割
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("trojan://")) {
                urls.add(line.trim());
            }
        }
        return urls;
    }

    private void connectToTrojan(String trojanUrl) {
        // TODO: 实现trojan连接逻辑
        Log.d(TAG, "Connecting to: " + trojanUrl);
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> dataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String trojanUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MyAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    public void updateData(List<String> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = dataList.get(position);
        holder.textView.setText(item);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
