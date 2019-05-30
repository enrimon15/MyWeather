package it.univaq.mobileprogramming.myweather;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.univaq.mobileprogramming.myweather.json.ParsingSearch;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** parsing list city world json (per ricerca) **/
        if (ParsingSearch.getInstance(getApplicationContext()).getList().isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ParsingSearch.getInstance(getApplicationContext()).loadJson();
                }
            }).start();
        }


        setContentView(R.layout.activity_splash_screen);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent(SplashScreen.this, AroundMeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
    }
}
