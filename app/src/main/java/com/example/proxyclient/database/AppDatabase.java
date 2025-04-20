package com.example.proxyclient.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.proxyclient.model.XrayConfig;
import com.example.proxyclient.model.TrojanConfig;

@Database(entities = {XrayConfig.class, TrojanConfig.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // DAO 声明
    public abstract XrayConfigDao xrayConfigDao();
    public abstract TrojanConfigDao trojanConfigDao();
} 