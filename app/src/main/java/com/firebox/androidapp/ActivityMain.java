package com.firebox.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.firebox.androidapp.activity.ActivityCategory;
import com.firebox.androidapp.activity.ActivityProduct;
import com.squareup.picasso.Picasso;

public class ActivityMain extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.test_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ActivityMain.this, ActivityCategory.class);
                ActivityMain.this.startActivity(myIntent);
            }
        });

        ImageView whatNew = (ImageView) findViewById(R.id.main_whatsnew_imageview);
        ImageView overlay = (ImageView) findViewById(R.id.main_whatsnew_overlay_imageview);

        ////
        Picasso
                .with(this)
                .load("https://media.firebox.com/pic/p7618_column_grid_6.jpg")
                .fit()
                //.resize(whatNew.getMaxWidth(), 0)
                .into(whatNew);

        Picasso
                .with(this)
                .load("https://media.firebox.com/i/home/feature/overlay/newstuff_TR_black.png")
                .fit()
                .into(overlay);



        this.initDrawer();
    }
}
