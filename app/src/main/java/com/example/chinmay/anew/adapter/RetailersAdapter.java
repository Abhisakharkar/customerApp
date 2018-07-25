package com.example.chinmay.anew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.model.RetailersList;
import com.example.chinmay.anew.repository.ServerOperation;

import java.util.ArrayList;

/**
 * Created by Chinmay on 21-03-2018.
 */

public class RetailersAdapter extends RecyclerView.Adapter<RetailersAdapter.MyViewHolder> {



    private ArrayList<RetailersList> retailersListArray;
    private Context context;


    public RetailersAdapter(Context contxt) {

        context=contxt;


        retailersListArray =new ArrayList<>();
        retailersListArray = ServerOperation.retailersArray;



    }

//    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
//
//    }

    @Override
    public RetailersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);



        MyViewHolder vh = new MyViewHolder(v);
        context=v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(retailersListArray.size()==0)
        {
            holder.retail.setText("No RetailersList found near you");
        }






        holder.enterpriseName.setText(retailersListArray.get(position).getEnterpriseName());

        String url = "http://ec2-13-58-16-206.us-east-2.compute.amazonaws.com/rt" + retailersListArray.get(position).getShopPhoto();
        if (!url.equals("0") && !url.isEmpty()){
            Glide.with(context)
                    .load(url)
                    .into(holder.img1);
        }
        if(holder.img1.getDrawable()==null) {

            holder.img1.setImageResource(R.drawable.store);
        }



    }

    @Override
    public int getItemCount() {
        return retailersListArray.size();


        //This will be changed later
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact;
        public TextView timeToReach;
        public ImageView img1;
        public TextView enterpriseName;
        public TextView retail;


        public MyViewHolder(View v) {
            super(v);
            enterpriseName = (TextView) v.findViewById(R.id.name);
            timeToReach = (TextView) v.findViewById(R.id.time);
            img1=(ImageView)v.findViewById(R.id.image);
            retail=(TextView)v.findViewById(R.id.retail);
            if(retailersListArray.size()==0)
            {
                retail.setText("No RetailersList found near you");
            }



        }


    }


}
