package com.firebox.androidapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.firebox.androidapp.R;
import com.firebox.androidapp.adapter.ProductBlockAdapter;
import com.firebox.androidapp.entity.ProductBlock;

import java.util.ArrayList;

public class ActivityTop50 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);


        ArrayList<ProductBlock> arrayOfUsers = new ArrayList<ProductBlock>();

        for (int i = 0; i < 50; i++) {
            arrayOfUsers.add(new ProductBlock(617, "Monkey Picked Tea", "https://media.firebox.com/product/617/column_grid_3/monkey-picked-tea_20152.jpg"));
        }
        //arrayOfUsers.add(new ProductBlock("Monkey Picked Tea", "https://media.firebox.com/product/617/column_grid_3/monkey-picked-tea_20152.jpg"));
        //arrayOfUsers.add(new ProductBlock("a", "b"));
        //arrayOfUsers.add(new ProductBlock("Monkey Picked Tea", "https://media.firebox.com/product/617/column_grid_3/monkey-picked-tea_20152.jpg"));

        ProductBlockAdapter adapter = new ProductBlockAdapter(this, arrayOfUsers);

        GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);

        gridview.setAdapter(adapter);


        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HelloGridView.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        */

    }
}
