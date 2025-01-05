package com.example.proxyclient.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import com.example.proxyclient.model.TrojanConfig;
import java.util.List;

@Dao
public interface TrojanConfigDao {
    @Query("SELECT * FROM trojan_config")
    List<TrojanConfig> getAllConfigs();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConfig(TrojanConfig config);
    
    @Query("SELECT * FROM trojan_config WHERE serverAddress = :address AND serverPort = :port LIMIT 1")
    TrojanConfig findConfig(String address, int port);
} 