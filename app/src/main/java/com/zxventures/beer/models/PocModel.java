package com.zxventures.beer.models;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by joaopmmachete on 9/6/17.
 */

public class PocModel implements Serializable{

    @SerializedName("id")
    public int id;

    @SerializedName("tradingName")
    public String tradingName;

    @SerializedName("full_address")
    public String full_address;

    @SerializedName("address")
    public String address;

    @SerializedName("number")
    public String number;

    @SerializedName("city")
    public String city;

    @SerializedName("province")
    public String province;

    @SerializedName("zip")
    public String zip;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lgt")
    public double lgt;

    @SerializedName("categories")
    public SparseArray<CategoryModel> categories;

    public PocModel() {
        this.categories = new SparseArray<>();
    }

    @Override
    public String toString() {
        return "PocModel{" +
                "id=" + id +
                ", tradingName='" + tradingName + '\'' +
                ", full_address='" + full_address + '\'' +
                ", address='" + address + '\'' +
                ", number='" + number + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", zip='" + zip + '\'' +
                ", lat=" + lat +
                ", lgt=" + lgt +
                ", categories=" + categories +
                '}';
    }
}
