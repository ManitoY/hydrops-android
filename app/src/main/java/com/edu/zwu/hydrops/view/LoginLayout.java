package com.edu.zwu.hydrops.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by shengwei.yi on 2015/11/19.
 */
public class LoginLayout extends ScrollView {
    private OnSizeChangedListener listener;

    public LoginLayout(Context context) {
        super(context);
    }

    public LoginLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (listener != null) {
            listener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.listener = listener;
    }

    public interface OnSizeChangedListener {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
