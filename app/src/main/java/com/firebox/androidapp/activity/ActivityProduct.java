package com.firebox.androidapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebox.androidapp.BaseActivity;
import com.firebox.androidapp.R;
import com.firebox.androidapp.entity.FullProduct;
import com.firebox.androidapp.util.DefaultTextView;
import com.firebox.androidapp.util.StrongTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityProduct extends BaseActivity {
    private Integer productId;
    private ProgressBar pb;

    private FullProduct product;

    private Boolean loadedMainImage = false;
    private Boolean loadedInfo = false;
    private Boolean viewLoaded = false;

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

        this.initDrawer();
    }

    private void onLoadedPart()
    {
        if (loadedMainImage && loadedInfo && !viewLoaded) {

            if (product.images.size() > 0) {
            ImageView imageView = (ImageView) findViewById(R.id.product_main_image);
            Picasso
                .with(this)
                .load(product.images.get(0))
                .into(imageView);
            }

            StrongTextView title = ((StrongTextView) findViewById(R.id.product_title));
            title.setText(product.name);
            title.setTextColor(Color.BLACK);
            DefaultTextView subtitle = ((DefaultTextView) findViewById(R.id.product_subtitle));
            subtitle.setText(product.subtitle);
            subtitle.setTextColor(Color.BLACK);


            LinearLayout keyFeatures = (LinearLayout) findViewById(R.id.product_keyfeatures);

            for (String feature: product.keyFeatures) {
                ImageView tick = new ImageView(this);
                setDimension(tick);
                tick.setImageResource(R.drawable.ic_check_black_24dp);

                LinearLayout ll = new LinearLayout(this);
                setDimension(ll);
                ll.addView(tick);

                TextView tv = new DefaultTextView(this);
                setDimension(tv);
                tv.setText(feature);
                tv.setTextColor(Color.BLACK);

                float scale = getResources().getDisplayMetrics().density;
                tv.setPadding((int) (10*scale + 0.5f), 0,0,0);

                ll.addView(tv);

                keyFeatures.addView(ll);

            }
            viewLoaded = true;

        }
        //Else still wait here..
    }

    private void setDimension(View v)
    {
        if (v != null) {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                v.setLayoutParams(params);
            }
        }
    }

    private class ProductInfoLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... Void) {

            product = new FullProduct();

            try {
                String preferencesProductInfoKey = "product-info-".concat(productId.toString());
                String jsonData = PreferenceManager.
                        getDefaultSharedPreferences(getApplicationContext()).getString(preferencesProductInfoKey,"");

                if (jsonData.length() < 1) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.firebox.com/product/info/".concat(productId.toString()))
                            .build();

                    Response responses = client.newCall(request).execute();
                    jsonData = responses.body().string();

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString(preferencesProductInfoKey, jsonData).apply();
                }

                JSONObject productObject = new JSONObject(jsonData);

                product.id  = productObject.getInt("id");
                product.name = productObject.getString("name");
                product.subtitle    = productObject.getString("tagline");
                JSONArray JSONKeyFeatures = productObject.getJSONArray("keyFeatures");
                JSONArray JSONImages = productObject.getJSONArray("images");

                ArrayList<String> ArrayKeyFeatures = new ArrayList<>();
                for (int i = 0; i < JSONKeyFeatures.length(); i++) {
                    ArrayKeyFeatures.add(JSONKeyFeatures.getString(i));
                }

                ArrayList<String> ArrayImages = new ArrayList<>();
                for (int i = 0; i < JSONImages.length(); i++) {
                    ArrayImages.add("https:".concat(JSONImages.getJSONObject(i).getString("mainUrl")));
                }

                for (String feature: ArrayKeyFeatures) {
                    product.keyFeatures.add(feature);
                }

                // Preload all the images!
                for (String imageUrl: ArrayImages) {
                    Picasso
                        .with(getApplicationContext())
                        .load(imageUrl)
                        .fetch(new Callback() {
                            @Override
                            public void onSuccess() {
                                //
                                loadedMainImage = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoadedPart();
                                    }
                                });
                            }
                            @Override
                            public void onError() {
                                //
                            }
                        });
                    product.images.add(imageUrl);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            loadedInfo = true;

            return null;
        }

        protected void onPostExecute() {
            //showDialog("Done!");
            onLoadedPart();
        }

    }


}
