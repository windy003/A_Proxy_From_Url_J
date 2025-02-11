package com.example.proxyclient.service;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.proxyclient.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class VpnTileService extends TileService {
    private static final String TAG = "VpnTileService";
    private static boolean isVpnRunning = false;

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (!isVpnRunning) {
            startVpn();
        } else {
            stopVpn();
        }
        updateTile();
    }

    private void startVpn() {
        Intent intent = new Intent(this, ProxyVpnService.class);
        startService(intent);
        isVpnRunning = true;
    }

    private void stopVpn() {
        Intent intent = new Intent(this, ProxyVpnService.class);
        stopService(intent);
        isVpnRunning = false;
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile == null) return;

        if (isVpnRunning) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground));
            tile.setLabel("VPN 运行中");
        } else {
            tile.setState(Tile.STATE_INACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_foreground));
            tile.setLabel("VPN 已停止");
        }
        tile.updateTile();
    }
} 