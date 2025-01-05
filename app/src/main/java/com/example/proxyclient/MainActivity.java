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
import android.content.Intent;
import android.net.VpnService;
import com.example.proxyclient.service.ProxyVpnService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.app.ActivityManager;
import android.widget.Switch;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int VPN_REQUEST_CODE = 1;
    
    private EditText urlInput;
    private Button parseButton;
    private ListView nodeList;
    private Switch vpnSwitch;
    private TextView nodeInfo;
    private TrojanConfig lastConfig;
    private BroadcastReceiver vpnStateReceiver;
    private ArrayAdapter<TrojanConfig> adapter;
    private List<TrojanConfig> configs = new ArrayList<>();

    private static final String PREFS_NAME = "TrojanPrefs";
    private static final String KEY_SUBSCRIPTION_URL = "subscription_url";
    private static final String KEY_NODES = "nodes_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlInput);
        parseButton = findViewById(R.id.parseButton);
        nodeList = findViewById(R.id.nodeList);
        vpnSwitch = findViewById(R.id.vpnSwitch);
        nodeInfo = findViewById(R.id.nodeInfo);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, configs);
        nodeList.setAdapter(adapter);

        parseButton.setOnClickListener(v -> parseSubscription());
        vpnSwitch.setOnClickListener(v -> toggleVpn());

        nodeList.setOnItemClickListener((parent, view, position, id) -> {
            TrojanConfig config = configs.get(position);
            saveConfig(config);
            Toast.makeText(this, "已选择节点: " + config.getServerAddress(), Toast.LENGTH_SHORT).show();
        });

        // 注册 VPN 状态广播接收器
        vpnStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constants.ACTION_VPN_STATE_CHANGED.equals(intent.getAction())) {
                    boolean isConnected = intent.getBooleanExtra(Constants.EXTRA_VPN_STATE, false);
                    updateVpnState(isConnected);
                }
            }
        };
        registerReceiver(vpnStateReceiver, new IntentFilter(Constants.ACTION_VPN_STATE_CHANGED),
                Context.RECEIVER_NOT_EXPORTED);

        // 初始化 VPN 状态
        updateVpnState(isVpnServiceRunning());

        // 恢复上次的订阅链接
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUrl = prefs.getString(KEY_SUBSCRIPTION_URL, "");
        urlInput.setText(savedUrl);

        // 恢复保存的节点列表
        String savedNodes = prefs.getString(KEY_NODES, null);
        if (savedNodes != null) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<List<TrojanConfig>>(){}.getType();
                List<TrojanConfig> savedConfigs = gson.fromJson(savedNodes, type);
                configs.clear();
                configs.addAll(savedConfigs);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Restored " + configs.size() + " saved nodes");
            } catch (Exception e) {
                Log.e(TAG, "Error restoring saved nodes", e);
            }
        }
    }

    private void parseSubscription() {
        String url = urlInput.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入订阅链接", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存订阅链接
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_SUBSCRIPTION_URL, url).apply();

        new Thread(() -> {
            try {
                String base64Content = downloadUrl(url);
                String decodedContent = new String(Base64.decode(base64Content, Base64.DEFAULT));
                List<TrojanConfig> newConfigs = parseTrojanUrls(decodedContent);
                
                runOnUiThread(() -> {
                    configs.clear();
                    configs.addAll(newConfigs);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "解析成功: " + newConfigs.size() + " 个节点", Toast.LENGTH_SHORT).show();
                    
                    // 保存节点列表
                    try {
                        Gson gson = new Gson();
                        String nodesJson = gson.toJson(configs);
                        prefs.edit().putString(KEY_NODES, nodesJson).apply();
                        Log.d(TAG, "Saved " + configs.size() + " nodes");
                    } catch (Exception e) {
                        Log.e(TAG, "Error saving nodes", e);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error parsing subscription", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "解析失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } finally {
            conn.disconnect();
        }
    }

    private List<TrojanConfig> parseTrojanUrls(String content) {
        List<TrojanConfig> configs = new ArrayList<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("trojan://")) {
                try {
                    configs.add(TrojanConfig.fromUrl(line));
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing trojan URL: " + line, e);
                }
            }
        }
        return configs;
    }

    private void saveConfig(TrojanConfig config) {
        SharedPreferences prefs = getSharedPreferences("TrojanPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        prefs.edit().putString("last_config", gson.toJson(config)).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vpnStateReceiver != null) {
            unregisterReceiver(vpnStateReceiver);
        }
    }

    private void toggleVpn() {
        if (vpnSwitch.isChecked()) {
            startVpn();
        } else {
            stopVpn();
        }
    }

    private void startVpn() {
        try {
            SharedPreferences prefs = getSharedPreferences("TrojanPrefs", MODE_PRIVATE);
            String configJson = prefs.getString("last_config", null);
            
            if (configJson == null) {
                showError("请先选择节点");
                vpnSwitch.setChecked(false);
                return;
            }

            Gson gson = new Gson();
            TrojanConfig config = gson.fromJson(configJson, TrojanConfig.class);
            
            if (config == null) {
                showError("配置无效");
                vpnSwitch.setChecked(false);
                return;
            }

            Intent prepare = VpnService.prepare(this);
            if (prepare != null) {
                startActivityForResult(prepare, VPN_REQUEST_CODE);
                lastConfig = config;
                return;
            }
            
            startVpnService(config);
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to start VPN", e);
            showError("启动失败：" + e.getMessage());
            vpnSwitch.setChecked(false);
        }
    }

    private void startVpnService(TrojanConfig config) {
        Intent intent = new Intent(this, ProxyVpnService.class);
        intent.putExtra("server", config.getServerAddress());
        intent.putExtra("port", config.getServerPort());
        intent.putExtra("password", config.getPassword());
        intent.putExtra("sni", config.getSni());
        intent.putExtra("allowInsecure", config.isAllowInsecure());
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (lastConfig != null) {
                startVpnService(lastConfig);
            }
        } else {
            vpnSwitch.setChecked(false);
        }
    }

    private void stopVpn() {
        stopService(new Intent(this, ProxyVpnService.class));
    }

    private void updateVpnState(boolean isConnected) {
        vpnSwitch.setChecked(isConnected);
        
        if (isConnected) {
            try {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String configJson = prefs.getString("last_config", null);
                if (configJson != null) {
                    Gson gson = new Gson();
                    TrojanConfig config = gson.fromJson(configJson, TrojanConfig.class);
                    if (config != null) {
                        nodeInfo.setVisibility(View.VISIBLE);
                        String displayName = config.getName() != null ? config.getName() : 
                                           config.getServerAddress() + ":" + config.getServerPort();
                        nodeInfo.setText(String.format("当前连接: %s", displayName));
                        return;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error showing node info", e);
            }
        }
        
        nodeInfo.setVisibility(View.GONE);
    }

    private boolean isVpnServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ProxyVpnService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
