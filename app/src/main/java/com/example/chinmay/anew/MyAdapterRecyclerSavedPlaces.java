package com.example.chinmay.anew;

import android.content.Context;
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

public class MyAdapterRecyclerSavedPlaces extends RecyclerView.Adapter<MyAdapterRecyclerSavedPlaces.MyViewHolder> {



    private ArrayList<String> LocationArray;
    private Context context;


    public MyAdapterRecyclerSavedPlaces(Context contxt,ArrayList<String> la) {

        context=contxt;



        LocationArray=new ArrayList<>();
        LocationArray=la;




    }

//    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
//
//    }

    @Override
    public MyAdapterRecyclerSavedPlaces.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.savedplaces, parent, false);



        MyViewHolder vh = new MyViewHolder(v);
        context=v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {






        holder.Address.setText(LocationArray.get(position));




    }

    @Override
    public int getItemCount() {
        return LocationArray.size();

        //This will be changed later
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact;
        public TextView timeToReach;
        public ImageView img1;
        public TextView Address;
        public TextView directions;


        public MyViewHolder(View v) {
            super(v);
            Address = (TextView) v.findViewById(R.id.name);
            timeToReach = (TextView) v.findViewById(R.id.time);
            img1=(ImageView)v.findViewById(R.id.image);



        }


    }


}
