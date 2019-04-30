package it.univaq.mobileprogramming.myweather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_around;
import it.univaq.mobileprogramming.myweather.json.ParsingAround;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.CitySearch;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Today;

public class AroundMeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CITIES_FILE_NAME = "city_list.json";

    private RecyclerView recyclerView;
    private List<ListCity> lista = new ArrayList<ListCity>();
    private Toolbar toolbar;
    private View lay;
    private Snackbar snackbar;
    private TextView testoTop;
    Location location;
    private String lat;
    private String lon;

    private static List<CitySearch> citySuggestions = new ArrayList<>();

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                citySuggestions=loadJson(AroundMeActivity.this);
            }
        }).start();

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
        startGPS();
    }



    private void startGPS() {
            int check = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(check == PackageManager.PERMISSION_GRANTED) {

                LocationListener listener = new LocationListener() {

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
                public void onProviderDisabled(String provider) {} };

                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(manager != null) manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                //location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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


    private void setView() {
        testoTop.setText("Città intorno a me:");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter_around(lista));
        recyclerView.setHasFixedSize(true);
    }

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

            case R.id.position_button:
                Intent intent1 = new Intent(getApplicationContext(), FavouriteActivity.class);
                startActivity(intent1);
                return true;

            default:
                return false;
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

                //cityList.add(readMessage(reader));

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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
