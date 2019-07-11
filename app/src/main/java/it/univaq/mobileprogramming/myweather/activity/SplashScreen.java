package it.univaq.mobileprogramming.myweather.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.json.ParsingSearch;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //ContextCompat.getDrawable(this, getResources().getIdentifier("01n", "drawable", getPackageName()));

        /** parsing list city world json (per ricerca) **/
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter("completed"));

        if (ParsingSearch.getInstance(getApplicationContext()).getList().isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ParsingSearch.getInstance(getApplicationContext()).loadJson();

                    Intent intent = new Intent("completed");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
            }).start();
        } else {
            Intent intent = new Intent("completed");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent(SplashScreen.this, AroundMeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    };
}
