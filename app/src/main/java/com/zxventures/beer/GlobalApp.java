package com.zxventures.beer;

import android.app.Application;

import com.zxventures.beer.settings.Settings;

/**
 * Created by joaopmmachete on 9/1/17.
 */

public class GlobalApp extends Application {

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static GlobalApp mInstance;


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
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized GlobalApp getInstance() {
        return mInstance;
    }
}
