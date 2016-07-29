package com.wen.magi.baseframe.db;

import android.database.sqlite.SQLiteDatabase;

import com.wen.magi.baseframe.models.TestDaoModel;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cardOrderDaoConfig;

    private final CardOrderDao cardOrderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cardOrderDaoConfig = daoConfigMap.get(CardOrderDao.class).clone();
        cardOrderDaoConfig.initIdentityScope(type);

        cardOrderDao = new CardOrderDao(cardOrderDaoConfig, this);

        registerDao(TestDaoModel.class, cardOrderDao);
    }

    public void clear() {
        cardOrderDaoConfig.getIdentityScope().clear();
    }

//    public CardMerchantDao getCardMerchantDao() {

    public CardOrderDao getCardOrderDao() {
        return cardOrderDao;
    }
}
