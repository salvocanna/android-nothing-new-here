package com.firebox.androidapp.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebox.androidapp.R;
import com.firebox.androidapp.entity.ProductBlock;
import com.firebox.androidapp.util.SquareGridLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductBlockAdapter extends ArrayAdapter<ProductBlock> {
    public ProductBlockAdapter(Context context, ArrayList<ProductBlock> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProductBlock block = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_block1, parent, false);
            convertView.setPadding(14,14,14,14);
        }

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        //TextView imageurl = (TextView) convertView.findViewById(R.id.imageurl);

        ImageView image = (ImageView) convertView.findViewById(R.id.product_block_image);

        Picasso.with(getContext())
                .load(block.imageUrl)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(getContext(), "SUccess", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError() {
                        //Toast.makeText(getContext(), "onError", Toast.LENGTH_LONG).show();
                    }
                });

        // Populate the data into the template view using the data object
        name.setText(block.name);
        //imageurl.setText(block.imageUrl);
        // Return the completed view to render on screen
        return convertView;

    }
}