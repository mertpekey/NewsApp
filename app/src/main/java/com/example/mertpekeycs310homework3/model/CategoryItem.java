package com.example.mertpekeycs310homework3.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem {

    private int id;
    private String title;
    private String text;
    private String category;

    public CategoryItem() {
    }

    public CategoryItem(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }


}
