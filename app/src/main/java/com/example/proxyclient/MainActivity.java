package com.example.proxyclient;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proxyclient.service.ProxyVpnService;
import com.example.proxyclient.activity.LinkInputActivity;
import com.example.proxyclient.utils.NetworkUtils;
import com.example.proxyclient.service.TrojanService;
import com.example.proxyclient.adapter.ServerListAdapter;
import com.example.proxyclient.model.TrojanConfig;
import com.example.proxyclient.manager.ConfigManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnStartVpn;
    private boolean isVpnRunning = false;
    
    private final ActivityResultLauncher<Intent> vpnLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    startVpnService();
                } else {
                    Toast.makeText(this, "VPN权限被拒绝", Toast.LENGTH_SHORT).show();
                }
            });

    private static final int REQUEST_ADD_LINK = 1001;

    private RecyclerView serverListRecyclerView;
    private ServerListAdapter serverListAdapter;
    private ConfigManager configManager;
    private TrojanConfig currentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化服务和管理器
        TrojanService.getInstance().init(this);
        configManager = ConfigManager.getInstance(this);
        
        initViews();
        setupEventListeners();
        loadServerList();
    }
    
    private void initViews() {
        btnStartVpn = findViewById(R.id.btnStartVpn);
        serverListRecyclerView = findViewById(R.id.server_list);
        
        // 设置RecyclerView
        serverListAdapter = new ServerListAdapter();
        serverListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serverListRecyclerView.setAdapter(serverListAdapter);
    }

    private void setupEventListeners() {
        // VPN开关
        Switch vpnSwitch = findViewById(R.id.vpn_switch);
        vpnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (currentConfig != null) {
                    prepareVpn();
                } else {
                    Toast.makeText(this, "请先选择一个服务器", Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(false);
                }
            } else {
                stopVpnService();
            }
        });

        // 快速连接
        Button quickConnectButton = findViewById(R.id.quick_connect_button);
        quickConnectButton.setOnClickListener(v -> quickConnect());

        // 添加到列表
        Button addToListButton = findViewById(R.id.add_to_list_button);
        addToListButton.setOnClickListener(v -> addToServerList());

        // 服务器列表点击事件
        serverListAdapter.setOnServerClickListener(new ServerListAdapter.OnServerClickListener() {
            @Override
            public void onServerClick(TrojanConfig config, int position) {
                selectServer(config, position);
            }

            @Override
            public void onServerLongClick(TrojanConfig config, int position) {
                showServerMenu(config, position);
            }
        });

        // 测试连接
        Button testConnectionButton = findViewById(R.id.test_connection_button);
        testConnectionButton.setOnClickListener(v -> testConnection());
    }

    private void quickConnect() {
        EditText quickUrlInput = findViewById(R.id.quick_url_input);
        String url = quickUrlInput.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入Trojan链接", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            TrojanConfig config = TrojanConfig.fromUrl(url);
            connectToServer(config);
        } catch (Exception e) {
            Toast.makeText(this, "链接格式错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addToServerList() {
        EditText quickUrlInput = findViewById(R.id.quick_url_input);
        String url = quickUrlInput.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入Trojan链接", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            TrojanConfig config = TrojanConfig.fromUrl(url);
            configManager.saveConfig(config, new ConfigManager.SaveCallback() {
                @Override
                public void onSuccess(TrojanConfig savedConfig) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "服务器已添加", Toast.LENGTH_SHORT).show();
                        quickUrlInput.setText("");
                        loadServerList();
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "保存失败: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "链接格式错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadServerList() {
        configManager.getAllConfigs(new ConfigManager.LoadCallback() {
            @Override
            public void onSuccess(List<TrojanConfig> configs) {
                runOnUiThread(() -> {
                    serverListAdapter.updateServerList(configs);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "加载服务器列表失败: " + message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void selectServer(TrojanConfig config, int position) {
        currentConfig = config;
        serverListAdapter.setSelectedPosition(position);
        
        TextView currentServer = findViewById(R.id.current_server);
        currentServer.setText("当前服务器: " + config.getName());
    }

    private void connectToServer(TrojanConfig config) {
        TrojanService.getInstance().connect(config, new TrojanService.ConnectCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    updateConnectionStatus(true, config.getName());
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    updateConnectionStatus(false, "");
                    Toast.makeText(MainActivity.this, "连接失败: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void updateConnectionStatus(boolean connected, String serverName) {
        TextView connectionStatus = findViewById(R.id.connection_status);
        Switch vpnSwitch = findViewById(R.id.vpn_switch);
        
        if (connected) {
            connectionStatus.setText("已连接");
            connectionStatus.setTextColor(getColor(android.R.color.holo_green_dark));
            vpnSwitch.setChecked(true);
        } else {
            connectionStatus.setText("未连接");
            connectionStatus.setTextColor(getColor(android.R.color.holo_red_dark));
            vpnSwitch.setChecked(false);
        }
    }

    private void prepareVpn() {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null) {
            vpnLauncher.launch(vpnIntent);
        } else {
            startVpnService();
        }
    }
    
    private void startVpnService() {
        if (!isVpnRunning) {
            Intent intent = new Intent(this, ProxyVpnService.class);
            // 传递Trojan的本地SOCKS5代理端口
            intent.putExtra("trojanPort", 1080); // 确保这与TrojanConfig中的本地端口一致
            startService(intent);
            btnStartVpn.setText("停止VPN");
            isVpnRunning = true;
        } else {
            Intent intent = new Intent(this, ProxyVpnService.class);
            stopService(intent);
            btnStartVpn.setText("启动VPN");
            isVpnRunning = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_LINK && resultCode == RESULT_OK) {
            refreshProxyList();
        }
    }

    private void refreshProxyList() {
        // 实现刷新列表的逻辑
        // 这里可以从数据库重新加载配置列表
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_link) {
            Intent intent = new Intent(this, LinkInputActivity.class);
            startActivityForResult(intent, REQUEST_ADD_LINK);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void testConnection() {
        Toast.makeText(this, "正在测试连接...", Toast.LENGTH_SHORT).show();
        NetworkUtils.testProxyConnection("127.0.0.1", 1080, new NetworkUtils.TestCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "连接测试成功！可以正常访问外网", Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "连接测试失败: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void stopVpnService() {
        if (isVpnRunning) {
            Intent intent = new Intent(this, ProxyVpnService.class);
            stopService(intent);
            btnStartVpn.setText("启动VPN");
            isVpnRunning = false;
            
            // 断开Trojan连接
            TrojanService.getInstance().disconnect();
            
            // 更新UI状态
            updateConnectionStatus(false, "");
            
            Toast.makeText(this, "VPN已停止", Toast.LENGTH_SHORT).show();
        }
    }

    private void showServerMenu(TrojanConfig config, int position) {
        // 创建弹出菜单
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, serverListRecyclerView.getLayoutManager().findViewByPosition(position));
        popup.getMenuInflater().inflate(R.menu.server_menu, popup.getMenu());
        
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_connect) {
                selectServer(config, position);
                connectToServer(config);
                return true;
            } else if (itemId == R.id.action_edit) {
                editServer(config);
                return true;
            } else if (itemId == R.id.action_delete) {
                deleteServer(config, position);
                return true;
            }
            return false;
        });
        
        popup.show();
    }

    private void editServer(TrojanConfig config) {
        // 打开编辑界面
        Intent intent = new Intent(this, LinkInputActivity.class);
        intent.putExtra("edit_config", config.toUrl());
        startActivityForResult(intent, REQUEST_ADD_LINK);
    }

    private void deleteServer(TrojanConfig config, int position) {
        new android.app.AlertDialog.Builder(this)
            .setTitle("删除服务器")
            .setMessage("确定要删除服务器 \"" + config.getName() + "\" 吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                configManager.deleteConfig(config, new ConfigManager.DeleteCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "服务器已删除", Toast.LENGTH_SHORT).show();
                            loadServerList();
                            
                            // 如果删除的是当前选中的服务器，清除选择
                            if (currentConfig != null && currentConfig.getId() == config.getId()) {
                                currentConfig = null;
                                TextView currentServer = findViewById(R.id.current_server);
                                currentServer.setText("当前服务器: 无");
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "删除失败: " + message, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            })
            .setNegativeButton("取消", null)
            .show();
    }
}
