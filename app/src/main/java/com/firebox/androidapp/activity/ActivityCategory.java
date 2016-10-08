package com.firebox.androidapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.firebox.androidapp.R;
import com.firebox.androidapp.adapter.ProductBlockAdapter;
import com.firebox.androidapp.entity.ProductBlock;
import com.firebox.androidapp.helper.ProductHelper;

import java.util.ArrayList;

public class ActivityCategory extends AppCompatActivity {

    ArrayList<ProductBlock> productArray = new ArrayList<ProductBlock>();
    ProductHelper ph = new ProductHelper(this);

    public static final String TYPE   = "type";
    public static final String TAG_ID = "tagId";

    public static final int TAG = 0;
    public static final int TOP50 = 1;
    public static final int NEW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);

        ProductHelper ph = new ProductHelper(this);

        Integer tagFiltering = null;
        Integer sortBy = null;

        Intent intent = getIntent(); // gets the previously created intent

        Integer gotType = intent.getIntExtra(TYPE, TOP50);

        if (gotType == TOP50) {
            sortBy = ProductHelper.SORT_BY_CHART_ASC;
        } else if (gotType == NEW) {
            sortBy = ProductHelper.SORT_BY_NEW_ASC;
        } else if (gotType == TAG) {
            tagFiltering = intent.getIntExtra(TAG_ID, 8);
            sortBy = ProductHelper.SORT_BY_CHART_ASC;
        }

        ProductHelper.ProductGetter pg = ph.new ProductGetter(this, tagFiltering, sortBy) {
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

                Intent myIntent = new Intent(ActivityCategory.this, ActivityProduct.class);
                myIntent.putExtra("productId", productArray.get(position).id);
                ActivityCategory.this.startActivity(myIntent);

            }
        });

    }

}
