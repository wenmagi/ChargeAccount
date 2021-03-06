package com.wen.magi.baseframe.db;

import android.content.Context;

import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.managers.RequestQueueManager;
import com.wen.magi.baseframe.models.AppUser;
import com.wen.magi.baseframe.models.Income;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class DBManager {

    private static final String DB_NAME = "note_heart_%s_greendao.db";
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static DBManager dbManager;

    private DBManager() {
        initDB(AppManager.getApplicationContext());
    }

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
            String dbName = StringUtils.format(DB_NAME, name);
            DaoMaster.OpenHelper helper =
                    new DaoMaster.DevOpenHelper(context, StringUtils.md5(dbName),
                            null);
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null)
            synchronized (DBManager.class) {
                if (dbManager == null)
                    dbManager = new DBManager();
            }
        return dbManager;
    }

    public void clear() {


        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        daoMaster = null;
    }

//    public Income loadByIncomeID(long id){
//    }
    /**********************
     * CardOrder Func
     ********************************/
    public Income loadIncome(long id) {

        if (daoSession == null)
            return null;
        return daoSession.getIncomeDao().load(id);
    }

    /**
     * 根据IncomeId查询
     *
     * @param id 根据IncomeId查询
     * @return Income
     */
    public Income loadIncomeByIncomeId(long id) {
        List<Income> list =
                queryIncome(IncomeDao.Properties.IncomeID.columnName + " = ?", String.valueOf(id));
        return LangUtils.getFirstObj((ArrayList<? extends Income>) list);
    }

    public List<Income> loadIncome() {
        return daoSession.getIncomeDao().loadAll();
    }

    /**
     * query list with where clause ex: begin_date_time >= ? AND end_date_time <= ?
     *
     * @param where  where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<Income> queryIncome(String where, String... params) {

        if (daoSession == null)
            return null;
        return daoSession.getIncomeDao().queryRaw("where " + where, params);
    }


    /**
     * insert or update note
     *
     * @param income
     * @return insert or update note id
     */
    public long saveIncome(Income income) {
        if (daoSession == null || income == null)
            return 0l;
        return daoSession.getIncomeDao().insertOrReplace(income);
    }


    /**
     * insert or update noteList use transaction
     *
     * @param list
     */
    public void saveIncomeLists(final List<Income> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        if (daoSession == null)
            return;
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    Income order = list.get(i);
                    saveIncome(order);
                }
            }
        });

    }

    /**
     * delete all note
     */
    public void deleteAllIncomes() {
        daoSession.getIncomeDao().deleteAll();
    }

    /**
     * delete note by id
     *
     * @param id
     */
    public void deleteIncome(long id) {
        daoSession.getIncomeDao().deleteByKey(id);
    }

    public void deleteIncome(Income income) {
        daoSession.getIncomeDao().delete(income);
    }

//    public TestDaoModel getValidOrderWithClassID(String classID) {
//        if (LangUtils.isEmpty(classID))
//            return null;
//        String selection = CardOrderDao.Properties.ClassID.columnName + " = ? "/*
//                                                                            * and
//                                                                            * " + CardOrderDBTable.ROW_STATUS + "
//                                                                            * <> ?"
//                                                                            */;
//        String[] selectionArgs = {classID};
//        List<TestDaoModel> list = queryCardOrder(selection, selectionArgs);
//        if (!LangUtils.isEmpty(list)) {
//            // return list.get(0);
//            TestDaoModel validTestDaoModel = list.get(0);
//            if (list.size() > 1) {
//                for (TestDaoModel testDaoModel : list) {
//                    if (testDaoModel.getOrderStatus() != TestDaoModel.OrderStatus.OrderStatusCancelled) {
//                        validTestDaoModel = testDaoModel;
//                        break;
//                    }
//                }
//            }
//            return validTestDaoModel;
//        }
//        return null;
//    }

//    public List<TestDaoModel> getGoingOrderListByTime(long time, int pageCount, int page, int orderType) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(CardOrderDao.Properties.EndTime.columnName);
//        sb.append(" >= ");
//        sb.append(time);
//        sb.append(" and ");
//        sb.append(CardOrderDao.Properties.OrderType.columnName);
//        sb.append(" = ");
//        sb.append(orderType);
//        sb.append(" and ");
//        sb.append(/*CardOrderDBTable.ROW_STATUS*/CardOrderDao.Properties.OrderStatus.columnName);
//        sb.append(" <> 2");
//        sb.append(" ORDER BY ");
//        sb.append(CardOrderDao.Properties.StartTime.columnName);
//        sb.append(", ");
//        sb.append(CardOrderDao.Properties.OrderStatus.columnName);
//        sb.append(" ASC ");
//        sb.append(" limit ");
//        sb.append(pageCount);
//        sb.append(" offset ");
//        sb.append((page - 1) * pageCount);
//
//        List<TestDaoModel> list = queryCardOrder(sb.toString());
//        return list;
//    }

//    public List<TestDaoModel> getFinishedOrderListByTime(long time, int pageCount, int page, int orderType) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(CardOrderDao.Properties.EndTime.columnName);
//        sb.append(" < ");
//        sb.append(time);
//        sb.append(" and ");
//        sb.append(CardOrderDao.Properties.OrderType.columnName);
//        sb.append(" = ");
//        sb.append(orderType);
//        sb.append(" and ");
//        sb.append(/*CardOrderDBTable.ROW_STATUS*/CardOrderDao.Properties.OrderStatus.columnName);
//        sb.append(" <> 2");
//        sb.append(" ORDER BY ");
//
//        sb.append(CardOrderDao.Properties.StartTime.columnName);
//        sb.append(" DESC ");
//        sb.append(", ");
//        sb.append(CardOrderDao.Properties.OrderStatus.columnName);
//        sb.append(" ASC ");
//        sb.append(" limit ");
//        sb.append(pageCount);
//        sb.append(" offset ");
//        sb.append((page - 1) * pageCount);
//
//        List<TestDaoModel> list = queryCardOrder(sb.toString());
//        return list;
//    }
}
