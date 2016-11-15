package com.wen.magi.baseframe.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.wen.magi.baseframe.models.Consume;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONSUME".
*/
public class ConsumeDao extends AbstractDao<Consume, Long> {

    public static final String TABLENAME = "CONSUME";

    /**
     * Properties of entity Consume.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property ConsumeID = new Property(1, Long.class, "consumeID", false, "CONSUME_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Desc = new Property(3, String.class, "desc", false, "DESC");
        public final static Property ConsumeType = new Property(4, int.class, "consumeType", false, "CONSUME_TYPE");
        public final static Property ConsumeTitle = new Property(5, String.class, "consumeTitle", false, "CONSUME_TITLE");
        public final static Property ConsumeNum = new Property(6, Long.class, "consumeNum", false, "CONSUME_NUM");
        public final static Property ConsumeDate = new Property(7, java.util.Date.class, "consumeDate", false, "CONSUME_DATE");
        public final static Property DtStart = new Property(8, java.util.Date.class, "dtStart", false, "DT_START");
        public final static Property DtEnd = new Property(9, java.util.Date.class, "dtEnd", false, "DT_END");
        public final static Property Location = new Property(10, String.class, "location", false, "LOCATION");
        public final static Property Picture = new Property(11, String.class, "picture", false, "PICTURE");
        public final static Property RepeatType = new Property(12, String.class, "repeatType", false, "REPEAT_TYPE");
    }


    public ConsumeDao(DaoConfig config) {
        super(config);
    }
    
    public ConsumeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONSUME\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: _id
                "\"CONSUME_ID\" INTEGER," + // 1: consumeID
                "\"TITLE\" TEXT," + // 2: title
                "\"DESC\" TEXT," + // 3: desc
                "\"CONSUME_TYPE\" INTEGER NOT NULL ," + // 4: consumeType
                "\"CONSUME_TITLE\" TEXT," + // 5: consumeTitle
                "\"CONSUME_NUM\" INTEGER," + // 6: consumeNum
                "\"CONSUME_DATE\" INTEGER," + // 7: consumeDate
                "\"DT_START\" INTEGER," + // 8: dtStart
                "\"DT_END\" INTEGER," + // 9: dtEnd
                "\"LOCATION\" TEXT," + // 10: location
                "\"PICTURE\" TEXT," + // 11: picture
                "\"REPEAT_TYPE\" TEXT);"); // 12: repeatType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONSUME\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Consume entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long consumeID = entity.getConsumeID();
        if (consumeID != null) {
            stmt.bindLong(2, consumeID);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(4, desc);
        }
        stmt.bindLong(5, entity.getConsumeType());
 
        String consumeTitle = entity.getConsumeTitle();
        if (consumeTitle != null) {
            stmt.bindString(6, consumeTitle);
        }
 
        Long consumeNum = entity.getConsumeNum();
        if (consumeNum != null) {
            stmt.bindLong(7, consumeNum);
        }
 
        java.util.Date consumeDate = entity.getConsumeDate();
        if (consumeDate != null) {
            stmt.bindLong(8, consumeDate.getTime());
        }
 
        java.util.Date dtStart = entity.getDtStart();
        if (dtStart != null) {
            stmt.bindLong(9, dtStart.getTime());
        }
 
        java.util.Date dtEnd = entity.getDtEnd();
        if (dtEnd != null) {
            stmt.bindLong(10, dtEnd.getTime());
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(11, location);
        }
 
        String picture = entity.getPicture();
        if (picture != null) {
            stmt.bindString(12, picture);
        }
 
        String repeatType = entity.getRepeatType();
        if (repeatType != null) {
            stmt.bindString(13, repeatType);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Consume entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        Long consumeID = entity.getConsumeID();
        if (consumeID != null) {
            stmt.bindLong(2, consumeID);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(4, desc);
        }
        stmt.bindLong(5, entity.getConsumeType());
 
        String consumeTitle = entity.getConsumeTitle();
        if (consumeTitle != null) {
            stmt.bindString(6, consumeTitle);
        }
 
        Long consumeNum = entity.getConsumeNum();
        if (consumeNum != null) {
            stmt.bindLong(7, consumeNum);
        }
 
        java.util.Date consumeDate = entity.getConsumeDate();
        if (consumeDate != null) {
            stmt.bindLong(8, consumeDate.getTime());
        }
 
        java.util.Date dtStart = entity.getDtStart();
        if (dtStart != null) {
            stmt.bindLong(9, dtStart.getTime());
        }
 
        java.util.Date dtEnd = entity.getDtEnd();
        if (dtEnd != null) {
            stmt.bindLong(10, dtEnd.getTime());
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(11, location);
        }
 
        String picture = entity.getPicture();
        if (picture != null) {
            stmt.bindString(12, picture);
        }
 
        String repeatType = entity.getRepeatType();
        if (repeatType != null) {
            stmt.bindString(13, repeatType);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Consume readEntity(Cursor cursor, int offset) {
        Consume entity = new Consume( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // consumeID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // desc
            cursor.getInt(offset + 4), // consumeType
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // consumeTitle
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // consumeNum
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // consumeDate
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)), // dtStart
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // dtEnd
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // location
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // picture
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // repeatType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Consume entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setConsumeID(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDesc(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setConsumeType(cursor.getInt(offset + 4));
        entity.setConsumeTitle(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setConsumeNum(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setConsumeDate(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setDtStart(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
        entity.setDtEnd(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setLocation(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPicture(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRepeatType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Consume entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Consume entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Consume entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
