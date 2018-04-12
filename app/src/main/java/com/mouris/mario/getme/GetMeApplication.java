package com.mouris.mario.getme;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;

public class GetMeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //To enable Firebase offline capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Facebook app logger
        AppEventsLogger.activateApp(this);

        //To support CompatVectorDrawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
