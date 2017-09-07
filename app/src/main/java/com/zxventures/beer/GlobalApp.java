package com.zxventures.beer;

import android.app.Application;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.zxventures.beer.helpers.RxBus;
import com.zxventures.beer.settings.Settings;

import okhttp3.OkHttpClient;

/**
 * Created by joaopmmachete on 9/1/17.
 */

public class GlobalApp extends Application {

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static GlobalApp mInstance;
    private static final String BASE_URL = "https://803votn6w7.execute-api.us-west-2.amazonaws.com/dev/public/graphql";
    private ApolloClient apolloClient;
    public Settings mSettings;
    private Gson gson;
    private RxBus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        mInstance = this;
        mSettings = new Settings(this);
        gson = new Gson();
        bus = new RxBus();
        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(new OkHttpClient.Builder().build())
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

    public Gson getGson() {
        return gson;
    }

    public RxBus bus() {
        return bus;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized GlobalApp getInstance() {
        return mInstance;
    }
}
