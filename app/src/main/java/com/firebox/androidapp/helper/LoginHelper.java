package com.firebox.androidapp.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.firebox.androidapp.util.ExpirableSharedPrederences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginHelper {

    private static LoginHelper mInstance = null;
    private Context context;
    private ExpirableSharedPrederences esp;

    private LoginHelper(Context c) {
        context = c;
        esp = ExpirableSharedPrederences.getInstance(c);
    }

    public Boolean isUserLoggedIn()
    {
        Boolean v = esp.getBoolean("login-loggedin");

        return v != null && v;
    }

    public void setUserLoggedIn(Boolean r)
    {
        esp.setBoolean("login-loggedin", r);
    }

    public String getLoginEmail()
    {
        return esp.getString("login-email");
    }

    public void setLoginEmail(String email)
    {
        esp.setString("login-email", email);
    }

    public String getLoginPassword()
    {
        return esp.getString("login-password");
    }

    public void setLoginPassword(String password)
    {
        esp.setString("login-password", password);
    }

    public static LoginHelper getInstance(Context c) {
        if (mInstance == null) {
            synchronized (LoginHelper.class) {
                mInstance = new LoginHelper(c);
            }
        }
        return mInstance;
    }

    public abstract class UserLogin extends AsyncTask<Void, Void, Boolean> {

        private String email;
        private String password;

        private Boolean loginResult = null;
        private String  loginEmail  = "";
        private String  loginError  = "";

        public UserLogin(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                String token = "";
                Request request;

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.cookieJar(CookieJar.getVolatileCookieJar());
                OkHttpClient client = builder.build();

                request = new Request.Builder()
                        .url("https://www.firebox.com/token")
                        .build();

                Response responses = client.newCall(request).execute();

                token = (new JSONObject(responses.body().string())).getString("token");

                //Log.i("LOGIN-TOKEN", token);
                //Let's do the real login now.

                RequestBody formBody = new FormBody.Builder()
                        .add("email", this.email)
                        .add("password", this.password)
                        .add("_csrf_token", token)
                        .build();

                request = new Request.Builder()
                        .url("https://www.firebox.com/login_check")
                        .post(formBody)
                        .addHeader("x-requested-with","XMLHttpRequest")
                        .build();

                Response loginResponse = client.newCall(request).execute();

                JSONObject responseObject = new JSONObject(loginResponse.body().string());

                //{"status":true,"email":"salvo.cannamela@xxxx.com","id":xxxx,"roles":["xxxx"]}

                //Addresses and other info
                //https://www.firebox.com/checkout/user
                //{loggedIn: true, emailAddress: "salvo.cannamela@xxxx.com", title: "Mr.", firstName: "Salvo",...}

                loginResult = responseObject.getBoolean("status");
                if (loginResult) {
                    loginEmail = responseObject.getString("email");
                    loginError = "";
                } else {
                    loginError = "SERVER: ".concat(responseObject.getString("message"));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return true;
        }

        public abstract void onPostExecuteCallback(Boolean result, String loginError, String loginEmail);
        public abstract void onCancelledCallback();

        @Override
        protected void onPostExecute(final Boolean success) {
            onPostExecuteCallback(loginResult, loginError, loginEmail);

            //mAuthTask = null;
            //showProgress(false);
            /*if (success) {
                //Login gone good.. what now?
                //finish();
                //Save it on shared preferences...
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

        @Override
        protected void onCancelled() {
            onCancelledCallback();
            //mAuthTask = null;
            //showProgress(false);
        }
    }


}
