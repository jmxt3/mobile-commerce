/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxventures.beer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zxventures.beer.GlobalApp;
import com.zxventures.beer.R;
import com.zxventures.beer.adapters.DataAdapter;
import com.zxventures.beer.models.ProductModel;

import java.util.List;

/**
 * Created by joaopmmachete on 9/5/17.
 */

public class TabFragment extends Fragment {

    private static final String TAG = TabFragment.class.getSimpleName();
    private static final String ARG_POSITION = "position";
    public static final String ARG_PRODUCTS = "products";

    private List<ProductModel> products;
    private int position;

    public static TabFragment newInstance(int position, List<ProductModel> products) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PRODUCTS, GlobalApp.getInstance().getGson().toJson(products));
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the framework to try to keep this fragment around
        // during a configuration change.
        setRetainInstance(true);

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.position = bundle.getInt(ARG_POSITION);
            //re-construct object from string
            this.products = GlobalApp.getInstance().getGson().fromJson(bundle.getString(ARG_PRODUCTS),
                    new TypeToken<List<ProductModel>>() {
                    }.getType());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate a diferent layout if Featured Tab
        int layoutToInflate = this.position == 0 ? R.layout.fragment_featured_tab : R.layout.fragment_tab;
        return inflater.inflate(layoutToInflate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.grid);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        DataAdapter adapter = new DataAdapter(getActivity().getApplicationContext(), this.products);
        recyclerView.setAdapter(adapter);

    }
}
