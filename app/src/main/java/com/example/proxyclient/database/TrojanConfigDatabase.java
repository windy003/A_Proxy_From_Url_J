package com.example.proxyclient.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.proxyclient.model.TrojanConfig;

@Database(entities = {TrojanConfig.class}, version = 1, exportSchema = false)
public abstract class TrojanConfigDatabase extends RoomDatabase {
    private static TrojanConfigDatabase instance;
    
    public abstract TrojanConfigDao trojanConfigDao();
    
    public static synchronized TrojanConfigDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                TrojanConfigDatabase.class,
                "trojan_config_db"
            ).build();
        }
        return instance;
    }
} 