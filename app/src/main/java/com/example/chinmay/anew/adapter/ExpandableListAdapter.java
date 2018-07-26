package com.example.chinmay.anew.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Category> _listDataHeader = new ArrayList<>();
    private HashMap<Category, List<Category>> _listDataChild = new HashMap<>();

    public ExpandableListAdapter(Context _context, List<Category> _listDataHeader, HashMap<Category, List<Category>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        try{
            if(_listDataChild != null && _listDataChild.size() != 0 && _listDataHeader != null && _listDataHeader.size() != 0){
                return this._listDataChild.get(this._listDataHeader.get(i)).size();
            }else{
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this._listDataChild.get(this._listDataHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            Category category =  (Category) getGroup(i);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.lvl_one_category_list_item, null);
            }

            if(category != null){
                TextView lblListHeader = (TextView) view.findViewById(R.id.secCategoryName);
                ImageView imageView = view.findViewById(R.id.secCategoryImage);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText(category.getName());
                if(category.getImageUrl() != null){
                    Glide.with(_context).load(category.getImageUrl()).into(imageView);
                }
            }
            return view;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Category category = (Category) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.last_level_category_list_item, null);
        }

        if(category != null){
            TextView txtListChild = (TextView) convertView.findViewById(R.id.lastCategoryName);
            ImageView imageView = convertView.findViewById(R.id.lastCategoryImage);
            txtListChild.setText(category.getName());
            if(category.getImageUrl() != null){
                Glide.with(_context).load(category.getImageUrl()).into(imageView);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
