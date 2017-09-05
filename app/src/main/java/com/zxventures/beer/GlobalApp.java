package com.zxventures.beer;

import android.app.Application;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;
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

    /**
     * The settings.
     */
    public Settings mSettings;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        mInstance = this;
        mSettings = new Settings(this);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized GlobalApp getInstance() {
        return mInstance;
    }
}
