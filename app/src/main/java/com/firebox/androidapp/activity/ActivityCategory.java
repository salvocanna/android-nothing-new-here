package com.firebox.androidapp.activity;

import android.content.Intent;
import android.icu.util.MeasureUnit;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TextView.*;
import android.widget.Toast;

import com.firebox.androidapp.BaseActivity;
import com.firebox.androidapp.R;
import com.firebox.androidapp.adapter.ProductBlockAdapter;
import com.firebox.androidapp.entity.ProductBlock;
import com.firebox.androidapp.helper.ProductHelper;
import com.firebox.androidapp.util.ExpandedGridView;
import com.firebox.androidapp.util.HeaderGridView;
import com.firebox.androidapp.util.StrongTextView;

import java.util.ArrayList;

public class ActivityCategory  extends BaseActivity {

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
        setContentView(R.layout.activity_category);

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
                HeaderGridView gridview = (HeaderGridView) findViewById(R.id.product_block_top50_gridview);
                gridview.setAdapter(adapter);
            }
        };

        pg.execute();


        HeaderGridView gridview = (HeaderGridView) findViewById(R.id.product_block_top50_gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent myIntent = new Intent(ActivityCategory.this, ActivityProduct.class);

                Integer index = (int) id;
                if (index >= 0 && index < productArray.size()) {
                    myIntent.putExtra("productId", productArray.get((int)id).id);
                    ActivityCategory.this.startActivity(myIntent);
                }

            }
        });

        StrongTextView a =  new StrongTextView(getApplicationContext());
        a.setText("Test label on GridView");
        a.setPadding(0, 50, 0, 50);
        a.setGravity(Gravity.CENTER);
        a.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        a.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        a.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        gridview.addHeaderView(a);

        this.initDrawer();


    }

}
