package com.zxventures.beer.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by joaopmmachete on 9/6/17.
 */

public class ProductModel {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("imageUrl")
    public String imageUrl;

    @SerializedName("price")
    public String price;

    public ProductModel(int id, String title, String imageUrl, String price) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
