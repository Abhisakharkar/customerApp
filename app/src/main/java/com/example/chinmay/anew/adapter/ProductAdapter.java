package com.example.chinmay.anew.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.model.ProductDetail;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductCustomeAdapter>{

    private ArrayList<ProductDetail> productDetails;
    private Context context;
    private HashMap<String,Integer> priceAndId;

    public ProductAdapter(ArrayList<ProductDetail> productDetails, Context context, HashMap<String,Integer> priceAndId) {
        this.productDetails = productDetails;
        this.context = context;
        this.priceAndId = priceAndId;
    }

    @NonNull
    @Override
    public ProductCustomeAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item,parent,false);
        return new ProductCustomeAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCustomeAdapter holder, int position) {
        int price = priceAndId.get(productDetails.get(position).getId());
        holder.productPrice.setText(String.valueOf(price));
        holder.productName.setText(productDetails.get(position).getName());
        Glide.with(context).load("http://ec2-13-59-88-132.us-east-2.compute.amazonaws.com/magento/pub/media/catalog/product"+productDetails.get(position).getImage()).into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ProductCustomeAdapter extends RecyclerView.ViewHolder{

        private ImageView productImg;
        private TextView productName;
        private TextView productPrice;

        public ProductCustomeAdapter(View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.productImage);
            productName =itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
