package com.wen.magi.baseframe.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wen.magi.baseframe.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class NumKeyboardView extends PercentRelativeLayout implements View.OnClickListener {
    private static final int STATE_OTHER = -1;
    private static final int STATE_PLUS = 0;
    private static final int STATE_MINUS = 1;
    private static final int STATE_EQUAL = 2;
    boolean called;
    private NumInputView numInputView;
    private TextView textView;
    private List<InnerClass> mLists;
    private DecimalFormat decimalFormat;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick();

        void onAddSignal(List<InnerClass> list, String str);
    }

    public static class InnerClass {
        final boolean called;
        final int curState;
        final double calResult;

        InnerClass(int i) {
            called = true;
            curState = i;
            calResult = 0.0d;
        }

        InnerClass(double d) {
            called = false;
            curState = NumKeyboardView.STATE_OTHER;
            calResult = d;
        }
    }


    public NumKeyboardView(Context context) {
        super(context);
        mLists = new ArrayList();
        decimalFormat = new DecimalFormat("0.00");
        called = false;
        initView(context);
    }

    public NumKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mLists = new ArrayList();
        decimalFormat = new DecimalFormat("0.00");
        called = false;
        initView(context);
    }

    public NumKeyboardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mLists = new ArrayList();
        decimalFormat = new DecimalFormat("0.00");
        called = false;
        initView(context);
    }

    public void setKeyboardListener(OnClickListener c0283a) {
        onClickListener = c0283a;
    }

    public void setInputView(@NonNull NumInputView numInputView) {
        numInputView = numInputView;
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_calculator_keyboard, this, true);
        bindView();
    }

    private void bindView() {
        findViewById(R.id.account_kb_0).setOnClickListener(this);
        findViewById(R.id.account_kb_1).setOnClickListener(this);
        findViewById(R.id.account_kb_2).setOnClickListener(this);
        findViewById(R.id.account_kb_3).setOnClickListener(this);
        findViewById(R.id.account_kb_4).setOnClickListener(this);
        findViewById(R.id.account_kb_5).setOnClickListener(this);
        findViewById(R.id.account_kb_6).setOnClickListener(this);
        findViewById(R.id.account_kb_7).setOnClickListener(this);
        findViewById(R.id.account_kb_8).setOnClickListener(this);
        findViewById(R.id.account_kb_9).setOnClickListener(this);
        View findViewById = findViewById(R.id.account_kb_delete);
        findViewById.setOnClickListener(this);
        findViewById(R.id.account_kb_dot).setOnClickListener(this);
        findViewById(R.id.account_kb_plus).setOnClickListener(this);
        findViewById(R.id.account_kb_minus).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.account_kb_OK);
        textView.setOnClickListener(this);
        findViewById.setOnLongClickListener(new OnDeleteLongClickListener());
    }

    private boolean m15738d() {
        return mLists.size() > 0 && mLists.get(mLists.size() + STATE_OTHER).called;
    }

    public void onClick(View view) {
        if (numInputView != null) {
            Editable text;
            switch (view.getId()) {
                case R.id.account_kb_7:
                case R.id.account_kb_8:
                case R.id.account_kb_9:
                case R.id.account_kb_4:
                case R.id.account_kb_5:
                case R.id.account_kb_6:
                case R.id.account_kb_1:
                case R.id.account_kb_2:
                case R.id.account_kb_3:
                case R.id.account_kb_0:
                    addNum();
                    text = numInputView.getText();
                    CharSequence charSequence = ((TextView) view).getText().toString();
                    if (!m15738d() || called) {
                        text.insert(numInputView.getSelectionStart(), charSequence);
                    } else {
                        text.replace(STATE_PLUS, text.length(), charSequence);
                        numInputView.setSelection(STATE_MINUS);
                        m15742b();
                    }
                    called = true;
                case R.id.account_kb_plus:
                    addOperation(STATE_PLUS);
                case R.id.account_kb_minus:
                    addOperation(STATE_MINUS);
                case R.id.account_kb_dot:
                    addNum();
                    text = numInputView.getText();
                    if (!m15738d() || called) {
                        String obj = text.toString();
                        if (obj.length() == 0) {
                            text.append("0.");
                        } else if (obj.contains(".")) {
                            numInputView.setSelection(obj.indexOf(46) + STATE_MINUS);
                        } else {
                            text.append('.');
                        }
                    } else {
                        text.replace(STATE_PLUS, text.length(), "0.");
                        numInputView.setSelection(STATE_EQUAL);
                        m15742b();
                    }
                    called = true;
                case R.id.account_kb_delete:
                    addNum();
                    text = numInputView.getText();
                    if (called) {
                        int selectionStart = numInputView.getSelectionStart();
                        int selectionEnd = numInputView.getSelectionEnd();
                        if (selectionStart == selectionEnd) {
                            if (text.toString().startsWith(".") && selectionStart == STATE_MINUS) {
                                text.replace(selectionStart + STATE_OTHER, selectionStart, "0");
                            }
                            if (selectionStart > 0) {
                                text.delete(selectionStart + STATE_OTHER, selectionStart);
                                return;
                            }
                            return;
                        }
                        text.delete(selectionStart, selectionEnd);
                        return;
                    }
                    text.clear();
                case R.id.account_kb_OK:
                    m15741g();
                default:
            }
        }
    }

    private void addNum() {
        //TODO need // TODO: 10/14/16
        //TODO
        if (mLists.size() == 0 && onClickListener != null) {
            onClickListener.onAddSignal(mLists, "");
        }
    }

    private void m15740f() {
        textView.setText(mLists.size() > STATE_MINUS ? "=" : "OK");
    }

    private void m15741g() {
        if (mLists.size() > 0) {
            String obj = numInputView.getText().toString();
            if (obj.length() > 0) {
                mLists.add(new InnerClass(Double.valueOf(obj).doubleValue()));
            } else {
                mLists.add(new InnerClass(0.0d));
            }
            mLists.add(new InnerClass((int) STATE_EQUAL));
            m15742b();
            double d = ((InnerClass) mLists.get(STATE_PLUS)).calResult;
            int size = mLists.size();
            double d2 = d;
            int i = STATE_EQUAL;
            while (i < size) {
                double d3;
                int i2 = ((InnerClass) mLists.get(i + STATE_OTHER)).curState;
                if (i2 == 0) {
                    d3 = ((InnerClass) mLists.get(i)).calResult + d2;
                } else if (i2 == STATE_MINUS) {
                    d3 = d2 - ((InnerClass) mLists.get(i)).calResult;
                } else {
                    throw new RuntimeException("\u7b97\u5f0f\u683c\u5f0f\u9519\u8bef\uff01\u65e0\u6cd5\u8ba1\u7b97\u7ed3\u679c");
                }
                i += STATE_EQUAL;
                d2 = d3;
            }
            called = true;
            mLists.clear();
            numInputView.setText(decimalFormat.format(d2));
            numInputView.resetSelection();
            m15740f();
        } else if (onClickListener != null) {
            onClickListener.onClick();
        }
    }

    private void addOperation(int i) {
        if (numInputView != null) {
            if (called || !m15738d()) {
                String obj = numInputView.getText().toString();
                if (obj.length() > 0) {
                    mLists.add(new InnerClass(Double.valueOf(obj).doubleValue()));
                } else {
                    mLists.add(new InnerClass(0.0d));
                }
                mLists.add(new InnerClass(i));
            } else {
                mLists.set(mLists.size() + STATE_OTHER, new InnerClass(i));
            }
            called = false;
            m15742b();
            m15740f();
        }
    }

    void m15742b() {
        if (onClickListener != null) {
            onClickListener.onAddSignal(mLists, getEquationString());
        }
    }

    private String getEquationString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (InnerClass c0299b : mLists) {
            if (c0299b.called) {
                switch (c0299b.curState) {
                    case STATE_PLUS:
                        stringBuilder.append("+");
                        break;
                    case STATE_MINUS:
                        stringBuilder.append("-");
                        break;
                    case STATE_EQUAL:
                        stringBuilder.append("=");
                        break;
                    default:
                        break;
                }
            }
            stringBuilder.append(decimalFormat.format(c0299b.calResult));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private class OnDeleteLongClickListener implements OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
