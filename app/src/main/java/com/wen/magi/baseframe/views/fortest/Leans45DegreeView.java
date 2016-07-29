package com.wen.magi.baseframe.views.fortest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.SysUtils;

/**
 * Created by MVEN on 16/7/25.
 * <p/>
 * email: magiwen@126.com.
 */

/**
 * 斜切View
 */
public class Leans45DegreeView extends View {
    private int height;
    private Paint paint;
    private double offDegree;

    public Leans45DegreeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Leans45DegreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Leans45DegreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        offDegree = 0.14054083;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Leans45DegreeView, defStyleAttr, 0);
        height = typedArray.getDimensionPixelSize(R.styleable.Leans45DegreeView_leans_height, 0);
        typedArray.recycle();

    }

    //tan(8°) = 0.14054083470239
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path5 = new Path();
        path5.moveTo(0, height);
        path5.lineTo(0, height / 2 + (float) offDegree * SysUtils.WIDTH / 2);
        path5.lineTo(SysUtils.WIDTH, height / 2 - (float) offDegree * SysUtils.WIDTH / 2);
        path5.lineTo(SysUtils.WIDTH, height);
        path5.close();
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(path5, paint);
    }
}
