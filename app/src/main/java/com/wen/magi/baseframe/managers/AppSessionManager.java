package com.wen.magi.baseframe.managers;

import com.wen.magi.baseframe.eventbus.LoginEvent;
import com.wen.magi.baseframe.eventbus.LogoutEvent;
import com.wen.magi.baseframe.eventbus.NetConnChangeEvent;
import com.wen.magi.baseframe.eventbus.NetTypeChangeEvent;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MVEN on 16/6/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class AppSessionManager {
    public enum SessionMode {
        OnLine, OffLine
    }

    public enum SessionStatus {
        Login, Logout
    }

    public enum NetWorkType {
        NETWORK_TYPE_WIFI,
        NETWORK_TYPE_WAP,
        NETWORK_TYPE_NET,
        NETWORK_TYPE_NULL
    }

    private static AppSessionManager mManager;
    private SessionMode sessionMode = SessionMode.OnLine;
    private SessionStatus sessionStatus = SessionStatus.Logout;
    private NetWorkType netWorkType = NetWorkType.NETWORK_TYPE_NET;

    public static AppSessionManager getSessionManager() {

        if (mManager == null) {
            mManager = new AppSessionManager();
        }

        return mManager;
    }

    /**
     * SessionMode改变，发送通知
     *
     * @param sessionMode
     */
    public void setSessionMode(SessionMode sessionMode) {
        if (this.sessionMode == sessionMode)
            return;

        this.sessionMode = sessionMode;
        NetConnChangeEvent event = new NetConnChangeEvent();
        event.setOnline(isOnline());
        EventBus.getDefault().post(event);
    }

    public SessionMode getSessionMode() {
        return sessionMode;
    }

    /**
     * 登陆或者登出时，发送通知
     *
     * @param sessionStatus
     */
    public void setSessionStatus(SessionStatus sessionStatus) {
        if (this.sessionStatus == sessionStatus)
            return;
        this.sessionStatus = sessionStatus;
        if (isLogin())
            EventBus.getDefault().post(new LoginEvent());
        else
            EventBus.getDefault().post(new LogoutEvent());
    }

    public boolean isLogin() {
        return sessionStatus == SessionStatus.Login;
    }

    public boolean isOnline() {
        return sessionMode == SessionMode.OnLine;
    }

    /**
     * NetWorkType改变，发送通知
     *
     * @param netWorkType
     */
    public void setNetWorkType(NetWorkType netWorkType) {
        if (this.netWorkType == netWorkType)
            return;

        this.netWorkType = netWorkType;
        NetTypeChangeEvent event = new NetTypeChangeEvent();
        event.setNetWorkType(netWorkType);
        EventBus.getDefault().post(event);
    }

    public NetWorkType getNetWorkType() {
        return netWorkType;
    }
}
