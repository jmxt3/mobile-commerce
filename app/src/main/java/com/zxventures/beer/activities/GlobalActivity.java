package com.zxventures.beer.activities;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by joaopmmachete on 9/7/17.
 */

public class GlobalActivity extends AppCompatActivity {

    public final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // do not send event after activity has been destroyed
        disposables.clear();
    }
}
