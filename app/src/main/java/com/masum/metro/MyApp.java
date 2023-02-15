package com.masum.metro;

import android.app.Application;


import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("ad5e4097-213f-4d4e-abf7-c0bedebe3a8c");

        CaocConfig.Builder.create()
                .errorActivity(CustomErrorActivity.class)
                //Customizes what to do when the app crashes while it is in background. Possible values:
                //BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: launch the error activity when the app is in background,
                //BackgroundMode.BACKGROUND_MODE_CRASH: launch the default system error when the app is in background,
                //BackgroundMode.BACKGROUND_MODE_SILENT: crash silently when the app is in background,
//                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                //This disables the interception of crashes. Use it to disable CustomActivityOnCrash (for example, depending on your buildType).
//                .enabled(false)
                //This hides the "error details" button in the error activity, thus hiding the stack trace
//                .showErrorDetails(false)
                //This avoids the app from using the "Restart app" button and displaying a "Close app" button directly.
                //Even with restart app enabled, the Close app can still be displayed if your app has no launch activity.
//                .showRestartButton(false)
                //This makes the library track the activites visited by the user and their lifecycle calls.
                //Use it if you want that info in the error details screen shown on the error activity.
//                .trackActivities(true)
                //This hides the additional log shown when the error activity is launched.
                //It is shown by default because the Android Studio Logcat view by default only shows
                //the current process output, and this makes the stack trace more obvious to find.
//                .logErrorOnRestart(false)
                //Defines the time that must pass between app crashes to determine that we are not in a crash loop.
                //If a crash has occurred less that this time ago, the error activity will not be launched
                //and the system crash screen will be invoked.
//                .minTimeBetweenCrashesMs(2000)
                //This shows a different image on the error activity, instead of the default upside-down bug.
                //You may use a drawable or a mipmap.
//                .errorDrawable(R.mipmap.ic_launcher)
                //This sets the restart activity.
                //If you set this, this will be used. However, you can also set it with an intent-filter:
                //  <action android:name="cat.ereza.customactivityoncrash.RESTART" />
                //If none are set, the default launch activity will be used.
//                .restartActivity(MainActivity.class)
                //This sets a custom error activity class instead of the default one.
                //If you set this, this will be used. However, you can also set it with an intent-filter:
                //  <action android:name="cat.ereza.customactivityoncrash.ERROR" />
                //If none are set, the default launch activity will be used.
                //Comment it (and disable the intent filter) to see the customization effects on the default error activity.
                //Uncomment to use the custom error activity
//                .errorActivity(CustomErrorActivity.class)
                //This sets a EventListener to be notified of events regarding the error activity,
                //so you can, for example, report them to Google Analytics.
//                .eventListener(new MyCustomEventListener())
                //This sets a CustomCrashDataCollector that is invoked when a crash occurs. This allows you to add more
                //data to the error details.
//                .customCrashDataCollector(new MyCustomCrashDataCollector())
                .apply();

    }
}
