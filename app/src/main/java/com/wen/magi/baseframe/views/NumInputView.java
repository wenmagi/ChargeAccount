package com.wen.magi.baseframe.views;

import android.content.Context;
import android.support.v4.view.InputDeviceCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.TextView;

import static android.view.DragEvent.ACTION_DROP;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-13-2016
 */

public class NumInputView extends TextView {

    private final int MAX_NUM_LENGTH = 9;

    public NumInputView(Context context) {
        super(context);
        initPriorities();
    }

    public NumInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPriorities();
    }

    public NumInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPriorities();
    }

    private void initPriorities() {
        setInputType(InputDeviceCompat.SOURCE_TOUCHPAD);
        setTextIsSelectable(true);
        setFocusable(true);
    }

    public void resetSelection() {
        setSelection(getText().toString().length());
    }

    @Override
    public boolean onDragEvent(DragEvent dragEvent) {
        return dragEvent.getAction() == ACTION_DROP || super.onDragEvent(dragEvent);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        String substring;
        String ownStr = text.toString();
        int selectionStart = getSelectionStart();
        int index = ownStr.indexOf(".");

        if (index == -1)
            index = ownStr.length();

        int startPos = 0;

        String str = ownStr;

        while (startPos < index && ownStr.charAt(startPos) == '0' && startPos < index - 1 && ownStr.charAt(startPos + 1) != '.') {
            startPos++;
            str = str.substring(1);
        }

        index = str.indexOf(".");

        if (index > MAX_NUM_LENGTH || (index == -1 && str.length() > MAX_NUM_LENGTH)) {
            substring = str.substring(0, MAX_NUM_LENGTH);
        } else {
            substring = str;
        }
        int index2 = substring.indexOf(".");
        if (index2 != -1 && substring.length() > index2 + 2) {
            substring = substring.substring(0, index2 + 3);
        }
        if (!text.toString().equals(substring)) {
            setText(substring);
            setSelection(Math.min(selectionStart, substring.length()));
            invalidate();
        }
    }

    protected boolean getDefaultEditable() {
        return true;
    }

    protected MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    public Editable getText() {
        return (Editable) super.getText();
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(charSequence, BufferType.EDITABLE);
    }

    public void setSelection(int i, int i2) {
        Selection.setSelection(getText(), i, i2);
    }

    public void setSelection(int i) {
        Selection.setSelection(getText(), i);
    }

    public void setSelectionAll() {
        Selection.selectAll(getText());
    }

    public void extendSelection(int i) {
        Selection.extendSelection(getText(), i);
    }

    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (truncateAt == TextUtils.TruncateAt.MARQUEE) {
            throw new IllegalArgumentException("EditText cannot use the ellipsize mode TextUtils.TruncateAt.MARQUEE");
        }
        super.setEllipsize(truncateAt);
    }

    public CharSequence getAccessibilityClassName() {
        return NumInputView.class.getName();
    }
}
