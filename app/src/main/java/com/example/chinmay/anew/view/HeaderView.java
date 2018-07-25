package com.example.chinmay.anew.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chinmay.anew.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

@Parent
@SingleTop
@Layout(R.layout.lvl_one_category_list_item)
public class HeaderView {

    @View(R.id.secCategoryName)
    TextView headerText;
    @View(R.id.secCategoryImage)
    ImageView imageView;
    @View(R.id.expandCollapseImage)
    ImageView expColl;

    private Context mContext;
    private String mHeaderText;
    private String imageUrl;

    public HeaderView(Context context,String headerText,String imageUrl) {
        this.mContext = context;
        this.mHeaderText = headerText;
        this.imageUrl = imageUrl;
    }

    @Resolve
    private void onResolve(){
        headerText.setText(mHeaderText);
       // Glide.with(mContext).load(imageUrl).into(imageView);
    }

    @Expand
    private void onExpand(){
        expColl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
    }
    @Collapse
    private void onCollapse(){
        expColl.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
    }
}
