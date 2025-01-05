package com.example.proxyclient.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.example.proxyclient.R;
import com.example.proxyclient.model.TrojanConfig;
import com.example.proxyclient.Constants;
import com.example.proxyclient.MainActivity;

public class VpnTileService extends TileService {
    private static final String TAG = "VpnTileService";
    private boolean isVpnActive = false;
    private boolean isProcessing = false;
    private BroadcastReceiver vpnStateReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Log.d(TAG, "VpnTileService onCreate");
            vpnStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Constants.ACTION_VPN_STATE_CHANGED.equals(intent.getAction())) {
                        boolean newState = intent.getBooleanExtra(Constants.EXTRA_VPN_STATE, false);
                        Log.d(TAG, "Received VPN state broadcast: " + newState);
                        isVpnActive = newState;
                        updateTile();
                    }
                }
            };
            
            IntentFilter filter = new IntentFilter(Constants.ACTION_VPN_STATE_CHANGED);
            registerReceiver(vpnStateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            Log.d(TAG, "Broadcast receiver registered");
            
            // 初始化时检查 VPN 状态
            isVpnActive = isVpnServiceRunning();
            updateTile();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
        }
    }

    private boolean isVpnServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ProxyVpnService.class.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "Found VPN service running");
                return true;
            }
        }
        Log.d(TAG, "VPN service not running");
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vpnStateReceiver != null) {
            try {
                unregisterReceiver(vpnStateReceiver);
                Log.d(TAG, "Broadcast receiver unregistered");
            } catch (Exception e) {
                Log.e(TAG, "Error unregistering receiver", e);
            }
        }
    }

    @Override
    public void onClick() {
        try {
            super.onClick();
            Log.d(TAG, "=== VPN TILE CLICKED ===");
            Log.d(TAG, "Current VPN state: " + isVpnActive);

            if (isProcessing) {
                Log.d(TAG, "Already processing a request, ignoring click");
                return;
            }
            isProcessing = true;

            if (isVpnActive) {
                Log.d(TAG, "=== STOPPING VPN ===");
                stopService(new Intent(this, ProxyVpnService.class));
            } else {
                Log.d(TAG, "=== STARTING VPN ===");
                Intent prepare = VpnService.prepare(getApplicationContext());
                if (prepare != null) {
                    Log.d(TAG, "No VPN permission, launching MainActivity");
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityAndCollapse(intent);
                    isProcessing = false;
                    return;
                }
                startVpn();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onClick", e);
            showError("操作失败: " + e.getMessage());
        } finally {
            isProcessing = false;
        }
    }

    private void startVpn() {
        try {
            SharedPreferences prefs = getSharedPreferences("TrojanPrefs", MODE_PRIVATE);
            String configJson = prefs.getString("last_config", null);
            
            if (configJson == null) {
                Log.d(TAG, "No saved config found");
                showError("未找到配置");
                return;
            }
            
            Log.d(TAG, "Found saved config");
            Gson gson = new Gson();
            TrojanConfig config = gson.fromJson(configJson, TrojanConfig.class);
            
            if (config == null) {
                showError("配置无效");
                return;
            }

            Log.d(TAG, "Parsed config: \nServer: " + config.getServerAddress() + "\nPort: " + config.getServerPort());
            
            Intent intent = new Intent(this, ProxyVpnService.class);
            intent.putExtra("server", config.getServerAddress());
            intent.putExtra("port", config.getServerPort());
            intent.putExtra("password", config.getPassword());
            intent.putExtra("sni", config.getSni());
            intent.putExtra("allowInsecure", config.isAllowInsecure());

            startService(intent);
            Log.d(TAG, "VPN service start requested");
            
            isVpnActive = true;
            updateTile();
        } catch (Exception e) {
            Log.e(TAG, "Failed to start VPN", e);
            showError("启动 VPN 失败: " + e.getMessage());
        }
    }

    private void updateTile() {
        try {
            Tile tile = getQsTile();
            if (tile != null) {
                tile.setState(isVpnActive ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
                tile.setIcon(Icon.createWithResource(this, 
                    isVpnActive ? R.drawable.ic_vpn_connected : R.drawable.ic_vpn_disconnected));
                tile.setLabel(isVpnActive ? "VPN 已连接" : "VPN 未连接");
                
                tile.updateTile();
                Log.d(TAG, "Tile updated, state: " + (isVpnActive ? "active" : "inactive"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating tile", e);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
} 