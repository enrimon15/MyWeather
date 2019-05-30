package it.univaq.mobileprogramming.myweather.Background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import it.univaq.mobileprogramming.myweather.AroundMeActivity;
import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.Settings.Settings;
import it.univaq.mobileprogramming.myweather.SplashScreen;
import it.univaq.mobileprogramming.myweather.database.AroundDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyWorker extends Worker {
    private static final String TAG = "JOBSERVICE";
    private static final int NOTIFICATION_ID = 55553;
    private String lat;
    private String lon;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        doBackgroundWork();
        return Result.success();
    }

    private void doBackgroundWork() {

        Log.d(TAG, "pre work ");

        if ((Settings.loadString(getApplicationContext(), Settings.LATITUDE, "") == null) || (Settings.loadString(getApplicationContext(), Settings.LONGITUDE, "") == null))
            return;

        lat = Settings.loadString(getApplicationContext(), Settings.LATITUDE, "");
        lon = Settings.loadString(getApplicationContext(), Settings.LONGITUDE, "");
        Log.d(TAG, lat);
        Log.d(TAG, lon);

        VolleyRequest.getInstance(getApplicationContext())
                .downloadBackground(lat,lon, getApplicationContext().getResources().getString(R.string.keyOPEN), getApplicationContext().getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
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

                Log.d(TAG, "post work ");
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

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("myChannel", "Il Mio Canale", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLightColor(Color.argb(255, 255, 0, 0));
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getApplicationContext(), "myChannel");
            builder.setContentTitle(getApplicationContext().getString(R.string.app_name));
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setLargeIcon(BitmapFactory.decodeResource( getApplicationContext().getResources(), R.drawable.weather_512));
            builder.setContentText(message);
            builder.setAutoCancel(true);

            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, intent, 0);

            builder.setContentIntent(pendingIntent);

            Notification notify = builder.build();
            if (notificationManager != null) notificationManager.notify(NOTIFICATION_ID, notify);

        }
    }
}
