package com.wen.magi.baseframe.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MVEN on 16/8/22.
 * <p/>
 * email: magiwen@126.com.
 */


public class IncomeDao {

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'INCOME_EVENT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'INCOME_ID' INTEGER UNIQUE ," + // 1: incomeID
                "'DEVICE_ID' INTEGER," + // 2: deviceID
                "'INCOME_TYPE' INTEGER," + // 3: classType
                "'CO_TYPE' INTEGER," + // 4: coType
                "'ORDER_STATUS' INTEGER," + // 5: orderStatus
                "'ORDER_CODE' TEXT," + // 6: orderCode
                "'CLASS_ID' TEXT," + // 7: classID
                "'CLASS_NAME' TEXT," + // 8: className
                "'MERCHANT_NAME' TEXT," + // 9: merchantName
                "'GROUP_PURCHASE_NAME' TEXT," + // 10: groupPurchaseName
                "'MERCHANT_LOCATION' TEXT," + // 11: merchantLocation
                "'UPDATE_TIME' INTEGER," + // 12: updateTime
                "'START_TIME' INTEGER," + // 13: startTime
                "'END_TIME' INTEGER," + // 14: endTime
                "'NOTICE' TEXT," + // 15: notice
                "'INVITE_MSG_ID' INTEGER," + // 16: inviteMsgID
                "'PUNCH_MSG_ID' INTEGER," + // 17: punchMsgID
                "'CARD_ORDER_TYPE' INTEGER);");// 18: CardOrder
    }
}
