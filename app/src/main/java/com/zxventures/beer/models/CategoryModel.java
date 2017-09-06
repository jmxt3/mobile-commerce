package com.zxventures.beer.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaopmmachete on 9/6/17.
 */

public class CategoryModel {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("products")
    public List<ProductModel> products;

    public CategoryModel(int id, String title) {
        this.id = id;
        this.title = title;
        products = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", products=" + products +
                '}';
    }
}
