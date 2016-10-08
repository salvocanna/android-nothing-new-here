package com.firebox.androidapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.firebox.androidapp.R;
import com.firebox.androidapp.adapter.ProductBlockAdapter;
import com.firebox.androidapp.entity.ProductBlock;
import com.firebox.androidapp.helper.ProductHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityTop50 extends AppCompatActivity {

    ArrayList<ProductBlock> productArray = new ArrayList<ProductBlock>();
    ProductHelper ph = new ProductHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);

        ProductHelper ph = new ProductHelper(this);

        ProductHelper.ProductGetter pg = ph.new ProductGetter(this, null, ProductHelper.SORT_BY_CHART_ASC) {
            @Override
            public void receiveData(ArrayList<ProductBlock> productBlocks) {
                productArray = productBlocks;
                ProductBlockAdapter adapter = new ProductBlockAdapter(getApplicationContext(), productArray);
                GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);
                gridview.setAdapter(adapter);
            }
        };

        pg.execute();


        GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent myIntent = new Intent(ActivityTop50.this, ActivityProduct.class);
                myIntent.putExtra("productId", productArray.get(position).id);
                ActivityTop50.this.startActivity(myIntent);
            }
        });

    }

}
