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

    public void setString(String key, String value)
    {
        sp.edit().putString(key, value).putLong(getTTLKey(key), 0).apply();
    }

    public void setBoolean(String key, Boolean value)
    {
        sp.edit().putBoolean(key, value).putLong(getTTLKey(key), 0).apply();
    }

    public void setString(String key, String value, Integer ttl)
    {
        sp.edit().putString(key, value).putLong(getTTLKey(key), (ttl+getCurrentTime())).apply();
    }

    public void setBoolean(String key, Boolean value, Integer ttl)
    {
        sp.edit().putBoolean(key, value).putLong(getTTLKey(key), (ttl+getCurrentTime())).apply();
    }

    public String getString(String key)
    {
        String data = null;
        if (sp.contains(key)) {
            data = sp.getString(key, null);

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

    public Boolean getBoolean(String key)
    {
        Boolean data = null;
        if (sp.contains(key)) {
            data = sp.getBoolean(key, false);

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
