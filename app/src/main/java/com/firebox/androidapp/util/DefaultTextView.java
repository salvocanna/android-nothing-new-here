package com.firebox.androidapp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class DefaultTextView extends TextView {

    public DefaultTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DefaultTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        //if (!isInEditMode()) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/SofiaProRegular-webfont.ttf");
        setTypeface(tf);
        //}
    }

}