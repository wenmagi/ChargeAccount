package com.wen.magi.baseframe.db;

import android.content.Context;

import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.managers.RequestQueueManager;
import com.wen.magi.baseframe.models.AppUser;
import com.wen.magi.baseframe.models.TestDaoModel;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.StringUtils;

import java.util.List;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class DBManager {


    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public void initDB(Context context) {
        if (daoMaster == null) {
            String name;
            AppUser user = AppManager.getAppUser();
            if (user == null) {
                String tokenPhone = RequestQueueManager.getTokenPhone();
                name = LangUtils.isNotEmpty(tokenPhone) ? tokenPhone : "default";
            } else {
                name = user.phoneNum;
            }
            String dbName = StringUtils.format("%s_%s_greendao.db", "papaya_card", name);
            DaoMaster.OpenHelper helper =
                    new DaoMaster.DevOpenHelper(context, StringUtils.md5(dbName),
                            null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
    }

    public void clear() {


        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        daoMaster = null;
    }

    /**********************
     * CardOrder Func
     ********************************/
    public TestDaoModel loadOrder(long id) {

        if (daoSession == null)
            return null;
        return daoSession.getCardOrderDao().load(id);
    }

//    public CardOrder loadOrderByOrderId(long id) {
//        List<CardOrder> list =
//                queryCardOrder(CardOrderDao.Properties.OrderID.columnName + " = ?", String.valueOf(id));
//        return LangUtils.getFirstObj(list);
//    }

    public List<TestDaoModel> loadOrder() {
        return daoSession.getCardOrderDao().loadAll();
    }

    /**
     * query list with where clause ex: begin_date_time >= ? AND end_date_time <= ?
     *
     * @param where  where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<TestDaoModel> queryCardOrder(String where, String... params) {

        if (daoSession == null)
            return null;
        return daoSession.getCardOrderDao().queryRaw("where " + where, params);
    }


    /**
     * insert or update note
     *
     * @param testDaoModel
     * @return insert or update note id
     */
    public long saveOrder(TestDaoModel testDaoModel) {
        if (daoSession == null)
            return 0l;
        return daoSession.getCardOrderDao().insertOrReplace(testDaoModel);
    }


    /**
     * insert or update noteList use transaction
     *
     * @param list
     */
    public void saveOrderLists(final List<TestDaoModel> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        if (daoSession == null)
            return;
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    TestDaoModel order = list.get(i);
                    saveOrder(order);
                }
            }
        });

    }

    /**
     * delete all note
     */
    public void deleteAllOrders() {
        daoSession.getCardOrderDao().deleteAll();
    }

    /**
     * delete note by id
     *
     * @param id
     */
    public void deleteOrder(long id) {
        daoSession.getCardOrderDao().deleteByKey(id);
        // Log.i(TAG, "delete");
    }

    public void deleteOrder(TestDaoModel Order) {
        daoSession.getCardOrderDao().delete(Order);
    }

    public TestDaoModel getValidOrderWithClassID(String classID) {
        if (LangUtils.isEmpty(classID))
            return null;
        String selection = CardOrderDao.Properties.ClassID.columnName + " = ? "/*
                                                                            * and
                                                                            * " + CardOrderDBTable.ROW_STATUS + "
                                                                            * <> ?"
                                                                            */;
        String[] selectionArgs = {classID};
        List<TestDaoModel> list = queryCardOrder(selection, selectionArgs);
        if (!LangUtils.isEmpty(list)) {
            // return list.get(0);
            TestDaoModel validTestDaoModel = list.get(0);
            if (list.size() > 1) {
                for (TestDaoModel testDaoModel : list) {
                    if (testDaoModel.getOrderStatus() != TestDaoModel.OrderStatus.OrderStatusCancelled) {
                        validTestDaoModel = testDaoModel;
                        break;
                    }
                }
            }
            return validTestDaoModel;
        }
        return null;
    }

    public List<TestDaoModel> getGoingOrderListByTime(long time, int pageCount, int page, int orderType) {
        StringBuilder sb = new StringBuilder();
        sb.append(CardOrderDao.Properties.EndTime.columnName);
        sb.append(" >= ");
        sb.append(time);
        sb.append(" and ");
        sb.append(CardOrderDao.Properties.OrderType.columnName);
        sb.append(" = ");
        sb.append(orderType);
        sb.append(" and ");
        sb.append(/*CardOrderDBTable.ROW_STATUS*/CardOrderDao.Properties.OrderStatus.columnName);
        sb.append(" <> 2");
        sb.append(" ORDER BY ");
        sb.append(CardOrderDao.Properties.StartTime.columnName);
        sb.append(", ");
        sb.append(CardOrderDao.Properties.OrderStatus.columnName);
        sb.append(" ASC ");
        sb.append(" limit ");
        sb.append(pageCount);
        sb.append(" offset ");
        sb.append((page - 1) * pageCount);

        List<TestDaoModel> list = queryCardOrder(sb.toString());
        return list;
    }

    public List<TestDaoModel> getFinishedOrderListByTime(long time, int pageCount, int page, int orderType) {
        StringBuilder sb = new StringBuilder();
        sb.append(CardOrderDao.Properties.EndTime.columnName);
        sb.append(" < ");
        sb.append(time);
        sb.append(" and ");
        sb.append(CardOrderDao.Properties.OrderType.columnName);
        sb.append(" = ");
        sb.append(orderType);
        sb.append(" and ");
        sb.append(/*CardOrderDBTable.ROW_STATUS*/CardOrderDao.Properties.OrderStatus.columnName);
        sb.append(" <> 2");
        sb.append(" ORDER BY ");

        sb.append(CardOrderDao.Properties.StartTime.columnName);
        sb.append(" DESC ");
        sb.append(", ");
        sb.append(CardOrderDao.Properties.OrderStatus.columnName);
        sb.append(" ASC ");
        sb.append(" limit ");
        sb.append(pageCount);
        sb.append(" offset ");
        sb.append((page - 1) * pageCount);

        List<TestDaoModel> list = queryCardOrder(sb.toString());
        return list;
    }
}
