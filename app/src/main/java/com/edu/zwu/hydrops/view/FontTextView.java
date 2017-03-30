package com.edu.zwu.hydrops.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by shengwei.yi on 17/3/29.
 */

public class FontTextView extends TextView{

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    private Typeface createTypeface(Context context, String fontPath) {
//        return Typeface.createFromAsset(context.getAssets(), fontPath);
//    }
//
//    @Override
//    public void setTypeface(Typeface tf, int style) {
//        super.setTypeface(createTypeface(getContext(),"fonts/syht.ttf"), style);
//    }
}
