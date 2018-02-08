package com.zxventures.beer.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by joaopmmachete on 9/2/17.
 *
 * Represents the Shared Preferences class to Store private primitive data in
 * key-value pairs.
 */

public class Settings {

    /**
     * The pref file name.
     */
    private final String pref_file_name = "PrefFile";

    /**
     * The settings.
     */
    private final SharedPreferences mSettings;

    /**
     * Instantiates a new settings.
     *
     * @param ctx the ctx
     */
    public Settings(final Context ctx) {
        this.mSettings = ctx.getSharedPreferences(this.pref_file_name,
                Context.MODE_PRIVATE);
    }
}
