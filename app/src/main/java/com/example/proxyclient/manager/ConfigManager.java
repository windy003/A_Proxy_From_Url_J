package com.example.proxyclient.manager;

import android.content.Context;
import androidx.room.Room;
import com.example.proxyclient.database.AppDatabase;
import com.example.proxyclient.model.TrojanConfig;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigManager {
    private static ConfigManager instance;
    private AppDatabase database;
    private ExecutorService executorService;
    
    private ConfigManager(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "proxy_database")
                .build();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public static ConfigManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager(context);
                }
            }
        }
        return instance;
    }
    
    public void saveConfig(TrojanConfig config, SaveCallback callback) {
        executorService.execute(() -> {
            try {
                long id = database.trojanConfigDao().insert(config);
                config.setId(id);
                if (callback != null) {
                    callback.onSuccess(config);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }
    
    public void getAllConfigs(LoadCallback callback) {
        executorService.execute(() -> {
            try {
                List<TrojanConfig> configs = database.trojanConfigDao().getAll();
                if (callback != null) {
                    callback.onSuccess(configs);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }
    
    public void deleteConfig(TrojanConfig config, DeleteCallback callback) {
        executorService.execute(() -> {
            try {
                database.trojanConfigDao().delete(config);
                if (callback != null) {
                    callback.onSuccess();
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }
    
    public interface SaveCallback {
        void onSuccess(TrojanConfig config);
        void onError(String message);
    }
    
    public interface LoadCallback {
        void onSuccess(List<TrojanConfig> configs);
        void onError(String message);
    }
    
    public interface DeleteCallback {
        void onSuccess();
        void onError(String message);
    }
} 