package com.example.chinmay.anew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Belal on 1/23/2018.
 */

public class RetailerListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapterRecyclerMainAc madapter;
    private TextView retail;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.retailerlist, null);
        retail=(TextView)view.findViewById(R.id.retail);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        madapter = new MyAdapterRecyclerMainAc(getActivity());

        mRecyclerView.setAdapter(madapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );
        if(ServerOp.retailersArray.size()==0)
        {
            retail.setText("No retailers found near you");
        }


        return view;

        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Bind your views.

    }

}