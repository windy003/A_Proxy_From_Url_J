package com.example.proxyclient.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.proxyclient.model.XrayConfig;

@Database(entities = {XrayConfig.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // 后续添加 Xray 相关的 DAO
} 