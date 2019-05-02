package it.univaq.mobileprogramming.myweather;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.Location.LocationGoogleService;
import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_around;
import it.univaq.mobileprogramming.myweather.database.AroundDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.CitySearch;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.InternetConnection.*;


public class AroundMeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationGoogleService.LocationListener  {

    private RecyclerView recyclerView;
    private List<ListCity> lista = new ArrayList<ListCity>();
    private Toolbar toolbar;
    private View lay;
    private Snackbar snackbar;
    private TextView testoTop;
    private String lat;
    private String lon;
    private MyListener listener = new MyListener();
    private LocationGoogleService locationService;
    private final int notification_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lay = findViewById(R.id.view_list);
        recyclerView = findViewById(R.id.around_list);
        testoTop = findViewById(R.id.testo_top);

        //gestione drawer
        onCreateDrawer();
    }

    protected void onCreateDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.haveNetworkConnection(AroundMeActivity.this))
            startGPS();
        else {
            loadDataFromDB();

            AlertDialog.Builder builder = new AlertDialog.Builder(AroundMeActivity.this);
            builder.setTitle("ATTENZIONE");
            builder.setMessage("La connessione internet non è disponibile! \nSono state caricate le città in base all'ultima posizione rilevata.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    //google location
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude() + "";
        lon = location.getLongitude() + "";
        downloadData();
        locationService.stopLocationUpdates(this);
    }

    //gps location
    private void startGPS() {
            int check = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(check == PackageManager.PERMISSION_GRANTED) {
                //LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //if(manager != null) manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);

                locationService = new LocationGoogleService();
                locationService.onCreate(this, this);
                locationService.requestLocationUpdates(this);
            }

            else {
                ActivityCompat.requestPermissions(AroundMeActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            }
        }


    private void downloadData(){
        VolleyRequest.getInstance(this)
                .downloadAroundMe(lat,lon, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.length() > 0) {
                            try {
                                ParsingAround pars = new ParsingAround(response);
                                lista = pars.getAround();
                                clearDataFromDB();
                                saveDataInDB(lista);
                                setView();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        snackbar = Snackbar.make(lay, "Errore di geolocalizzazione", snackbar.LENGTH_INDEFINITE);
                        snackbar.setDuration(3000);
                        snackbar.setAction("RIMUOVI", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                        error.printStackTrace();
                    }
                });
    }


    /**
     * Location Listener
     */
    private class MyListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
            Log.d("prova", location.getLongitude() + "");
            downloadData();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }


    /** Save city in database. */
    private void saveDataInDB(final List<ListCity> city){

        // Save by RoomDatabase
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ListCity l : city){
                AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().save(l);
                notifyMess("Database aggiornato: " + lat + ", " + lon);
                    }
            }
        }).start();
    }

    /** Load all cities from database */
    private void loadDataFromDB(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<ListCity> data = AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().getAll();
                    lista.addAll(data);
                    setView();
                }
            }).start();
        }

    /** Clear data from database */
    private void clearDataFromDB(){

        //lista.clear();
            // Delete by RoomDatabase
        new Thread(new Runnable() {
                @Override
                public void run() {
                    AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().deleteAll();
                }
            }).start();
        }


    /** Publish a notify. */
    private void notifyMess(String message) {

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

    /****** setta vista *****/
    private void setView() {
        testoTop.setText("Città intorno a me:");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter_around(lista));
        recyclerView.setHasFixedSize(true);
    }


    /******* menu ******/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            Intent intent2 = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent2);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_button:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.favourite_button:
                Intent intent1 = new Intent(getApplicationContext(), FavouriteActivity.class);
                startActivity(intent1);
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startGPS();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

}
