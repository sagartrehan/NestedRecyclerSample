package com.ecommerce;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class AppManager extends Application {

    private static AppModels sAppModels;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize App Models
        sAppModels = new AppModels();
        // Initialize context
        sContext = this;
        // Initialize Stetho
        initStetho();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Cleanup
        sAppModels = null;
        sContext = null;
    }

    public static AppModels getModels() {
        return sAppModels;
    }

    public static Context getContext() {
        return sContext;
    }

    private void initStetho() {
        // Initialize if this is debug build.
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
