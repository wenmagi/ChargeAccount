package com.wen.magi.baseframe.views.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.SysUtils;

import java.util.ArrayList;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class BottomDialog {

    public static void showBottomDialog(Context mContext, View view, final View.OnClickListener listener) {

        final AlertDialog dlg = new AlertDialog.Builder(mContext, R.style.dialog_bkg).create();
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        int screenWidth = SysUtils.WIDTH;
        if (screenWidth > 0) {
            dlg.getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Window window = dlg.getWindow();
        window.setContentView(view);
        window.setGravity(Gravity.BOTTOM);

        int childCount = ((ViewGroup) view).getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = ((ViewGroup) view).getChildAt(i);
            if (child != null && listener != null) {
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v);
                        dlg.dismiss();
                    }
                });
            }
        }
    }
}
