package com.example.chinmay.anew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Chinmay on 21-03-2018.
 */

public class MyAdapterRecyclerMainAc extends RecyclerView.Adapter<MyAdapterRecyclerMainAc.MyViewHolder> {



    private ArrayList<retailers> retailersArray;
    private Context context;


    public MyAdapterRecyclerMainAc(Context contxt) {

        context=contxt;


        retailersArray=new ArrayList<>();
        retailersArray=ServerOp.retailersArray;



    }

//    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
//
//    }

    @Override
    public MyAdapterRecyclerMainAc.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);



        MyViewHolder vh = new MyViewHolder(v);
        context=v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {






        holder.enterpriseName.setText(retailersArray.get(position).getEnterpriseName());

        String url = "http://ec2-13-58-16-206.us-east-2.compute.amazonaws.com/rt/" + retailersArray.get(position).getShopPhoto();
        if (!url.equals("0") && !url.isEmpty()){
            Glide.with(context)
                    .load(url)
                    .into(holder.img1);
        }


    }

    @Override
    public int getItemCount() {
        return retailersArray.size();


        //This will be changed later
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact;
        public TextView timeToReach;
        public ImageView img1;
        public TextView enterpriseName;
        public TextView directions;


        public MyViewHolder(View v) {
            super(v);
            enterpriseName = (TextView) v.findViewById(R.id.name);
            timeToReach = (TextView) v.findViewById(R.id.time);
            img1=(ImageView)v.findViewById(R.id.image);



        }


    }


}
