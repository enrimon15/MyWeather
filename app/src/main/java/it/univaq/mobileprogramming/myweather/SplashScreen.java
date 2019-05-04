package it.univaq.mobileprogramming.myweather;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.model.CitySearch;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds
    private static List<CitySearch> citySuggestions = new ArrayList<>();
    private static final String CITIES_FILE_NAME = "city_list.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                citySuggestions=loadJson(SplashScreen.this);
            }
        }).start();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent(SplashScreen.this, AroundMeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


    private static List<CitySearch> loadJson (Context context){
        List<CitySearch> cityList = new ArrayList<>();

        try {
            InputStream is = context.getAssets().open(CITIES_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginArray();

            Gson gson = new GsonBuilder().create();

            while (reader.hasNext()) {

                CitySearch cityJson = gson.fromJson(reader, CitySearch.class);
                cityList.add(cityJson);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return cityList;
    }

    public static List<CitySearch> getList () {
        return citySuggestions;
    }
}
