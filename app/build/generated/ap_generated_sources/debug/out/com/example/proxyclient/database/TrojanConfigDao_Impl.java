package com.example.proxyclient.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.proxyclient.model.TrojanConfig;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TrojanConfigDao_Impl implements TrojanConfigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrojanConfig> __insertionAdapterOfTrojanConfig;

  private final EntityDeletionOrUpdateAdapter<TrojanConfig> __deletionAdapterOfTrojanConfig;

  private final EntityDeletionOrUpdateAdapter<TrojanConfig> __updateAdapterOfTrojanConfig;

  private final SharedSQLiteStatement __preparedStmtOfDeleteConfigsBySubscription;

  public TrojanConfigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrojanConfig = new EntityInsertionAdapter<TrojanConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trojan_configs` (`id`,`name`,`serverAddress`,`serverPort`,`password`,`localPort`,`verifyCert`,`sni`,`enableUdp`,`lastConnected`,`remark`,`subscriptionUrl`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TrojanConfig entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getServerAddress() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getServerAddress());
        }
        statement.bindLong(4, entity.getServerPort());
        if (entity.getPassword() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPassword());
        }
        statement.bindLong(6, entity.getLocalPort());
        final int _tmp = entity.isVerifyCert() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getSni() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSni());
        }
        final int _tmp_1 = entity.isEnableUdp() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getLastConnected());
        if (entity.getRemark() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRemark());
        }
        if (entity.getSubscriptionUrl() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getSubscriptionUrl());
        }
      }
    };
    this.__deletionAdapterOfTrojanConfig = new EntityDeletionOrUpdateAdapter<TrojanConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `trojan_configs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TrojanConfig entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTrojanConfig = new EntityDeletionOrUpdateAdapter<TrojanConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `trojan_configs` SET `id` = ?,`name` = ?,`serverAddress` = ?,`serverPort` = ?,`password` = ?,`localPort` = ?,`verifyCert` = ?,`sni` = ?,`enableUdp` = ?,`lastConnected` = ?,`remark` = ?,`subscriptionUrl` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TrojanConfig entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getServerAddress() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getServerAddress());
        }
        statement.bindLong(4, entity.getServerPort());
        if (entity.getPassword() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPassword());
        }
        statement.bindLong(6, entity.getLocalPort());
        final int _tmp = entity.isVerifyCert() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getSni() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSni());
        }
        final int _tmp_1 = entity.isEnableUdp() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getLastConnected());
        if (entity.getRemark() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRemark());
        }
        if (entity.getSubscriptionUrl() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getSubscriptionUrl());
        }
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteConfigsBySubscription = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trojan_configs WHERE subscriptionUrl = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final TrojanConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfTrojanConfig.insertAndReturnId(config);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final TrojanConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTrojanConfig.handle(config);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TrojanConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTrojanConfig.handle(config);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteConfigsBySubscription(final String url) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteConfigsBySubscription.acquire();
    int _argIndex = 1;
    if (url == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, url);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteConfigsBySubscription.release(_stmt);
    }
  }

  @Override
  public LiveData<List<TrojanConfig>> getAllConfigs() {
    final String _sql = "SELECT * FROM trojan_configs ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"trojan_configs"}, false, new Callable<List<TrojanConfig>>() {
      @Override
      @Nullable
      public List<TrojanConfig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
          final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
          final int _cursorIndexOfVerifyCert = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyCert");
          final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
          final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
          final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
          final List<TrojanConfig> _result = new ArrayList<TrojanConfig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrojanConfig _item;
            _item = new TrojanConfig();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            _item.setName(_tmpName);
            final String _tmpServerAddress;
            if (_cursor.isNull(_cursorIndexOfServerAddress)) {
              _tmpServerAddress = null;
            } else {
              _tmpServerAddress = _cursor.getString(_cursorIndexOfServerAddress);
            }
            _item.setServerAddress(_tmpServerAddress);
            final int _tmpServerPort;
            _tmpServerPort = _cursor.getInt(_cursorIndexOfServerPort);
            _item.setServerPort(_tmpServerPort);
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            _item.setPassword(_tmpPassword);
            final int _tmpLocalPort;
            _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
            _item.setLocalPort(_tmpLocalPort);
            final boolean _tmpVerifyCert;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfVerifyCert);
            _tmpVerifyCert = _tmp != 0;
            _item.setVerifyCert(_tmpVerifyCert);
            final String _tmpSni;
            if (_cursor.isNull(_cursorIndexOfSni)) {
              _tmpSni = null;
            } else {
              _tmpSni = _cursor.getString(_cursorIndexOfSni);
            }
            _item.setSni(_tmpSni);
            final boolean _tmpEnableUdp;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfEnableUdp);
            _tmpEnableUdp = _tmp_1 != 0;
            _item.setEnableUdp(_tmpEnableUdp);
            final long _tmpLastConnected;
            _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
            _item.setLastConnected(_tmpLastConnected);
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            _item.setRemark(_tmpRemark);
            final String _tmpSubscriptionUrl;
            if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
              _tmpSubscriptionUrl = null;
            } else {
              _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
            }
            _item.setSubscriptionUrl(_tmpSubscriptionUrl);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public TrojanConfig getConfigById(final long id) {
    final String _sql = "SELECT * FROM trojan_configs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
      final int _cursorIndexOfVerifyCert = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyCert");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
      final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
      final TrojanConfig _result;
      if (_cursor.moveToFirst()) {
        _result = new TrojanConfig();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result.setName(_tmpName);
        final String _tmpServerAddress;
        if (_cursor.isNull(_cursorIndexOfServerAddress)) {
          _tmpServerAddress = null;
        } else {
          _tmpServerAddress = _cursor.getString(_cursorIndexOfServerAddress);
        }
        _result.setServerAddress(_tmpServerAddress);
        final int _tmpServerPort;
        _tmpServerPort = _cursor.getInt(_cursorIndexOfServerPort);
        _result.setServerPort(_tmpServerPort);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _result.setPassword(_tmpPassword);
        final int _tmpLocalPort;
        _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
        _result.setLocalPort(_tmpLocalPort);
        final boolean _tmpVerifyCert;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfVerifyCert);
        _tmpVerifyCert = _tmp != 0;
        _result.setVerifyCert(_tmpVerifyCert);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _result.setSni(_tmpSni);
        final boolean _tmpEnableUdp;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfEnableUdp);
        _tmpEnableUdp = _tmp_1 != 0;
        _result.setEnableUdp(_tmpEnableUdp);
        final long _tmpLastConnected;
        _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
        _result.setLastConnected(_tmpLastConnected);
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        _result.setRemark(_tmpRemark);
        final String _tmpSubscriptionUrl;
        if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
          _tmpSubscriptionUrl = null;
        } else {
          _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
        }
        _result.setSubscriptionUrl(_tmpSubscriptionUrl);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TrojanConfig> getConfigsBySubscription(final String url) {
    final String _sql = "SELECT * FROM trojan_configs WHERE subscriptionUrl = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (url == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, url);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
      final int _cursorIndexOfVerifyCert = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyCert");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
      final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
      final List<TrojanConfig> _result = new ArrayList<TrojanConfig>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TrojanConfig _item;
        _item = new TrojanConfig();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item.setName(_tmpName);
        final String _tmpServerAddress;
        if (_cursor.isNull(_cursorIndexOfServerAddress)) {
          _tmpServerAddress = null;
        } else {
          _tmpServerAddress = _cursor.getString(_cursorIndexOfServerAddress);
        }
        _item.setServerAddress(_tmpServerAddress);
        final int _tmpServerPort;
        _tmpServerPort = _cursor.getInt(_cursorIndexOfServerPort);
        _item.setServerPort(_tmpServerPort);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        _item.setPassword(_tmpPassword);
        final int _tmpLocalPort;
        _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
        _item.setLocalPort(_tmpLocalPort);
        final boolean _tmpVerifyCert;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfVerifyCert);
        _tmpVerifyCert = _tmp != 0;
        _item.setVerifyCert(_tmpVerifyCert);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _item.setSni(_tmpSni);
        final boolean _tmpEnableUdp;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfEnableUdp);
        _tmpEnableUdp = _tmp_1 != 0;
        _item.setEnableUdp(_tmpEnableUdp);
        final long _tmpLastConnected;
        _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
        _item.setLastConnected(_tmpLastConnected);
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        _item.setRemark(_tmpRemark);
        final String _tmpSubscriptionUrl;
        if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
          _tmpSubscriptionUrl = null;
        } else {
          _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
        }
        _item.setSubscriptionUrl(_tmpSubscriptionUrl);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
