package com.firebox.androidapp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class StrongTextView extends TextView {

    public StrongTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public StrongTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StrongTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Novecentowide-Bold-webfont.ttf");
        setTypeface(tf);
    }

}