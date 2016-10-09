package com.firebox.androidapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebox.androidapp.activity.ActivityCategory;
import com.firebox.androidapp.activity.ActivitySearchable;


public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //<item name="android:statusBarColor">@color/navigationBarColor</item>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.navigationBarColor, null));
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBarColor, null));
        }

        //setContentView(R.layout.activity_main);

    }

    protected void initDrawer()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searched:: ".concat(query), Toast.LENGTH_SHORT).show();
        }



        /*Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //SearchManager.QUERY
            Intent myIntent = new Intent(BaseActivity.this, ActivitySearchable.class);
            myIntent.putExtra(SearchManager.QUERY, query);
            BaseActivity.this.startActivity(myIntent);
            //doMySearch(query);
        }*/

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) BaseActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setQueryHint("Search: ");
            searchView.setOnQueryTextListener(this);
            //searchView.setOnCloseListener(...);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(BaseActivity.this.getComponentName()));

            searchView.setOnSuggestionListener(this);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent categoryIntent = new Intent(BaseActivity.this, ActivityCategory.class);



        if (id == R.id.category_top50) {
            // Handle the camera action
            //Toast.makeText(getApplicationContext(), "Clicked camera", Toast.LENGTH_SHORT).show();
            categoryIntent.putExtra(ActivityCategory.TYPE, ActivityCategory.TOP50);
        } else if (id == R.id.category_new) {
            categoryIntent.putExtra(ActivityCategory.TYPE, ActivityCategory.NEW);
        } else if (id == R.id.gift_for_him) {
            categoryIntent.putExtra(ActivityCategory.TYPE, ActivityCategory.TAG);
            categoryIntent.putExtra(ActivityCategory.TAG_ID, 9);
        } else if (id == R.id.gift_for_her) {
            categoryIntent.putExtra(ActivityCategory.TYPE, ActivityCategory.TAG);
            categoryIntent.putExtra(ActivityCategory.TAG_ID, 10);
        }

        BaseActivity.this.startActivity(categoryIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(), "Query ".concat(query), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.search_layout);

        if (newText.length() > 0) {
            ll.setVisibility(View.VISIBLE);
            TextView tv = (TextView) findViewById(R.id.search_text);
            tv.setText(newText);
            //Toast.makeText(getApplicationContext(), "Change ".concat(newText), Toast.LENGTH_SHORT).show();
        } else {
            ll.setVisibility(View.GONE);

        }

        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        return false;
    }
}
