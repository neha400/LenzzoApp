package com.lenzzo.localization;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.lenzzo.pushnotification.OneSignalNotificationOpenedHandler;
import com.lenzzo.pushnotification.OneSignalNotificationReceivedHandler;
import com.onesignal.OneSignal;

public class App extends Application {

    private final String TAG = "App";

    /*For language and multidex*/

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler(this))
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler(this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        //MultiDex.install(this);
        Log.d(TAG, "attachBaseContext");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}