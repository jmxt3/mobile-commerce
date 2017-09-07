package com.zxventures.beer.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zxventures.beer.GlobalApp;
import com.zxventures.beer.R;
import com.zxventures.beer.adapters.DataAdapter;
import com.zxventures.beer.models.ProductModel;
import com.zxventures.beer.utils.ImageUtils;
import com.zxventures.beer.utils.Log;

import java.util.List;

public class ProductDetailActivity extends GlobalActivity {

    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    private static final String FAKE_IMG_URL = "http://wwww.google.com/img.jpg";

    private List<ProductModel> data;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.position = bundle.getInt(DataAdapter.ViewHolder.ARG_POSITION);
            //re-construct object from string
            this.data = GlobalApp.getInstance().getGson().fromJson(bundle.getString(DataAdapter.ViewHolder.ARG_PRODUCTS),
                    new TypeToken<List<ProductModel>>() {
                    }.getType());
        }

        initToolbar();

        ImageView img = findViewById(R.id.img);
        TextView title= findViewById(R.id.title);
        title.setText(data.get(position).title);
        TextView price= findViewById(R.id.price);
        price.setText(data.get(position).price);
        TextView description= findViewById(R.id.description);
        description.setText(getString(R.string.loren_ipsun));

        final int imgSize = ImageUtils.dpToPx(getResources(), (int) getResources().getDimension(R.dimen.thumbnail_image));

        try {

            String url = TextUtils.isEmpty(data.get(position).imageUrl) ?
                    FAKE_IMG_URL:data.get(position).imageUrl;

            Picasso.with(this).invalidate(url);
            Picasso.with(this)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(imgSize, imgSize)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_sync)
                    .error(R.mipmap.ic_avatar)
                    .into(img);
        } catch (final NullPointerException e) {
            Log.e(TAG, NullPointerException.class
                    .getSimpleName(), e);
        }

    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.actionbar_product_details_activity_title));
        } catch (NullPointerException e) {
            Log.e(TAG, "initToolbar", e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
