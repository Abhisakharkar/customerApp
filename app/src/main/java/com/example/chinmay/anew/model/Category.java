package com.example.chinmay.anew.model;

import java.util.ArrayList;

public class Category {

    private String name;
    private String imageUrl;
    private String level;
    private String id;
    private ArrayList<Integer> children;

    public Category(){

    }

    public Category(String name, String imageUrl, String level, String id, ArrayList<Integer> children) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.level = level;
        this.id = id;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getChildren() {
        return children;
    }
}
