package com.firebox.androidapp.helper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by salvo on 09/10/16.
 */
public class CookieJar {
    public static okhttp3.CookieJar getVolatileCookieJar() {
        return new okhttp3.CookieJar() {
            private List<Cookie> cookies;

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                this.cookies =  cookies;
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                if (cookies != null)
                    return cookies;
                return new ArrayList<Cookie>();

            }
        };
    }
}
