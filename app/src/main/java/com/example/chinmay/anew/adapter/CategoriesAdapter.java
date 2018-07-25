package com.example.chinmay.anew.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chinmay.anew.R;
import com.example.chinmay.anew.model.Category;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CustomAdapter>{

    private ArrayList<Category> categories;
    private Context context;

    public CategoriesAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_list_item,parent,false);
        return new CustomAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter holder, int position) {
        if(categories != null){
            holder.textView.setText(categories.get(position).getName());
            if(categories.get(position).getImageUrl() != null){
                /*Glide.with(context)
                        .load(categories.get(position).getImageUrl())
                        .into(holder.imageView);*/
            }
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CustomAdapter extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public CustomAdapter(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mainCategoryImage);
            textView = itemView.findViewById(R.id.mainCategoryName);
        }
    }
}
