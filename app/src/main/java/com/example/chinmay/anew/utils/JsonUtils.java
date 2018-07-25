package com.example.chinmay.anew.utils;

import com.example.chinmay.anew.model.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<Category> parseCategoryJson(String json) {
        if (json != null) {
            ArrayList<Category> category = new ArrayList<>();
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONArray itemsArray = jsonObj.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonobject = itemsArray.getJSONObject(i);
                    String name = jsonobject.getString("name");
                    String url = "";
                    String id = jsonobject.getString("id");
                    String level = jsonobject.getString("level");
                    String child = jsonobject.getString("children");
                    ArrayList<Integer> children = new ArrayList<>();
                    for(int j=0;j<child.length();j++){
                        try {
                            int num = Integer.parseInt(child.substring(j,j+1));
                            children.add(num);
                        }catch (Exception e){

                        }
                    }
                    category.add(new Category(name,url,level,id,children));
                }
            } catch (Exception e) {
                return null;
            }

            return category;
        }else{
            return null;
        }
    }

}
