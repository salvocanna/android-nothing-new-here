package com.firebox.androidapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebox.androidapp.R;
import com.firebox.androidapp.entity.FullProduct;
import com.firebox.androidapp.util.DefaultTextView;
import com.firebox.androidapp.util.StrongTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ActivityProduct extends AppCompatActivity {
    private int productId;
    private ProgressBar pb;

    private FullProduct product;

    private Boolean loadedMainImage = false;
    private Boolean loadedInfo = false;

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

        Picasso
            .with(this)
            .load("https://media.firebox.com/product/7789/column_grid_8/rainbow-rage_28474.jpg")
            .placeholder(R.drawable.ic_logo)
            .into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    //
                    loadedMainImage = true;
                    onLoadedPart();
                }

                @Override
                public void onError() {
                    //
                }
            });
    }

    private void onLoadedPart()
    {
        if (loadedMainImage && loadedInfo) {
            Toast.makeText(this, "Loaded!", Toast.LENGTH_SHORT).show();

            StrongTextView title = ((StrongTextView) findViewById(R.id.product_title));
            title.setText(product.name);
            title.setTextColor(Color.BLACK);
            DefaultTextView subtitle = ((DefaultTextView) findViewById(R.id.product_subtitle));
            subtitle.setText(product.subtitle);
            subtitle.setTextColor(Color.BLACK);


            return;
        }
        //Else still wait here..
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
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            loadedInfo = true;

            product = new FullProduct();
            product.id = 5398;
            product.name = "Chocolate Skulls";
            product.subtitle = "Scrumptious skullduggery";
            product.images.add("https://media.firebox.com/product/5398/column_grid_8/chocolate-skulls_7567.jpg");
            product.images.add("https://media.firebox.com/product/5398/extra1_column_grid_8/chocolate-skulls_7568.jpg");
            product.images.add("https://media.firebox.com/product/5398/extra2_column_grid_8/chocolate-skulls_7569.jpg");
            product.keyFeatures.add("Confectionery craniums moulded from an authentic human skull");
            product.keyFeatures.add("Obtained by completely non-nefarious means – don’t ask");
            product.keyFeatures.add("2.5kg of chocolatey (and presumably Belgian) human remains");
            product.keyFeatures.add("Feel like an eccentric Hannibal Lecter sort of character");
            product.keyFeatures.add("Better (and tastier) than spending months excavating one yourself");

            onLoadedPart();

            //dismissLoadingDialog();
            return null;
        }

    }


}
