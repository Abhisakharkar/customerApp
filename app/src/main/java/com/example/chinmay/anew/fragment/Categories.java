package com.example.chinmay.anew.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chinmay.anew.AddCategoryAct;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.RecyclerItemClickListener;
import com.example.chinmay.anew.adapter.CategoriesAdapter;
import com.example.chinmay.anew.adapter.ExpandableListAdapter;
import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.viewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Categories extends Fragment {

    private RecyclerView recyclerView;
    private ExpandableListView expListView;
    private LinearLayout lvlOne;
    private CardView lvlTwo;

    private int lvlOnePos = 0;
    private int lvlTwoPos = 0;

    private ExpandableListAdapter listAdapter;

    private ArrayList<Category> lvlOneArrayList;
    private ArrayList<Category> fullArrayList;

    private List<Category> listDataHeader;
    private HashMap<Category, List<Category>> listDataChild;

    public Categories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recylcerViewMainCat);
        expListView = view.findViewById(R.id.recylcerViewSecCat);
        lvlOne = view.findViewById(R.id.mainCategoryAddCat);
        lvlTwo = view.findViewById(R.id.addCatLvlTwo);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        findCategoryAtLvlOne();
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        lvlOnePos = position;
                        findChildAtLvlTwo(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                lvlTwoPos = groupPosition;
                return false;
            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                lvlTwoPos = groupPosition;
                return false;
            }
        });

        lvlTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCategoryAct.class);
                intent.putExtra("PARENT_ID",listDataHeader.get(lvlTwoPos).getId());
                startActivity(intent);
            }
        });

        lvlOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCategoryAct.class);
                intent.putExtra("PARENT_ID",lvlOneArrayList.get(lvlOnePos).getId());
                startActivity(intent);
            }
        });

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
                    recyclerView.setAdapter(categoriesAdapter);
                    lvlOneArrayList = arrayList;
                    fullArrayList = categoryArrayList;
                    findChildAtLvlTwo(0);
                }
            }
        });
    }

    private void findChildAtLvlTwo(int pos){
        listDataHeader = new ArrayList<Category>();
        ArrayList<Integer> childId = lvlOneArrayList.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<fullArrayList.size();j++){
                    try{
                        if(Integer.parseInt(fullArrayList.get(j).getId()) == childId.get(i)){
                            listDataHeader.add(fullArrayList.get(j));
                        }
                    }catch (Exception e){

                    }
                }
            }
            for(int i=0;i<listDataHeader.size();i++) {
                findChildAtLvlThree(i, listDataHeader);
            }
        }catch (Exception e){

        }

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setIndicatorBounds(expListView.getRight()- 120, expListView.getWidth());
        expListView.setAdapter(listAdapter);
    }

    private void findChildAtLvlThree(int pos, List<Category> categoryArrayList){
        listDataChild = new HashMap<Category, List<Category>>();
        ArrayList<Category> arrayList = new ArrayList<>();
        arrayList.clear();
        ArrayList<Integer> childId = categoryArrayList.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<fullArrayList.size();j++){
                    try{
                        if(Integer.parseInt(fullArrayList.get(j).getId()) == childId.get(i)){
                            arrayList.add(fullArrayList.get(j));
                            break;
                        }
                    }catch (Exception e){

                    }
                }
            }
            listDataChild.put(categoryArrayList.get(pos),arrayList);
        }catch (Exception e){

        }
    }


}