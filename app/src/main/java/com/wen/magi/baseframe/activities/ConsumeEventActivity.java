package com.wen.magi.baseframe.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SimpleAdapter;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.views.DraggableGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class ConsumeEventActivity extends BaseActivity {
    private List<HashMap<String, Object>> dataSourceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_event);
        setupGridView();
    }

    private void setupGridView() {
        DraggableGridView mDragGridView = (DraggableGridView) findViewById(R.id.consume_event_drag_view);
        for (int i = 0; i < 30; i++) {
            HashMap<String, Object> itemHashMap = new HashMap<>();
            itemHashMap.put("item_image",R.mipmap.ic_launcher);
            itemHashMap.put("item_text", "11111111" + Integer.toString(i));
            dataSourceList.add(itemHashMap);
        }


        final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, dataSourceList,
                R.layout.view_grid_item, new String[] { "item_image", "item_text" },
                new int[] { R.id.item_image, R.id.item_text });

        mDragGridView.setAdapter(mSimpleAdapter);

        mDragGridView.setOnChangeListener(new DraggableGridView.OnChangeListener() {

            @Override
            public void onChange(int from, int to) {
                HashMap<String, Object> temp = dataSourceList.get(from);
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(dataSourceList, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(dataSourceList, i, i-1);
                    }
                }

                dataSourceList.set(to, temp);

                mSimpleAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    protected void OnClickView(View v) {

    }
}
