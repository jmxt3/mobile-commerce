package com.zxventures.beer.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;
import com.zxventures.beer.GlobalApp;
import com.zxventures.beer.R;
import com.zxventures.beer.fragments.TabFragment;
import com.zxventures.beer.models.PocModel;
import com.zxventures.beer.utils.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductsListActivity extends GlobalActivity {

    private static final String TAG = ProductsListActivity.class.getSimpleName();

    private ViewPager pager;
    private MyPagerAdapter adapter;
    private PocModel pocModel = new PocModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        disposables.add(GlobalApp.getInstance()
                .bus()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {

                    if (object instanceof PocModel) {
                        pocModel = (PocModel) object;
                        Log.w(TAG, "PocModel Received - "+pocModel.full_address);

                        initToolbar();
                        adapter = new MyPagerAdapter(getSupportFragmentManager());
                        initPager();
                        initTabs();
                    }

                }));
    }

    private void initPager() {
        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
    }

    private void initTabs() {
        PagerSlidingTabStrip tabs = findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setIndicatorColor(getColor(R.color.colorAccent));
        tabs.setTextColor(Color.WHITE);
        tabs.setDividerColor(Color.WHITE);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(pocModel.tradingName);
        } catch (NullPointerException e) {
            Log.e(TAG, "initToolbar", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
                int key = pocModel.categories.keyAt(position);
            return pocModel.categories.get(key).title;
        }

        @Override
        public int getCount() {
            return pocModel.categories.size();
        }

        @Override
        public Fragment getItem(int position) {
            int key = pocModel.categories.keyAt(position);
            return TabFragment.newInstance(position, pocModel.categories.get(key).products);
        }

    }
}
