package com.example.chinmay.anew.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.model.Category;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import de.hdodenhof.circleimageview.CircleImageView;

@Layout(R.layout.last_level_category_list_item)
public class ChildView {

    @View(R.id.lastCategoryName)
    TextView textViewChild;
    @View(R.id.lastCategoryImage)
    CircleImageView childImage;

    private Context mContext;
    private Category category;
    public ChildView(Context mContext, Category category) {
        this.mContext = mContext;
        this.category = category;
    }

    @Resolve
    private void onResolve(){
        textViewChild.setText(category.getName());
       // Glide.with(mContext).load(category.getImageUrl()).into(childImage);
    }
}

