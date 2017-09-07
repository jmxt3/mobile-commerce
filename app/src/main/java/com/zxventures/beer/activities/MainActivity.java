package com.zxventures.beer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.zxventures.beer.AllCategoriesSearchQuery.AllCategory;
import com.zxventures.beer.GlobalApp;
import com.zxventures.beer.PocCategorySearchQuery;
import com.zxventures.beer.PocCategorySearchQuery.Product;
import com.zxventures.beer.PocSearchMethodQuery;
import com.zxventures.beer.PocSearchMethodQuery.PocSearch;
import com.zxventures.beer.R;
import com.zxventures.beer.models.CategoryModel;
import com.zxventures.beer.models.PocModel;
import com.zxventures.beer.models.ProductModel;
import com.zxventures.beer.utils.DateUtils;
import com.zxventures.beer.utils.Log;

import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity extends GlobalActivity implements PlaceSelectionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PocModel pocModel = new PocModel();
    private int processedCounter = 0;
    private ProgressBar mProgressBar;
    private TextView mProgressLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressLabel = (TextView) findViewById(R.id.progress_label);

        initPlaceAutoCompleteFragment();
    }

    private void initPlaceAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(this);
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
        Log.i(TAG, "Place: " + place.getName() +
                "\nAddress: " + place.getAddress().toString() +
                "\nLat: " + place.getLatLng().latitude +
                "\nLgt: " + place.getLatLng().longitude);

        fetchPoc(place);

    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "Ocorreu um erro: " + status);

        Toast.makeText(MainActivity.this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private void fetchProductsInCategory(int pocIdToQuery, int categoryIdToQuery) {

        processedCounter++;

        Log.d(TAG, "PocCategorySearchQuery started");
        Log.d(TAG, "pocIdToQuery =" + pocIdToQuery + "\ncategoryIdToQuery=" + categoryIdToQuery);
        final PocCategorySearchQuery productQuery = PocCategorySearchQuery.builder()
                .id(String.valueOf(pocIdToQuery))
                .search("")
                .categoryId(categoryIdToQuery) //TODO: BUG FOUND ON THE API, CATEGORIES NOT BEING FILTERING CORRETLY
                .build();

        ApolloCall<PocCategorySearchQuery.Data> productCall = GlobalApp.getInstance().apolloClient()
                .query(productQuery);
        productCall.enqueue(new ApolloCall.Callback<PocCategorySearchQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<PocCategorySearchQuery.Data> response) {
                Log.d(TAG, " fetchProductsInCategory onResponse start");

                try {
                    if (response.data().poc() != null && !response.data().poc().products().isEmpty()) {

                        final List<Product> responseProductsList = response.data().poc().products();
                        for (Product product : responseProductsList) {
                            final CategoryModel temp = pocModel.categories.get(categoryIdToQuery);
                            if (temp != null) {
                                temp.products.add(new ProductModel(
                                        Integer.valueOf(product.id()),
                                        product.title(),
                                        product.productVariants().get(0).imageUrl(),
                                        String.valueOf(product.productVariants().get(0).price())));
                            } else {
                                Log.e(TAG, "The Category ID " + categoryIdToQuery + " doesn't exist in our model");
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "NullPointerException", e);
                }

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                Log.d(TAG, "fetchProductsInCategory Completed");

                processedCounter--;

                if (processedCounter <= 0) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "Address: " + pocModel.full_address +
                                        "\nLat: " + pocModel.lat +
                                        "\nLgt: " + pocModel.lgt,
                                Toast.LENGTH_SHORT).show();

                        GlobalApp.getInstance().bus().send(pocModel);

                        startActivity(new Intent(MainActivity.this, ProductsListActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        });
    }

    private void fetchCategories() {
        Log.d(TAG, "AllCategoriesSearchQuery started");
        final AllCategoriesSearchQuery categoryQuery = AllCategoriesSearchQuery.builder()
                .build();

        final ApolloCall<AllCategoriesSearchQuery.Data> categoriesCall = GlobalApp.getInstance().apolloClient()
                .query(categoryQuery);
        categoriesCall.enqueue(new ApolloCall.Callback<AllCategoriesSearchQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AllCategoriesSearchQuery.Data> response) {
                Log.d(TAG, "fetchCategories onResponse start");

                try {
                    if (!response.data().allCategory().isEmpty()) {

                        final List<AllCategory> responseList = response.data().allCategory();

                        //add a default category to our POCModel
                        pocModel.categories.put(0, new CategoryModel(0, "Featured"));

                        //fetch all products to our default category
                        fetchProductsInCategory(pocModel.id, 0);

                        for (int i = 0; i < responseList.size(); i++) {

                            //add the next category to our POCModel
                            final int id = Integer.valueOf(responseList.get(i).id());
                            pocModel.categories.put(id, new CategoryModel(
                                    id, responseList.get(i).title()));

                            //Fetch the products into that category
                            fetchProductsInCategory(pocModel.id, Integer.valueOf(responseList.get(i).id()));
                        }

                    }

                } catch (NullPointerException e) {
                    Log.e(TAG, "NullPointerException", e);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                Log.d(TAG, "fetchCategories Completed");
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        });

    }

    private void fetchPoc(final Place place) {

        pocModel = new PocModel();
        showProgress(true);

        final PocSearchMethodQuery pocQuery = PocSearchMethodQuery.builder()
                .algorithm("NEAREST")
                .lat(String.valueOf(place.getLatLng().latitude))
                .lgt(String.valueOf(place.getLatLng().longitude))
                .now(DateUtils.getCurrentDate())
                .build();

        GlobalApp.getInstance().apolloClient()
                .query(pocQuery)
                .enqueue(new ApolloCall.Callback<PocSearchMethodQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<PocSearchMethodQuery.Data> response) {

                        try {
                            if (!response.data().pocSearch().isEmpty()) {

                                //List of POCs
                                final List<PocSearch> responseList = response.data().pocSearch();

                                //Get the first Poc available from the list
                                for (int i = 0; i < responseList.size(); i++) {
                                    if (responseList.get(i).status().equals(com.zxventures.beer.type.Status.AVAILABLE)) {
                                        final PocSearch availablePOC = responseList.get(i);

                                        //Create our Poc Model
                                        pocModel.id = Integer.valueOf(availablePOC.id());
                                        pocModel.tradingName = availablePOC.tradingName();
                                        pocModel.full_address = place.getAddress().toString();
                                        pocModel.address = availablePOC.address().address1();
                                        pocModel.number = availablePOC.address().number();
                                        pocModel.city = availablePOC.address().city();
                                        pocModel.province = availablePOC.address().province();
                                        pocModel.zip = availablePOC.address().zip();
                                        pocModel.lat = place.getLatLng().latitude;
                                        pocModel.lgt = place.getLatLng().longitude;

                                        fetchCategories();

                                        break;
                                    }
                                }
                            } else {

                                MainActivity.this.runOnUiThread(() -> {
                                    showProgress(false);
                                    Toast.makeText(MainActivity.this, getString(R.string.poc_not_found_error),
                                            Toast.LENGTH_LONG).show();
                                });
                            }

                        } catch (NullPointerException e) {
                            Log.e(TAG, "NullPointerException", e);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        Log.d(TAG, "fetchPoc Completed");
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                });
    }

    public void showProgress(final boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressLabel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}