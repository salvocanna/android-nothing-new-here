package com.firebox.androidapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebox.androidapp.BaseActivity;
import com.firebox.androidapp.R;
import com.firebox.androidapp.adapter.SkuListAdapter;
import com.firebox.androidapp.entity.FullProduct;
import com.firebox.androidapp.entity.ProductSku;
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

    private ViewPager pager;
    private ImageSlidePagerAdapter pagerAdapter;

    private ProductSku selectedSku;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent myIntent = getIntent(); // gets the previously created intent
        productId = myIntent.getIntExtra("productId", -1);

        pager = (ViewPager) findViewById(R.id.image_pager);


        ViewTreeObserver vto = pager.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pager.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                ViewGroup.LayoutParams pageLP = pager.getLayoutParams();
                pageLP.height = pager.getMeasuredWidth();
                pageLP.width  = pager.getMeasuredWidth();
                pager.setLayoutParams(pageLP);
            }
        });



        if (productId > 0) {
            //Display a spinner...
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

            if (product.imagesUrl.size() > 0) {

                pagerAdapter = new ImageSlidePagerAdapter(getSupportFragmentManager());
                pagerAdapter.setImages(product.imagesUrl);
                pager.setAdapter(pagerAdapter);

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

            final ListView skuListView = (ListView) findViewById(R.id.sku_list);
            final LinearLayout llSelectedSku = (LinearLayout) findViewById(R.id.selected_sku);

            LinearLayout slis = (LinearLayout) getLayoutInflater().inflate(R.layout.sku_list_item_selected, null);
            llSelectedSku.addView(slis);

            ViewGroup.LayoutParams params = slis.getLayoutParams();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                slis.setLayoutParams(params);
            }

            DefaultTextView skuName = (DefaultTextView) llSelectedSku.findViewById(R.id.sku_name);

            if (product.skus.size() > 1) {
                setSelectedSku(product.skus.get(0));
                SkuListAdapter sla = new SkuListAdapter(this, product.skus);
                skuListView.setAdapter(sla);

                llSelectedSku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llSelectedSku.setVisibility(View.GONE);
                        skuListView.setVisibility(View.VISIBLE);
                    }
                });
            } else if (product.skus.size() > 0) {
                setSelectedSku(product.skus.get(0));
                llSelectedSku.setVisibility(View.GONE);
                skuListView.setVisibility(View.GONE);
            } else {
                llSelectedSku.setVisibility(View.GONE);
                skuListView.setVisibility(View.GONE);

                Button addToBasket = (Button) findViewById(R.id.add_to_basket);
                addToBasket.setVisibility(View.GONE);
                // Disable add-to-basket
            }



            skuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setSelectedSku(product.skus.get(i));
                    skuListView.setVisibility(View.GONE);
                    llSelectedSku.setVisibility(View.VISIBLE);
                }
            });


            skuListView.setVisibility(View.GONE);


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

    private void setSelectedSku(ProductSku sku)
    {
        final LinearLayout llSelectedSku = (LinearLayout) findViewById(R.id.selected_sku);
        DefaultTextView skuName = (DefaultTextView) llSelectedSku.findViewById(R.id.sku_name);
        DefaultTextView skuStock = (DefaultTextView) llSelectedSku.findViewById(R.id.sku_stock_status);

        skuName.setText(sku.name);
        skuStock.setText(sku.stockStatus==0 ? "Pre-order" : "In stock");

        selectedSku = sku;
    }

    private class ProductInfoLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... Void) {

            product = new FullProduct(productId);

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
                JSONArray JSONSkus = productObject.getJSONArray("skus");

                ArrayList<String> ArrayKeyFeatures = new ArrayList<>();
                for (int i = 0; i < JSONKeyFeatures.length(); i++) {
                    ArrayKeyFeatures.add(JSONKeyFeatures.getString(i));
                }

                ArrayList<String> ArrayImages = new ArrayList<>();
                for (int i = 0; i < JSONImages.length(); i++) {
                    ArrayImages.add("https:".concat(JSONImages.getJSONObject(i).getString("mainUrl")));
                }

                for (int i = 0; i < JSONSkus.length(); i++) {
                    JSONObject jo = JSONSkus.getJSONObject(i);
                    ProductSku s = new ProductSku();
                    s.id = jo.getInt("id");
                    s.name = jo.getString("name");
                    s.stockStatus = jo.getInt("stockStatus");
                    s.price = jo.getJSONArray("price").getDouble(0);
                    product.addSku(s);
                }

                /*{
                    "id": 17127,
                    "name": "",
                    "stockStatus": 1,
                    "dateDue": "",
                    "stockNotifyMessage": "Notify me when back in stock",
                    "price": [
                        6.99,
                        9.09,
                        8.29
                    ],
                    "secondarySku": 0,
                    "sortOrder": 0,
                    "onPromotion": false,
                    "promotion": null,
                    "priceBeforePromotion": [
                        6.99,
                        9.09,
                        8.29
                    ],
                    "promotionPercentage": 0,
                    "isTooExpensiveToPurchase": false,
                    "isAprilFools": false
                }*/



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
                    product.imagesUrl.add(imageUrl);
                }

            } catch (JSONException | IOException e) {
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


    public  class ImageSlideFragment extends Fragment {

        private String url = "";
        ImageSlideFragment(String url)
        {
            this.url = url;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_image_slide, container, false);


            ImageView imageView = (ImageView) rootView.findViewById(R.id.product_main_image);
            Picasso
                .with(getApplicationContext())
                .load(this.url)
                .into(imageView);

            return rootView;
        }
    }

    private class ImageSlidePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> images = new ArrayList<>();

        public ImageSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public void setImages(ArrayList<String> url)
        {
            images = url;
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageSlideFragment(images.get(position));
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }



}
