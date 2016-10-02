package com.firebox.androidapp.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebox.androidapp.R;

public class ActivityProduct extends AppCompatActivity implements AsyncTask<> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);



    }

    @Override
    protected Object doInBackground(Object[] objects) {



        return null;
    }
}
