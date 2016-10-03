package com.firebox.androidapp.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

/**
 * Created by salvo on 02/10/16.
 */
public class BlackButton extends Button {


    public BlackButton(Context context) {
        super(context);
        doManage();
    }

    public BlackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        doManage();
    }

    public BlackButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doManage();
    }

    public BlackButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        doManage();
    }

    private void doManage() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Novecentowide-Bold-webfont.ttf");
        setTypeface(tf);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        setBackgroundColor(Color.argb(255, 34, 34, 34));
        setTextColor(Color.WHITE);
        setPadding(50, 20, 50, 20);
        setMinHeight(0);
        
    }
}
