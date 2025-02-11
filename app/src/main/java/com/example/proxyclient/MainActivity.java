package com.example.proxyclient;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proxyclient.service.ProxyVpnService;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnStartVpn = findViewById(R.id.btnStartVpn);
        btnStartVpn.setOnClickListener(v -> prepareVpn());
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
}
