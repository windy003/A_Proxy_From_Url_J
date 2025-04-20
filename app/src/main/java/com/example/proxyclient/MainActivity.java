package com.example.proxyclient;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proxyclient.service.ProxyVpnService;
import com.example.proxyclient.activity.LinkInputActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnStartVpn = findViewById(R.id.btnStartVpn);
        btnStartVpn.setOnClickListener(v -> prepareVpn());

        Button addLinkButton = findViewById(R.id.add_link_button);
        addLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 打开链接输入界面
                    Intent intent = new Intent(MainActivity.this, LinkInputActivity.class);
                    startActivityForResult(intent, REQUEST_ADD_LINK);
                } catch (Exception e) {
                    // 记录异常
                    Log.e("MainActivity", "Error starting LinkInputActivity", e);
                    // 向用户显示错误信息
                    Toast.makeText(MainActivity.this, 
                        "打开链接输入界面失败: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                }
            }
        });
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
}
