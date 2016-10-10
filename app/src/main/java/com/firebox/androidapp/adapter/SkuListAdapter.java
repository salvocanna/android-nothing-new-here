package com.firebox.androidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebox.androidapp.R;
import com.firebox.androidapp.entity.ProductBlock;
import com.firebox.androidapp.entity.ProductSku;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SkuListAdapter extends ArrayAdapter<ProductSku> {
    public SkuListAdapter(Context context, ArrayList<ProductSku> skus) {
        super(context, 0, skus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProductSku sku = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sku_list_item, parent, false);
        }

        convertView.setPadding(0,0,0,0);

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.sku_name);
        TextView stockStatus = (TextView) convertView.findViewById(R.id.sku_stock_status);
        TextView price = (TextView) convertView.findViewById(R.id.sku_price);

        name.setText(sku.name);

        if (sku.stockStatus == 0) {
            stockStatus.setText("Pre-order");
            stockStatus.setTextColor(getContext().getResources().getColor(R.color.orange));
        } else {
            stockStatus.setText("In stock");
            stockStatus.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        price.setText("£".concat(sku.price.toString()));

        return convertView;

    }
}