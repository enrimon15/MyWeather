package it.univaq.mobileprogramming.myweather;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.Location.LocationGoogleService;
import it.univaq.mobileprogramming.myweather.database.AroundDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private List<ListCity> lista = new ArrayList<ListCity>();

    private String lat;
    private String lon;
    private final int notification_id = 1;

    public MyIntentService() {
        super("MyIntentService");
    }

    private boolean stop;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int n=0;
        stop=false;
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        while(!stop)
        {
            Log.i("PROVA SERVICE", "Evento n."+n++);
            try {
                downloadData();
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                System.out.println("erroererwerwreererer rerererer ");
            }
        }
    }

    @Override
    public void onDestroy()
    {
        stop=true;
        Log.i("PROVA SERVICE", "Distruzione Service");
    }

    void downloadData(){
        VolleyRequest.getInstance(this)
                .downloadAroundMe(lat,lon, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        System.out.println("startDownload");
                        if (response.length() > 0) {
                            try {
                                ParsingAround pars = new ParsingAround(response);
                                lista = pars.getAround();
                                System.out.println(lista);
                                clearDataFromDB();
                                saveDataInDB(lista);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        System.out.println("errore download");
                    }
                });
    }

    /** Save city in database. */
    private void saveDataInDB(final List<ListCity> city){
        System.out.println("saveDB");
        // Save by RoomDatabase
        Thread save = new Thread(new Runnable() {
            @Override
            public void run() {
                for (ListCity l : city){
                    AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().save(l);
                    //notifyMess("Database aggiornato: " + lat + ", " + lon);
                }
            }
        });
        save.start();
    }

    /** Clear data from database */
    private void clearDataFromDB(){
        System.out.println("clearDB");
        //lista.clear();
        // Delete by RoomDatabase
        Thread clear = new Thread(new Runnable() {
            @Override
            public void run() {
                AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().deleteAll();
            }
        });
        clear.start();
    }


    /** Publish a notify. */
    private void notifyMess(String message) {
        System.out.println("notifica ");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myChannel", "Il Mio Canale", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.argb(255, 255, 0, 0));
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
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
        if(notificationManager != null) notificationManager.notify(notification_id, notify);
    }

}
