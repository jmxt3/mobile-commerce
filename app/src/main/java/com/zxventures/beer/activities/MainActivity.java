package com.zxventures.beer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.zxventures.beer.AllCategoriesSearchQuery;
import com.zxventures.beer.GlobalApp;
import com.zxventures.beer.R;
import com.zxventures.beer.utils.Log;

import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    Handler uiHandler = new Handler(Looper.getMainLooper());
    ApolloCall<AllCategoriesSearchQuery.Data> categoriesCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        fetchCategories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place: " + place.getName());
        Log.i(TAG, "Lat: " + place.getLatLng().latitude);
        Log.i(TAG, "Lgt: " + place.getLatLng().longitude);

        Toast.makeText(MainActivity.this, "Lat: " + place.getLatLng().latitude + "\nLgt: " + place.getLatLng().longitude,
                Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this,
                ProductsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "Ocorreu um erro: " + status);

        Toast.makeText(MainActivity.this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    ApolloCall.Callback<AllCategoriesSearchQuery.Data> dataCallback
            = new ApolloCall.Callback<AllCategoriesSearchQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<AllCategoriesSearchQuery.Data> response) {
            Log.d(TAG, "onResponse start");
            try {
                final List<AllCategoriesSearchQuery.AllCategory> list = response.data().allCategory();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Log.i(TAG, list.get(i).title());
                    }
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "onResponse error", e);
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    };

    private void fetchCategories() {
        Log.d(TAG, "fetchCategories");
        final AllCategoriesSearchQuery feedQuery = AllCategoriesSearchQuery.builder()
                .build();

        categoriesCall = GlobalApp.getInstance().apolloClient()
                .query(feedQuery);
        categoriesCall.enqueue(dataCallback);

    }

}