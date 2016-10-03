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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);


        /*
        PreferenceManager.getDefaultSharedPreferences(context).edit()
        .putString("theJson",jsonObject.toString()).apply();
        Getting the stored json:
        JsonObject jsonObject = PreferenceManager.
        getDefaultSharedPreferences(this).getString("theJson","");
        */

        new ProductGetter().execute();

        GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            /*    Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
*/

                Intent myIntent = new Intent(ActivityTop50.this, ActivityProduct.class);
                myIntent.putExtra("productId", productArray.get(position).id);
                ActivityTop50.this.startActivity(myIntent);
            }
        });

    }

    class ProductGetter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                /*
        Getting the stored json:
        JsonObject jsonObject = PreferenceManager.
        getDefaultSharedPreferences(this).getString("theJson","");
        */
                String SharedPreferencesProductKey = "product-summary";
                String jsonData = PreferenceManager.
                        getDefaultSharedPreferences(getApplicationContext()).getString(SharedPreferencesProductKey, "");

                if (jsonData.length() < 1) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.firebox.com/product/summary-all")
                            .build();

                    Response responses = client.newCall(request).execute();
                    jsonData = responses.body().string();

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString(SharedPreferencesProductKey, jsonData).apply();
                }


                JSONArray productsList = new JSONArray(jsonData);

                ArrayList<ProductBlock> tmpProductList = new ArrayList<ProductBlock>();

                for (int i = 0; i < productsList.length(); i++) {
                    JSONObject productObject = productsList.getJSONObject(i);

                    Integer chartPosition = productObject.optInt("chartPosition", 9999);
                    Integer productId = productObject.getInt("id");
                    String imageUrl = "https:".concat(productObject.getString("image"));
                    tmpProductList.add(new ProductBlock(
                            productId,
                            productObject.getString("name"),
                            imageUrl,
                            chartPosition));

                    if (chartPosition < 50) {
                        Picasso
                            .with(getApplicationContext())
                            .load(imageUrl)
                            .fetch();
                    }
                }

                Collections.sort(tmpProductList, new Comparator<ProductBlock>() {
                    @Override
                    public int compare(ProductBlock o1, ProductBlock o2) {
                        if (o1.chartPosition < o2.chartPosition) {
                            return -1;
                        } else if (o1.chartPosition > o2.chartPosition) {
                            return 1;
                        }
                        return 0;
                    }
                });
                productArray = tmpProductList;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids)
        {
            ProductBlockAdapter adapter = new ProductBlockAdapter(getApplicationContext(), productArray);
            GridView gridview = (GridView) findViewById(R.id.product_block_top50_gridview);
            gridview.setAdapter(adapter);
        }


    }
}
