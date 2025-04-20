package com.example.proxyclient.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proxyclient.model.XrayConfig;

import java.util.List;

/**
 * Xray配置的数据访问对象接口
 */
@Dao
public interface XrayConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(XrayConfig config);

    @Update
    void update(XrayConfig config);

    @Delete
    void delete(XrayConfig config);

    @Query("SELECT * FROM xray_configs ORDER BY name")
    LiveData<List<XrayConfig>> getAllConfigs();

    @Query("SELECT * FROM xray_configs WHERE id = :id")
    XrayConfig getConfigById(long id);

    @Query("SELECT * FROM xray_configs WHERE subscriptionUrl = :url")
    List<XrayConfig> getConfigsBySubscription(String url);

    @Query("DELETE FROM xray_configs WHERE subscriptionUrl = :url")
    void deleteConfigsBySubscription(String url);
    
    @Query("SELECT COUNT(*) FROM xray_configs")
    int getConfigCount();
} 