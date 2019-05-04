package it.univaq.mobileprogramming.myweather;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

import it.univaq.mobileprogramming.myweather.InternetConnection.InternetConnection;
import it.univaq.mobileprogramming.myweather.adapters.ViewPagerAdapter;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Today;

public class MainActivity extends AppCompatActivity implements TodayFragment.OnMyListner{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DetailsFragment fr =  new DetailsFragment();
    TodayFragment td = new TodayFragment();
    MapFragment mp = new MapFragment();
    private String Namecity;
    private Today todayCity;
    private Snackbar snackbar;
    private View lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lay = findViewById(R.id.main_ac);

        if (!InternetConnection.haveNetworkConnection(MainActivity.this)) {
            snackbar = Snackbar.make(lay, "Errore di connessione", snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(3000);
            snackbar.setAction("RIMUOVI", new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

        else {
            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment("OGGI", td);
            adapter.addFragment("DETTAGLI", fr);
            adapter.addFragment("MAPPA", mp);

            viewPager.setAdapter(adapter);

            tabLayout.setupWithViewPager(viewPager); }

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


    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AroundMeActivity.class);
        startActivity(intent);
    }*/
}
