package com.firebox.androidapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebox.androidapp.R;
import com.squareup.picasso.Picasso;

public class ActivityProduct extends AppCompatActivity {
    private int productId;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent myIntent = getIntent(); // gets the previously created intent
        productId = myIntent.getIntExtra("productId", -1);
        //pb = (ProgressBar) findViewById(R.id.product_spinner);

        if (productId > 0) {
            //Display a spinner...
            //pb.setVisibility(View.VISIBLE);
            ProductInfoLoader pil = new ProductInfoLoader();
            pil.execute();
        } else {
            //Display an user friendly error message...

        }

        //.placeholder(R.drawable.profile_wall_picture)
        //        .resize(0, holder.message_picture.getHeight()),


        ImageView imageView = (ImageView) findViewById(R.id.product_main_image);

//        Toast.makeText(this, imageView.getWidth(), Toast.LENGTH_SHORT).show();

        Picasso
                .with(this)
                .load("https://media.firebox.com/product/7789/column_grid_8/rainbow-rage_28474.jpg")
                //.fit()
                //.resize(imageView.getWidth(), imageView.getWidth())
                .into(imageView);
    }

    private void dismissLoadingDialog()
    {
     //   pb.setVisibility(View.GONE);
    }

    private class ProductInfoLoader extends AsyncTask<Void, Void, Void> {
        /*private Context context;
        public ProductInfoLoader(Context c) {
            context = c;
        }*/

        @Override
        protected Void doInBackground(Void... Void) {

            try {
                //productId
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //dismissLoadingDialog();

            return null;
        }

    }


}
