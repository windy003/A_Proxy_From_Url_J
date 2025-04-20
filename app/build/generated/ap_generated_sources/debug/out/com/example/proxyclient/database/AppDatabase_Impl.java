package com.example.proxyclient.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile XrayConfigDao _xrayConfigDao;

  private volatile TrojanConfigDao _trojanConfigDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `xray_configs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `protocol` TEXT, `serverAddress` TEXT, `serverPort` INTEGER NOT NULL, `uuid` TEXT, `alterId` TEXT, `security` TEXT, `network` TEXT, `localPort` INTEGER NOT NULL, `remark` TEXT, `lastConnected` INTEGER NOT NULL, `subscriptionUrl` TEXT, `path` TEXT, `host` TEXT, `tls` TEXT, `sni` TEXT, `alpn` TEXT, `enableUdp` INTEGER NOT NULL, `encryption` TEXT, `method` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `trojan_configs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `serverAddress` TEXT, `serverPort` INTEGER NOT NULL, `password` TEXT, `localPort` INTEGER NOT NULL, `verifyCert` INTEGER NOT NULL, `sni` TEXT, `enableUdp` INTEGER NOT NULL, `lastConnected` INTEGER NOT NULL, `remark` TEXT, `subscriptionUrl` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4cda6b909cdcc554eb8ff28f521b2f69')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `xray_configs`");
        db.execSQL("DROP TABLE IF EXISTS `trojan_configs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsXrayConfigs = new HashMap<String, TableInfo.Column>(21);
        _columnsXrayConfigs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("protocol", new TableInfo.Column("protocol", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("serverAddress", new TableInfo.Column("serverAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("serverPort", new TableInfo.Column("serverPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("uuid", new TableInfo.Column("uuid", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("alterId", new TableInfo.Column("alterId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("security", new TableInfo.Column("security", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("network", new TableInfo.Column("network", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("localPort", new TableInfo.Column("localPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("remark", new TableInfo.Column("remark", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("lastConnected", new TableInfo.Column("lastConnected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("subscriptionUrl", new TableInfo.Column("subscriptionUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("path", new TableInfo.Column("path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("host", new TableInfo.Column("host", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("tls", new TableInfo.Column("tls", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("sni", new TableInfo.Column("sni", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("alpn", new TableInfo.Column("alpn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("enableUdp", new TableInfo.Column("enableUdp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("encryption", new TableInfo.Column("encryption", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXrayConfigs.put("method", new TableInfo.Column("method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysXrayConfigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesXrayConfigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoXrayConfigs = new TableInfo("xray_configs", _columnsXrayConfigs, _foreignKeysXrayConfigs, _indicesXrayConfigs);
        final TableInfo _existingXrayConfigs = TableInfo.read(db, "xray_configs");
        if (!_infoXrayConfigs.equals(_existingXrayConfigs)) {
          return new RoomOpenHelper.ValidationResult(false, "xray_configs(com.example.proxyclient.model.XrayConfig).\n"
                  + " Expected:\n" + _infoXrayConfigs + "\n"
                  + " Found:\n" + _existingXrayConfigs);
        }
        final HashMap<String, TableInfo.Column> _columnsTrojanConfigs = new HashMap<String, TableInfo.Column>(12);
        _columnsTrojanConfigs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("serverAddress", new TableInfo.Column("serverAddress", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("serverPort", new TableInfo.Column("serverPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("localPort", new TableInfo.Column("localPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("verifyCert", new TableInfo.Column("verifyCert", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("sni", new TableInfo.Column("sni", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("enableUdp", new TableInfo.Column("enableUdp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("lastConnected", new TableInfo.Column("lastConnected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("remark", new TableInfo.Column("remark", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrojanConfigs.put("subscriptionUrl", new TableInfo.Column("subscriptionUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrojanConfigs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrojanConfigs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrojanConfigs = new TableInfo("trojan_configs", _columnsTrojanConfigs, _foreignKeysTrojanConfigs, _indicesTrojanConfigs);
        final TableInfo _existingTrojanConfigs = TableInfo.read(db, "trojan_configs");
        if (!_infoTrojanConfigs.equals(_existingTrojanConfigs)) {
          return new RoomOpenHelper.ValidationResult(false, "trojan_configs(com.example.proxyclient.model.TrojanConfig).\n"
                  + " Expected:\n" + _infoTrojanConfigs + "\n"
                  + " Found:\n" + _existingTrojanConfigs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4cda6b909cdcc554eb8ff28f521b2f69", "20c6d7eb1ff8e1e7a6d38ddf8166e74e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "xray_configs","trojan_configs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `xray_configs`");
      _db.execSQL("DELETE FROM `trojan_configs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(XrayConfigDao.class, XrayConfigDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrojanConfigDao.class, TrojanConfigDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public XrayConfigDao xrayConfigDao() {
    if (_xrayConfigDao != null) {
      return _xrayConfigDao;
    } else {
      synchronized(this) {
        if(_xrayConfigDao == null) {
          _xrayConfigDao = new XrayConfigDao_Impl(this);
        }
        return _xrayConfigDao;
      }
    }
  }

  @Override
  public TrojanConfigDao trojanConfigDao() {
    if (_trojanConfigDao != null) {
      return _trojanConfigDao;
    } else {
      synchronized(this) {
        if(_trojanConfigDao == null) {
          _trojanConfigDao = new TrojanConfigDao_Impl(this);
        }
        return _trojanConfigDao;
      }
    }
  }
}
