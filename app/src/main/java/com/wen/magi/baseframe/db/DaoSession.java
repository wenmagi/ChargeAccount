package com.wen.magi.baseframe.db;


import com.wen.magi.baseframe.models.TestDaoModel;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;


/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cardOrderDaoConfig;

    private final CardOrderDao cardOrderDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
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

    public CardOrderDao getCardOrderDao() {
        return cardOrderDao;
    }
}
