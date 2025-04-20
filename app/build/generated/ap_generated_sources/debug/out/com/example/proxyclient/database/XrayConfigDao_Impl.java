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
import com.example.proxyclient.model.XrayConfig;
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
public final class XrayConfigDao_Impl implements XrayConfigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<XrayConfig> __insertionAdapterOfXrayConfig;

  private final EntityDeletionOrUpdateAdapter<XrayConfig> __deletionAdapterOfXrayConfig;

  private final EntityDeletionOrUpdateAdapter<XrayConfig> __updateAdapterOfXrayConfig;

  private final SharedSQLiteStatement __preparedStmtOfDeleteConfigsBySubscription;

  public XrayConfigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfXrayConfig = new EntityInsertionAdapter<XrayConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `xray_configs` (`id`,`name`,`protocol`,`serverAddress`,`serverPort`,`uuid`,`alterId`,`security`,`network`,`localPort`,`remark`,`lastConnected`,`subscriptionUrl`,`path`,`host`,`tls`,`sni`,`alpn`,`enableUdp`,`encryption`,`method`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final XrayConfig entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getProtocol() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getProtocol());
        }
        if (entity.getServerAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getServerAddress());
        }
        statement.bindLong(5, entity.getServerPort());
        if (entity.getUuid() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUuid());
        }
        if (entity.getAlterId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAlterId());
        }
        if (entity.getSecurity() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSecurity());
        }
        if (entity.getNetwork() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNetwork());
        }
        statement.bindLong(10, entity.getLocalPort());
        if (entity.getRemark() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRemark());
        }
        statement.bindLong(12, entity.getLastConnected());
        if (entity.getSubscriptionUrl() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSubscriptionUrl());
        }
        if (entity.getPath() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPath());
        }
        if (entity.getHost() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getHost());
        }
        if (entity.getTls() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getTls());
        }
        if (entity.getSni() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getSni());
        }
        if (entity.getAlpn() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getAlpn());
        }
        final int _tmp = entity.isEnableUdp() ? 1 : 0;
        statement.bindLong(19, _tmp);
        if (entity.getEncryption() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getEncryption());
        }
        if (entity.getMethod() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getMethod());
        }
      }
    };
    this.__deletionAdapterOfXrayConfig = new EntityDeletionOrUpdateAdapter<XrayConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `xray_configs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final XrayConfig entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfXrayConfig = new EntityDeletionOrUpdateAdapter<XrayConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `xray_configs` SET `id` = ?,`name` = ?,`protocol` = ?,`serverAddress` = ?,`serverPort` = ?,`uuid` = ?,`alterId` = ?,`security` = ?,`network` = ?,`localPort` = ?,`remark` = ?,`lastConnected` = ?,`subscriptionUrl` = ?,`path` = ?,`host` = ?,`tls` = ?,`sni` = ?,`alpn` = ?,`enableUdp` = ?,`encryption` = ?,`method` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final XrayConfig entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getProtocol() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getProtocol());
        }
        if (entity.getServerAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getServerAddress());
        }
        statement.bindLong(5, entity.getServerPort());
        if (entity.getUuid() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getUuid());
        }
        if (entity.getAlterId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAlterId());
        }
        if (entity.getSecurity() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSecurity());
        }
        if (entity.getNetwork() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNetwork());
        }
        statement.bindLong(10, entity.getLocalPort());
        if (entity.getRemark() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRemark());
        }
        statement.bindLong(12, entity.getLastConnected());
        if (entity.getSubscriptionUrl() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSubscriptionUrl());
        }
        if (entity.getPath() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPath());
        }
        if (entity.getHost() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getHost());
        }
        if (entity.getTls() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getTls());
        }
        if (entity.getSni() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getSni());
        }
        if (entity.getAlpn() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getAlpn());
        }
        final int _tmp = entity.isEnableUdp() ? 1 : 0;
        statement.bindLong(19, _tmp);
        if (entity.getEncryption() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getEncryption());
        }
        if (entity.getMethod() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getMethod());
        }
        statement.bindLong(22, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteConfigsBySubscription = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM xray_configs WHERE subscriptionUrl = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final XrayConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfXrayConfig.insertAndReturnId(config);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final XrayConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfXrayConfig.handle(config);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final XrayConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfXrayConfig.handle(config);
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
  public LiveData<List<XrayConfig>> getAllConfigs() {
    final String _sql = "SELECT * FROM xray_configs ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"xray_configs"}, false, new Callable<List<XrayConfig>>() {
      @Override
      @Nullable
      public List<XrayConfig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
          final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
          final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
          final int _cursorIndexOfAlterId = CursorUtil.getColumnIndexOrThrow(_cursor, "alterId");
          final int _cursorIndexOfSecurity = CursorUtil.getColumnIndexOrThrow(_cursor, "security");
          final int _cursorIndexOfNetwork = CursorUtil.getColumnIndexOrThrow(_cursor, "network");
          final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
          final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
          final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
          final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
          final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
          final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
          final int _cursorIndexOfTls = CursorUtil.getColumnIndexOrThrow(_cursor, "tls");
          final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
          final int _cursorIndexOfAlpn = CursorUtil.getColumnIndexOrThrow(_cursor, "alpn");
          final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
          final int _cursorIndexOfEncryption = CursorUtil.getColumnIndexOrThrow(_cursor, "encryption");
          final int _cursorIndexOfMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "method");
          final List<XrayConfig> _result = new ArrayList<XrayConfig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XrayConfig _item;
            _item = new XrayConfig();
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
            final String _tmpProtocol;
            if (_cursor.isNull(_cursorIndexOfProtocol)) {
              _tmpProtocol = null;
            } else {
              _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            }
            _item.setProtocol(_tmpProtocol);
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
            final String _tmpUuid;
            if (_cursor.isNull(_cursorIndexOfUuid)) {
              _tmpUuid = null;
            } else {
              _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
            }
            _item.setUuid(_tmpUuid);
            final String _tmpAlterId;
            if (_cursor.isNull(_cursorIndexOfAlterId)) {
              _tmpAlterId = null;
            } else {
              _tmpAlterId = _cursor.getString(_cursorIndexOfAlterId);
            }
            _item.setAlterId(_tmpAlterId);
            final String _tmpSecurity;
            if (_cursor.isNull(_cursorIndexOfSecurity)) {
              _tmpSecurity = null;
            } else {
              _tmpSecurity = _cursor.getString(_cursorIndexOfSecurity);
            }
            _item.setSecurity(_tmpSecurity);
            final String _tmpNetwork;
            if (_cursor.isNull(_cursorIndexOfNetwork)) {
              _tmpNetwork = null;
            } else {
              _tmpNetwork = _cursor.getString(_cursorIndexOfNetwork);
            }
            _item.setNetwork(_tmpNetwork);
            final int _tmpLocalPort;
            _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
            _item.setLocalPort(_tmpLocalPort);
            final String _tmpRemark;
            if (_cursor.isNull(_cursorIndexOfRemark)) {
              _tmpRemark = null;
            } else {
              _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
            }
            _item.setRemark(_tmpRemark);
            final long _tmpLastConnected;
            _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
            _item.setLastConnected(_tmpLastConnected);
            final String _tmpSubscriptionUrl;
            if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
              _tmpSubscriptionUrl = null;
            } else {
              _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
            }
            _item.setSubscriptionUrl(_tmpSubscriptionUrl);
            final String _tmpPath;
            if (_cursor.isNull(_cursorIndexOfPath)) {
              _tmpPath = null;
            } else {
              _tmpPath = _cursor.getString(_cursorIndexOfPath);
            }
            _item.setPath(_tmpPath);
            final String _tmpHost;
            if (_cursor.isNull(_cursorIndexOfHost)) {
              _tmpHost = null;
            } else {
              _tmpHost = _cursor.getString(_cursorIndexOfHost);
            }
            _item.setHost(_tmpHost);
            final String _tmpTls;
            if (_cursor.isNull(_cursorIndexOfTls)) {
              _tmpTls = null;
            } else {
              _tmpTls = _cursor.getString(_cursorIndexOfTls);
            }
            _item.setTls(_tmpTls);
            final String _tmpSni;
            if (_cursor.isNull(_cursorIndexOfSni)) {
              _tmpSni = null;
            } else {
              _tmpSni = _cursor.getString(_cursorIndexOfSni);
            }
            _item.setSni(_tmpSni);
            final String _tmpAlpn;
            if (_cursor.isNull(_cursorIndexOfAlpn)) {
              _tmpAlpn = null;
            } else {
              _tmpAlpn = _cursor.getString(_cursorIndexOfAlpn);
            }
            _item.setAlpn(_tmpAlpn);
            final boolean _tmpEnableUdp;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnableUdp);
            _tmpEnableUdp = _tmp != 0;
            _item.setEnableUdp(_tmpEnableUdp);
            final String _tmpEncryption;
            if (_cursor.isNull(_cursorIndexOfEncryption)) {
              _tmpEncryption = null;
            } else {
              _tmpEncryption = _cursor.getString(_cursorIndexOfEncryption);
            }
            _item.setEncryption(_tmpEncryption);
            final String _tmpMethod;
            if (_cursor.isNull(_cursorIndexOfMethod)) {
              _tmpMethod = null;
            } else {
              _tmpMethod = _cursor.getString(_cursorIndexOfMethod);
            }
            _item.setMethod(_tmpMethod);
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
  public XrayConfig getConfigById(final long id) {
    final String _sql = "SELECT * FROM xray_configs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfAlterId = CursorUtil.getColumnIndexOrThrow(_cursor, "alterId");
      final int _cursorIndexOfSecurity = CursorUtil.getColumnIndexOrThrow(_cursor, "security");
      final int _cursorIndexOfNetwork = CursorUtil.getColumnIndexOrThrow(_cursor, "network");
      final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
      final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
      final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
      final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
      final int _cursorIndexOfTls = CursorUtil.getColumnIndexOrThrow(_cursor, "tls");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfAlpn = CursorUtil.getColumnIndexOrThrow(_cursor, "alpn");
      final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
      final int _cursorIndexOfEncryption = CursorUtil.getColumnIndexOrThrow(_cursor, "encryption");
      final int _cursorIndexOfMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "method");
      final XrayConfig _result;
      if (_cursor.moveToFirst()) {
        _result = new XrayConfig();
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
        final String _tmpProtocol;
        if (_cursor.isNull(_cursorIndexOfProtocol)) {
          _tmpProtocol = null;
        } else {
          _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
        }
        _result.setProtocol(_tmpProtocol);
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
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _result.setUuid(_tmpUuid);
        final String _tmpAlterId;
        if (_cursor.isNull(_cursorIndexOfAlterId)) {
          _tmpAlterId = null;
        } else {
          _tmpAlterId = _cursor.getString(_cursorIndexOfAlterId);
        }
        _result.setAlterId(_tmpAlterId);
        final String _tmpSecurity;
        if (_cursor.isNull(_cursorIndexOfSecurity)) {
          _tmpSecurity = null;
        } else {
          _tmpSecurity = _cursor.getString(_cursorIndexOfSecurity);
        }
        _result.setSecurity(_tmpSecurity);
        final String _tmpNetwork;
        if (_cursor.isNull(_cursorIndexOfNetwork)) {
          _tmpNetwork = null;
        } else {
          _tmpNetwork = _cursor.getString(_cursorIndexOfNetwork);
        }
        _result.setNetwork(_tmpNetwork);
        final int _tmpLocalPort;
        _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
        _result.setLocalPort(_tmpLocalPort);
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        _result.setRemark(_tmpRemark);
        final long _tmpLastConnected;
        _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
        _result.setLastConnected(_tmpLastConnected);
        final String _tmpSubscriptionUrl;
        if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
          _tmpSubscriptionUrl = null;
        } else {
          _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
        }
        _result.setSubscriptionUrl(_tmpSubscriptionUrl);
        final String _tmpPath;
        if (_cursor.isNull(_cursorIndexOfPath)) {
          _tmpPath = null;
        } else {
          _tmpPath = _cursor.getString(_cursorIndexOfPath);
        }
        _result.setPath(_tmpPath);
        final String _tmpHost;
        if (_cursor.isNull(_cursorIndexOfHost)) {
          _tmpHost = null;
        } else {
          _tmpHost = _cursor.getString(_cursorIndexOfHost);
        }
        _result.setHost(_tmpHost);
        final String _tmpTls;
        if (_cursor.isNull(_cursorIndexOfTls)) {
          _tmpTls = null;
        } else {
          _tmpTls = _cursor.getString(_cursorIndexOfTls);
        }
        _result.setTls(_tmpTls);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _result.setSni(_tmpSni);
        final String _tmpAlpn;
        if (_cursor.isNull(_cursorIndexOfAlpn)) {
          _tmpAlpn = null;
        } else {
          _tmpAlpn = _cursor.getString(_cursorIndexOfAlpn);
        }
        _result.setAlpn(_tmpAlpn);
        final boolean _tmpEnableUdp;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnableUdp);
        _tmpEnableUdp = _tmp != 0;
        _result.setEnableUdp(_tmpEnableUdp);
        final String _tmpEncryption;
        if (_cursor.isNull(_cursorIndexOfEncryption)) {
          _tmpEncryption = null;
        } else {
          _tmpEncryption = _cursor.getString(_cursorIndexOfEncryption);
        }
        _result.setEncryption(_tmpEncryption);
        final String _tmpMethod;
        if (_cursor.isNull(_cursorIndexOfMethod)) {
          _tmpMethod = null;
        } else {
          _tmpMethod = _cursor.getString(_cursorIndexOfMethod);
        }
        _result.setMethod(_tmpMethod);
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
  public List<XrayConfig> getConfigsBySubscription(final String url) {
    final String _sql = "SELECT * FROM xray_configs WHERE subscriptionUrl = ?";
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
      final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "uuid");
      final int _cursorIndexOfAlterId = CursorUtil.getColumnIndexOrThrow(_cursor, "alterId");
      final int _cursorIndexOfSecurity = CursorUtil.getColumnIndexOrThrow(_cursor, "security");
      final int _cursorIndexOfNetwork = CursorUtil.getColumnIndexOrThrow(_cursor, "network");
      final int _cursorIndexOfLocalPort = CursorUtil.getColumnIndexOrThrow(_cursor, "localPort");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "lastConnected");
      final int _cursorIndexOfSubscriptionUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriptionUrl");
      final int _cursorIndexOfPath = CursorUtil.getColumnIndexOrThrow(_cursor, "path");
      final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
      final int _cursorIndexOfTls = CursorUtil.getColumnIndexOrThrow(_cursor, "tls");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfAlpn = CursorUtil.getColumnIndexOrThrow(_cursor, "alpn");
      final int _cursorIndexOfEnableUdp = CursorUtil.getColumnIndexOrThrow(_cursor, "enableUdp");
      final int _cursorIndexOfEncryption = CursorUtil.getColumnIndexOrThrow(_cursor, "encryption");
      final int _cursorIndexOfMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "method");
      final List<XrayConfig> _result = new ArrayList<XrayConfig>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final XrayConfig _item;
        _item = new XrayConfig();
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
        final String _tmpProtocol;
        if (_cursor.isNull(_cursorIndexOfProtocol)) {
          _tmpProtocol = null;
        } else {
          _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
        }
        _item.setProtocol(_tmpProtocol);
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
        final String _tmpUuid;
        if (_cursor.isNull(_cursorIndexOfUuid)) {
          _tmpUuid = null;
        } else {
          _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        }
        _item.setUuid(_tmpUuid);
        final String _tmpAlterId;
        if (_cursor.isNull(_cursorIndexOfAlterId)) {
          _tmpAlterId = null;
        } else {
          _tmpAlterId = _cursor.getString(_cursorIndexOfAlterId);
        }
        _item.setAlterId(_tmpAlterId);
        final String _tmpSecurity;
        if (_cursor.isNull(_cursorIndexOfSecurity)) {
          _tmpSecurity = null;
        } else {
          _tmpSecurity = _cursor.getString(_cursorIndexOfSecurity);
        }
        _item.setSecurity(_tmpSecurity);
        final String _tmpNetwork;
        if (_cursor.isNull(_cursorIndexOfNetwork)) {
          _tmpNetwork = null;
        } else {
          _tmpNetwork = _cursor.getString(_cursorIndexOfNetwork);
        }
        _item.setNetwork(_tmpNetwork);
        final int _tmpLocalPort;
        _tmpLocalPort = _cursor.getInt(_cursorIndexOfLocalPort);
        _item.setLocalPort(_tmpLocalPort);
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        _item.setRemark(_tmpRemark);
        final long _tmpLastConnected;
        _tmpLastConnected = _cursor.getLong(_cursorIndexOfLastConnected);
        _item.setLastConnected(_tmpLastConnected);
        final String _tmpSubscriptionUrl;
        if (_cursor.isNull(_cursorIndexOfSubscriptionUrl)) {
          _tmpSubscriptionUrl = null;
        } else {
          _tmpSubscriptionUrl = _cursor.getString(_cursorIndexOfSubscriptionUrl);
        }
        _item.setSubscriptionUrl(_tmpSubscriptionUrl);
        final String _tmpPath;
        if (_cursor.isNull(_cursorIndexOfPath)) {
          _tmpPath = null;
        } else {
          _tmpPath = _cursor.getString(_cursorIndexOfPath);
        }
        _item.setPath(_tmpPath);
        final String _tmpHost;
        if (_cursor.isNull(_cursorIndexOfHost)) {
          _tmpHost = null;
        } else {
          _tmpHost = _cursor.getString(_cursorIndexOfHost);
        }
        _item.setHost(_tmpHost);
        final String _tmpTls;
        if (_cursor.isNull(_cursorIndexOfTls)) {
          _tmpTls = null;
        } else {
          _tmpTls = _cursor.getString(_cursorIndexOfTls);
        }
        _item.setTls(_tmpTls);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _item.setSni(_tmpSni);
        final String _tmpAlpn;
        if (_cursor.isNull(_cursorIndexOfAlpn)) {
          _tmpAlpn = null;
        } else {
          _tmpAlpn = _cursor.getString(_cursorIndexOfAlpn);
        }
        _item.setAlpn(_tmpAlpn);
        final boolean _tmpEnableUdp;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfEnableUdp);
        _tmpEnableUdp = _tmp != 0;
        _item.setEnableUdp(_tmpEnableUdp);
        final String _tmpEncryption;
        if (_cursor.isNull(_cursorIndexOfEncryption)) {
          _tmpEncryption = null;
        } else {
          _tmpEncryption = _cursor.getString(_cursorIndexOfEncryption);
        }
        _item.setEncryption(_tmpEncryption);
        final String _tmpMethod;
        if (_cursor.isNull(_cursorIndexOfMethod)) {
          _tmpMethod = null;
        } else {
          _tmpMethod = _cursor.getString(_cursorIndexOfMethod);
        }
        _item.setMethod(_tmpMethod);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getConfigCount() {
    final String _sql = "SELECT COUNT(*) FROM xray_configs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
