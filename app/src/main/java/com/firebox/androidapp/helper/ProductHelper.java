package com.firebox.androidapp.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.firebox.androidapp.R;
import com.firebox.androidapp.activity.ActivityProduct;
import com.firebox.androidapp.adapter.ProductBlockAdapter;
import com.firebox.androidapp.entity.ProductBlock;
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

public class ProductHelper  {

    ArrayList<ProductBlock> productArray = new ArrayList<ProductBlock>();
    Context context;

    public static final Integer SORT_BY_CHART_ASC = 1;
    public static final Integer SORT_BY_PRICE_ASC = 2;


    public ProductHelper(Context c) {
        this.context = c;

    }

    public interface CallbackReceiver {
        public void receiveData(ArrayList<ProductBlock> productBlocks);
    }

    public abstract class ProductGetter extends AsyncTask<Void, Void, Void> implements CallbackReceiver {

        //Runnable callback;
        Context context;
        ArrayList<Integer> tagFiltering;
        Integer sortBy;

        public ProductGetter(Context c, ArrayList<Integer> tagFiltering, Integer sortBy)
        {
            this.context = c;
            this.tagFiltering = tagFiltering;
            this.sortBy = sortBy;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String SharedPreferencesProductKey = "product-summary";
                String jsonData = PreferenceManager.
                        getDefaultSharedPreferences(context).getString(SharedPreferencesProductKey, "");

                if (jsonData.length() < 1) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.firebox.com/product/summary-all")
                            .build();

                    Response responses = client.newCall(request).execute();
                    jsonData = responses.body().string();

                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                            .putString(SharedPreferencesProductKey, jsonData).apply();
                }


                JSONArray productsList = new JSONArray(jsonData);

                ArrayList<ProductBlock> tmpProductList = new ArrayList<ProductBlock>();

                for (int i = 0; i < productsList.length(); i++) {
                    JSONObject productObject = productsList.getJSONObject(i);

                    //This 9999 is one of the most stupid things I've ever done.
                    Integer chartPosition = productObject.optInt("chartPosition", 9999);

                    Integer productId = productObject.getInt("id");
                    String imageUrl = "https:".concat(productObject.getString("image"));
                    tmpProductList.add(new ProductBlock(
                            productId,
                            productObject.getString("name"),
                            imageUrl,
                            chartPosition));
                }



                Collections.sort(tmpProductList, new Comparator<ProductBlock>() {
                    @Override
                    public int compare(ProductBlock o1, ProductBlock o2) {
                        if (sortBy.equals(ProductHelper.SORT_BY_CHART_ASC)) {
                            if (o1.chartPosition < o2.chartPosition) {
                                return -1;
                            } else if (o1.chartPosition > o2.chartPosition) {
                                return 1;
                            }
                            return 0;
                        }
                        return 0;
                    }
                });
                productArray = tmpProductList;

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public abstract void receiveData(ArrayList<ProductBlock> productBlocks);

        @Override
        protected void onPostExecute(Void voids)
        {
            //ProductBlockAdapter adapter = new ProductBlockAdapter(getApplicationContext(), productArray);
            //GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);
            //gridview.setAdapter(adapter);

            receiveData(productArray);
        }


    }
}
