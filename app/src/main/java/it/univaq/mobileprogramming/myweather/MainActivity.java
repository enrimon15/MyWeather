package it.univaq.mobileprogramming.myweather;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import it.univaq.mobileprogramming.myweather.adapters.ViewPagerAdapter;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Today;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TodayFragment.OnMyListner{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DetailsFragment fr =  new DetailsFragment();
    TodayFragment td = new TodayFragment();
    MapFragment mp = new MapFragment();
    private String Namecity;
    private Today todayCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //gestione activity_main
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment("OGGI", td);
        adapter.addFragment("DETTAGLI", fr);
        adapter.addFragment("MAPPA", mp);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        //gestione drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getStringExtra("cityID") != null) Namecity =  getIntent().getStringExtra("cityID");
        else Namecity = "q=" + getIntent().getStringExtra("cityName");

        Bundle b = new Bundle();
        b.putString("nameCity",Namecity);

        td.setArguments(b);
        //mp.setArguments(b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        //MenuItem searchView = menu.findItem(R.id.search_item); PER APRIRE BARRA RICERCA
        //searchView.expandActionView();


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
                /*Intent intent1 = new Intent(getApplicationContext(), AroundMeActivity.class);
                startActivity(intent1);*/
                Intent intent1 = new Intent(getApplicationContext(), FavouriteActivity.class);
                startActivity(intent1);
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
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
    public void passCity(Today today) {
        todayCity = today;

        Bundle x = new Bundle();
        Bundle map = new Bundle();

        x.putString("nome", todayCity.getCity());
        x.putString("country", todayCity.getCountry());
        x.putString("nuv", todayCity.getClouds());
        x.putString("desc", todayCity.getWeatherResult());
        x.putString("temp", todayCity.getTemp());
        x.putString("min", todayCity.getMin());
        x.putString("max", todayCity.getMax());
        x.putString("vento", todayCity.getWind());
        x.putString("press", todayCity.getPressure());
        x.putString("sunset", todayCity.getSunset());
        x.putString("sunrise", todayCity.getSunrise());
        x.putString("umid", todayCity.getHumidity());
        x.putInt("imm", todayCity.getIcon());
        x.putString("id", todayCity.getcode());
        map.putString("lat", todayCity.getLat());
        map.putString("lon", todayCity.getLon());
        map.putString("cName", todayCity.getCity());
        map.putString("desc", todayCity.getWeatherResult());
        map.putInt("icon", todayCity.getIcon());
        map.putString("temperatura", todayCity.getTemp());

        mp.setArguments(map);
        fr.setArguments(x);
    }
}
