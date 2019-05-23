package it.univaq.mobileprogramming.myweather.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    public static final String FIRST_TIME = "first_time"; // Used to remember if is the first time that the user open the app
    public static final String CHECK_LOCATION = "check_location"; // Used to switch on the location service and vice versa
    public static final String CHECK_NOTIFY = "check_notify"; // Used to switch on the notify service and vice versa
    public static final String SWITCH_LOCATION = "switch_location"; // Used to switch from LocationManager to GoogleService and vice versa
    public static final String SWITCH_BACKGROUND = "switch_background"; // Used to switch from LocationManager to GoogleService and vice versa
    public static final String LATITUDE = "latitude"; // Used to save the last location knowed
    public static final String LONGITUDE = "longitude"; // Used to save the last location knowed


    public static void save(Context context, String key, String value){

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean loadBoolean(Context context, String key, boolean fallback){

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, fallback);
    }

    public static String loadString(Context context, String key, String fallback){

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getString(key, fallback);
    }
}
