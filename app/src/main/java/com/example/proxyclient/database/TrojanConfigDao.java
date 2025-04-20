package com.example.proxyclient.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proxyclient.model.TrojanConfig;

import java.util.List;

@Dao
public interface TrojanConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TrojanConfig config);

    @Update
    void update(TrojanConfig config);

    @Delete
    void delete(TrojanConfig config);

    @Query("SELECT * FROM trojan_configs ORDER BY name")
    LiveData<List<TrojanConfig>> getAllConfigs();

    @Query("SELECT * FROM trojan_configs WHERE id = :id")
    TrojanConfig getConfigById(long id);

    @Query("SELECT * FROM trojan_configs WHERE subscriptionUrl = :url")
    List<TrojanConfig> getConfigsBySubscription(String url);

    @Query("DELETE FROM trojan_configs WHERE subscriptionUrl = :url")
    void deleteConfigsBySubscription(String url);
} 