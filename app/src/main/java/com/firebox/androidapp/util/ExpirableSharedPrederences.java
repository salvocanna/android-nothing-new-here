package com.firebox.androidapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

public class ExpirableSharedPrederences {
    private static ExpirableSharedPrederences mInstance = null;
    private Context context;
    private SharedPreferences sp;

    private ExpirableSharedPrederences(Context c) {
        context = c;
        sp = PreferenceManager.
                getDefaultSharedPreferences(context);
    }

    public static ExpirableSharedPrederences getInstance(Context c) {
        if (mInstance == null) {
            synchronized (ExpirableSharedPrederences.class) {
                mInstance = new ExpirableSharedPrederences(c);
            }
        }
        return mInstance;
    }

    public void set(String key, String value)
    {
        sp.edit().putString(key, value).putLong(getTTLKey(key), 0).apply();
    }

    public void set(String key, String value, Integer ttl)
    {
        sp.edit().putString(key, value).putLong(getTTLKey(key), (ttl+getCurrentTime())).apply();
    }

    public String get(String key)
    {
        String data = sp.getString(key, null);

        if (data != null) {
            long ttl = sp.getLong(getTTLKey(key), -1);
            if (ttl != 0) {
                if (ttl < getCurrentTime()) {
                    //delete that value from shared preferences
                    sp.edit().remove(key).remove(getTTLKey(key)).apply();
                    data = null;
                }
            }
        }
        return data;
    }

    private String getTTLKey(String key)
    {
        return "ttl".concat(key);
    }

    private long getCurrentTime()
    {
        return (new Date()).getTime() * 1000;
    }

}
