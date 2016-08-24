package com.wen.magi.baseframe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wen.magi.baseframe.utils.LogUtils;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class DaoMaster extends AbstractDaoMaster {

    public static final int SCHEMA_VERSION = 1;

    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        CardOrderDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        CardOrderDao.dropTable(db, ifExists);
    }

    public static abstract class OpenHelper extends DatabaseOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createAllTables(db, false);
        }
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtils.d("#### greenDAO Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            if (oldVersion == 1 && newVersion == 2) {
                try {

                    CardOrderDao.dropTable(db, true);
                    CardOrderDao.createTable(db, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    dropAllTables(db, true);
                    onCreate(db);
                }
            } else {
                dropAllTables(db, true);
                onCreate(db);
            }
        }
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(CardOrderDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    @Override
    public AbstractDaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

}
