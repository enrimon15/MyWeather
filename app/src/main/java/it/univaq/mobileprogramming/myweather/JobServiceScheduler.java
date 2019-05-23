package it.univaq.mobileprogramming.myweather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.List;

import it.univaq.mobileprogramming.myweather.Location.LocationGoogleService;
import it.univaq.mobileprogramming.myweather.Settings.Settings;
import it.univaq.mobileprogramming.myweather.database.AroundDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobServiceScheduler extends JobService {
private boolean jobCancelled = false;
private static final String TAG = "JOBSERVICE";
private static final int NOTIFICATION_ID = 55553;
public static final String FILTER_REQUEST_DOWNLOAD = "filter_request_download";
private String lat;
private String lon;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job: started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!(Settings.loadBoolean(getApplicationContext(), Settings.SWITCH_BACKGROUND, true))) {return;}
                //if (jobCancelled) return;
                Log.d(TAG, "pre work ");

                lat = Settings.loadString(getApplicationContext(), Settings.LATITUDE, "");
                lon = Settings.loadString(getApplicationContext(), Settings.LONGITUDE, "");
                Log.d(TAG, lat);
                Log.d(TAG, lon);

                VolleyRequest.getInstance(getApplicationContext())
                        .downloadBackground(lat,lon, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {
                                if (response.length() > 0) {
                                    try {
                                        ParsingAround pars = new ParsingAround(response);
                                        clearDataFromDB();
                                        saveDataInDB(pars.getAround());
                                        Log.d("schedulerrr", "fatto");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                /*Intent intent = new Intent(FILTER_REQUEST_DOWNLOAD);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);*/

                Log.d(TAG, "post work ");

                jobFinished(params,false);
            }
        }).start();
    }

    private void saveDataInDB(final List<ListCity> city){

        // Save by RoomDatabase
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ListCity l : city){
                    AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().save(l);
                    Log.d("schedulerrr", "datadb ");
                }
                notifyMess("Database Aggiornato: " + lat + ", " + lon);
            }
        }).start();
    }

    /** Clear data from database */
    private void clearDataFromDB(){
        // Delete by RoomDatabase
        new Thread(new Runnable() {
            @Override
            public void run() {
                AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().deleteAll();
            }
        }).start();
    }

    private void notifyMess(String message) {

        if(Settings.loadBoolean(getApplicationContext(), Settings.CHECK_NOTIFY, true)) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("myChannel", "Il Mio Canale", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLightColor(Color.argb(255, 255, 0, 0));
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getApplicationContext(), "myChannel");
            builder.setContentTitle(getString(R.string.app_name));
            builder.setSmallIcon(R.drawable.weather_512);
            builder.setContentText(message);
            builder.setAutoCancel(true);

            Intent intent = new Intent(getApplicationContext(), AroundMeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, intent, 0);

            builder.setContentIntent(pendingIntent);

            Notification notify = builder.build();
            if (notificationManager != null) notificationManager.notify(NOTIFICATION_ID, notify);

        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job: stopped");
        //jobCancelled = true;
        return false;
    }

}
