package com.example.proxyclient.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.proxyclient.model.TrojanConfig;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TrojanConfigDao_Impl implements TrojanConfigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrojanConfig> __insertionAdapterOfTrojanConfig;

  public TrojanConfigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrojanConfig = new EntityInsertionAdapter<TrojanConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trojan_config` (`id`,`serverAddress`,`serverPort`,`password`,`remark`,`region`,`peer`,`sni`,`allowInsecure`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TrojanConfig entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getServerAddress() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerAddress());
        }
        statement.bindLong(3, entity.getServerPort());
        if (entity.getPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPassword());
        }
        if (entity.getRemark() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRemark());
        }
        if (entity.getRegion() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getRegion());
        }
        if (entity.getPeer() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPeer());
        }
        if (entity.getSni() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSni());
        }
        final int _tmp = entity.isAllowInsecure() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
  }

  @Override
  public void insertConfig(final TrojanConfig config) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTrojanConfig.insert(config);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<TrojanConfig> getAllConfigs() {
    final String _sql = "SELECT * FROM trojan_config";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfPeer = CursorUtil.getColumnIndexOrThrow(_cursor, "peer");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfAllowInsecure = CursorUtil.getColumnIndexOrThrow(_cursor, "allowInsecure");
      final List<TrojanConfig> _result = new ArrayList<TrojanConfig>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TrojanConfig _item;
        final String _tmpServerAddress;
        if (_cursor.isNull(_cursorIndexOfServerAddress)) {
          _tmpServerAddress = null;
        } else {
          _tmpServerAddress = _cursor.getString(_cursorIndexOfServerAddress);
        }
        final int _tmpServerPort;
        _tmpServerPort = _cursor.getInt(_cursorIndexOfServerPort);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        final String _tmpRegion;
        if (_cursor.isNull(_cursorIndexOfRegion)) {
          _tmpRegion = null;
        } else {
          _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
        }
        _item = new TrojanConfig(_tmpServerAddress,_tmpServerPort,_tmpPassword,_tmpRemark,_tmpRegion);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpPeer;
        if (_cursor.isNull(_cursorIndexOfPeer)) {
          _tmpPeer = null;
        } else {
          _tmpPeer = _cursor.getString(_cursorIndexOfPeer);
        }
        _item.setPeer(_tmpPeer);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _item.setSni(_tmpSni);
        final boolean _tmpAllowInsecure;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAllowInsecure);
        _tmpAllowInsecure = _tmp != 0;
        _item.setAllowInsecure(_tmpAllowInsecure);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TrojanConfig findConfig(final String address, final int port) {
    final String _sql = "SELECT * FROM trojan_config WHERE serverAddress = ? AND serverPort = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (address == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, address);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, port);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfServerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "serverAddress");
      final int _cursorIndexOfServerPort = CursorUtil.getColumnIndexOrThrow(_cursor, "serverPort");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfRemark = CursorUtil.getColumnIndexOrThrow(_cursor, "remark");
      final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
      final int _cursorIndexOfPeer = CursorUtil.getColumnIndexOrThrow(_cursor, "peer");
      final int _cursorIndexOfSni = CursorUtil.getColumnIndexOrThrow(_cursor, "sni");
      final int _cursorIndexOfAllowInsecure = CursorUtil.getColumnIndexOrThrow(_cursor, "allowInsecure");
      final TrojanConfig _result;
      if (_cursor.moveToFirst()) {
        final String _tmpServerAddress;
        if (_cursor.isNull(_cursorIndexOfServerAddress)) {
          _tmpServerAddress = null;
        } else {
          _tmpServerAddress = _cursor.getString(_cursorIndexOfServerAddress);
        }
        final int _tmpServerPort;
        _tmpServerPort = _cursor.getInt(_cursorIndexOfServerPort);
        final String _tmpPassword;
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _tmpPassword = null;
        } else {
          _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        }
        final String _tmpRemark;
        if (_cursor.isNull(_cursorIndexOfRemark)) {
          _tmpRemark = null;
        } else {
          _tmpRemark = _cursor.getString(_cursorIndexOfRemark);
        }
        final String _tmpRegion;
        if (_cursor.isNull(_cursorIndexOfRegion)) {
          _tmpRegion = null;
        } else {
          _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
        }
        _result = new TrojanConfig(_tmpServerAddress,_tmpServerPort,_tmpPassword,_tmpRemark,_tmpRegion);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpPeer;
        if (_cursor.isNull(_cursorIndexOfPeer)) {
          _tmpPeer = null;
        } else {
          _tmpPeer = _cursor.getString(_cursorIndexOfPeer);
        }
        _result.setPeer(_tmpPeer);
        final String _tmpSni;
        if (_cursor.isNull(_cursorIndexOfSni)) {
          _tmpSni = null;
        } else {
          _tmpSni = _cursor.getString(_cursorIndexOfSni);
        }
        _result.setSni(_tmpSni);
        final boolean _tmpAllowInsecure;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAllowInsecure);
        _tmpAllowInsecure = _tmp != 0;
        _result.setAllowInsecure(_tmpAllowInsecure);
      } else {
        _result = null;
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
