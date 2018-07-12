package com.example.chinmay.anew;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private final LayoutInflater mInflater;
    private ArrayList<MyGooglePlaces> resultList;
    private Context mContext;
    private int mResource;
    private PlaceAPI mPlaceAPI = new PlaceAPI();

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position).getAddress();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       MyViewHolder holder;
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.autocomplete, null);
            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new MyViewHolder(convertView);
            holder.text = (TextView) convertView.findViewById(R.id.myplaces);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            // Bind the data efficiently with the holder.
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (MyViewHolder) convertView.getTag();
        }
        // If weren't re-ordering this you could rely on what you set last time
        if(resultList.size()>0) {
            holder.text.setText(Html.fromHtml("<b>" + resultList.get(position).getName() + "<b>"));
            holder.address.setText(resultList.get(position).getAddress());
        }
        return convertView;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView address;
        public ImageView img1;
        public TextView enterpriseName;
        public TextView directions;


        public MyViewHolder(View v) {
            super(v);

        }

    }
}
