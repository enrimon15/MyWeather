package it.univaq.mobileprogramming.myweather.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.Location.LocationGoogleService;
import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.Settings.Settings;
import it.univaq.mobileprogramming.myweather.Settings.SettingsActivity;
import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_around;
import it.univaq.mobileprogramming.myweather.database.AroundDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.InternetConnection.*;


public class AroundMeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationGoogleService.LocationListener, SwipeRefreshLayout.OnRefreshListener {

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
    private final String TAG = "AroundActivity";
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //creazione toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lay = findViewById(R.id.view_list);
        recyclerView = findViewById(R.id.around_list);
        testoTop = findViewById(R.id.testo_top);
        swipeRefreshLayout = findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(AroundMeActivity.this);

        onCreateDrawer();
    }

    /** creazione drawer **/
    protected void onCreateDrawer() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /** check su connessione e posizione, se c'è connessione trova la posizione attuale altrimenti mostra un alert **/
    @Override
    protected void onResume() {
        super.onResume();

        if (InternetConnection.haveNetworkConnection(AroundMeActivity.this)) {
            if(Settings.loadBoolean(getApplicationContext(), Settings.CHECK_LOCATION, true)) {
                startGPS();
            }
            else {
                alert("Per continuare bisogna consentire la posizione dalle impostazioni dell'app", "settings", "ATTENZIONE");
            }
        }
        else {
            loadDataFromDB();
            alert("La connessione internet non è disponibile! \nSono state caricate le città in base all'ultima posizione rilevata.", "connection", "ATTENZIONE");
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    /** creazione alert dato titolo e messaggio da mostrare **/
    private void alert(String message, final String type, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AroundMeActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type.equals("settings")) {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** salvataggio longitudine e latidudine GOOGLE LOCATION **/
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude() + "";
        lon = location.getLongitude() + "";
        locationService.stopLocationUpdates(this);
        downloadData(); //scarica dati tramite lat e lon
        //salva lat e long nelle preferences da usare con il service in background
        Settings.save(getApplicationContext(), Settings.LATITUDE, lat);
        Settings.save(getApplicationContext(), Settings.LONGITUDE, lon);
    }

    /** ricerca posizione attuale se i permessi sono abilitati **/
    private void startGPS() {
            int check = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(check == PackageManager.PERMISSION_GRANTED) {
                if(Settings.loadBoolean(getApplicationContext(), Settings.SWITCH_LOCATION, true)) {
                    //google location
                    Log.d(TAG, "startGOOGLE");
                    locationService = new LocationGoogleService();
                    locationService.onCreate(this, this);
                    locationService.requestLocationUpdates(this);

                }
                else {
                    //gps
                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if(manager != null) manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                }
            }

            else {
                //permessi di posizione
                Log.d(TAG, "startGPS");
                ActivityCompat.requestPermissions(AroundMeActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            }
        }

    /** download dati città vicine tramite lat e long **/
    private void downloadData(){
        swipeRefreshLayout.setRefreshing(true);

        VolleyRequest.getInstance(this)
                .downloadAroundMe(lat,lon, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.length() > 0) {
                            try {
                                Log.d(TAG, "download");
                                ParsingAround pars = new ParsingAround(response);
                                lista = pars.getAround();
                                Log.d(TAG, "parsing");
                                clearDataFromDB(); //svuota db
                                saveDataInDB(lista); //salva in db
                                setView(); //popola la view
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            swipeRefreshLayout.setRefreshing(false);
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

    /** swipe refresh **/
    @Override
    public void onRefresh() { onResume(); }


    /** salvatggio lat e long GPS **/
    private class MyListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude() + "";
            lon = location.getLongitude() + "";
            downloadData(); //scarica dati tramite lat e lon
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }


    /** Salva città nel database. */
    private void saveDataInDB(final List<ListCity> city){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ListCity l : city){
                AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().save(l);
                    }
            }
        }).start();
    }

    /** Carica tutte le città dal db */
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

    /** Svuota db */
    private void clearDataFromDB(){
        new Thread(new Runnable() {
                @Override
                public void run() {
                    AroundDatabase.getInstance(getApplicationContext()).getAroundDAO().deleteAll();
                }
            }).start();
        }


    /****** setta vista *****/
    private void setView() {
        testoTop.setText(R.string.city_around);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter_around(lista));
        recyclerView.setHasFixedSize(true);
    }


    /******* barra top menu ******/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /** option drawer **/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_preferiti) {
            Intent intent = new Intent(getApplicationContext(), FavouriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cerca) {
            Intent intent2 = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent2);
        } else if (id == R.id.nav_posizione) {
            onRefresh();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            alert("Corso 'Mobile Programming' 2018-2019 \nDISIM - Univaq \nApp sviluppata da: \nEnrico Monte & Giuseppe Gasbarro.", "info", "INFORMAZIONI");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** option top manù **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_button:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.position_button:
                onRefresh();
                return true;

            default:
                return false;
        }

    }

    /** gestione tasto back con menù **/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /** abilitare permessi dell app alla posizione **/
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
}

