package com.example.chinmay.anew.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chinmay.anew.R;
import com.example.chinmay.anew.RecyclerItemClickListener;
import com.example.chinmay.anew.adapter.CategoriesAdapter;
import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.view.ChildView;
import com.example.chinmay.anew.view.HeaderView;
import com.example.chinmay.anew.viewModel.CategoryViewModel;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;

public class Categories extends Fragment {

    private RecyclerView recyclerView1;
    private ExpandablePlaceHolderView recyclerView2;

    private ArrayList<Category> lvlOneArrayList;
    private ArrayList<Category> fullArrayList;

    public Categories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView1 = view.findViewById(R.id.recylcerViewMainCat);
        recyclerView2 = view.findViewById(R.id.recylcerViewSecCat);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        findCategoryAtLvlOne();
        recyclerView1.setHasFixedSize(true);

        recyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView1,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        findChildAtLvlTwo(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );
    }

    private void findCategoryAtLvlOne(){
        CategoryViewModel categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
        categoryViewModel.getCategory(getActivity()).observe(getActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Category> categoryArrayList) {
                if(categoryArrayList != null){
                    ArrayList<Category> arrayList = new ArrayList<>();
                    for(int i=0;i<categoryArrayList.size();i++){
                        if(categoryArrayList.get(i).getLevel().equalsIgnoreCase("1")){
                            arrayList.add(categoryArrayList.get(i));
                        }
                    }
                    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(arrayList,getActivity());
                    recyclerView1.setAdapter(categoriesAdapter);
                    lvlOneArrayList = arrayList;
                    fullArrayList = categoryArrayList;
                    findChildAtLvlTwo(0);
                }
            }
        });
    }

    private void findChildAtLvlTwo(int pos){
        ArrayList<Category> arrayList = new ArrayList<>();
        arrayList.clear();
        ArrayList<Integer> childId = lvlOneArrayList.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<fullArrayList.size();j++){
                    try{
                        if(Integer.parseInt(fullArrayList.get(j).getId()) == childId.get(i)){
                            arrayList.add(fullArrayList.get(j));
                        }
                    }catch (Exception e){

                    }
                }
            }

            for(int i=0;i<arrayList.size();i++){
                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                recyclerView2.addView(new HeaderView(getActivity(), arrayList.get(i).getName(),arrayList.get(i).getImageUrl()));
                recyclerView2.notify();
                findChildAtLvlThree(i,arrayList);
            }

        }catch (Exception e){

        }
    }

    private void findChildAtLvlThree(int pos, ArrayList<Category> categoryArrayList){
        ArrayList<Category> arrayList = new ArrayList<>();
        arrayList.clear();
        ArrayList<Integer> childId = categoryArrayList.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<fullArrayList.size();j++){
                    try{
                        if(Integer.parseInt(fullArrayList.get(j).getId()) == childId.get(i)){
                            arrayList.add(fullArrayList.get(j));
                        }
                    }catch (Exception e){

                    }
                }
            }
            for(int i=0;i<arrayList.size();i++){
                recyclerView2.addView(new ChildView(getActivity(),arrayList.get(i)));
            }

        }catch (Exception e){

        }
    }

}