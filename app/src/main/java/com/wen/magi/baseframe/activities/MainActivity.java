package com.wen.magi.baseframe.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.views.BounceListView;
import com.wen.magi.baseframe.web.WebActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.wen.magi.baseframe.utils.Constants.ACTIVITY_WEB_KEY_INTENT_URL;

public class MainActivity extends BaseActivity {

    @From(R.id.main_tv)
    private TextView mainTv;

    @From(R.id.main_tv1)
    private TextView mainTv1;

    @From(R.id.list_view)
    private BounceListView listView;

    @From(R.id.stub_id1)
    private ViewStub viewStub;

    private View linearLayout;

    private HashMap<String, Objects> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTv.setOnClickListener(this);
        mainTv1.setOnClickListener(this);
        LogUtils.e("wwwwwwwwweeeee");
    }

    @Override
    protected void OnClickView(View v) {
        if (v == mainTv) {
            Intent intent = new Intent(this, DialogTestActivity.class);
//            startActivity(DialogTestActivity.class);
            if (SysUtils.nowSDKINTBigger(21)) {
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, mainTv, "shareNames").toBundle());
            } else
                startActivity(intent);

        } else if (v == mainTv1) {
//            viewStub.setVisibility(View.VISIBLE);
            if (linearLayout != null)
                return;
            linearLayout = viewStub.inflate();
            linearLayout.setBackgroundResource(R.color.red_btn_bg_color);
            linearLayout.setOnClickListener(this);
            viewStub.setBackgroundResource(R.color.blue_btn_bg_color);
        } else if (v == linearLayout) {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(ACTIVITY_WEB_KEY_INTENT_URL, "https://www.baidu.com");
            startActivity(intent);
        }
    }

    public static ArrayList<String> getDummyData(int num) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            items.add("Item " + i);
        }
        return items;
    }

}
