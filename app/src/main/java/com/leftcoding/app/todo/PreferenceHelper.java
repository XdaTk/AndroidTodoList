package com.leftcoding.app.todo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SnowFlake on 30.01.2016.
 * Saves the value of the checkbox. Helps to show SplashScreen.
 */
public class PreferenceHelper {

    private static PreferenceHelper sInstance;

    private SharedPreferences preferences;

    private PreferenceHelper() {

    }

    static PreferenceHelper getInstance() {
        if (sInstance ==null) {
            sInstance = new PreferenceHelper();
        }
        return sInstance;
    }

    void init(Context context) {
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }
    void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}
