package it.univaq.mobileprogramming.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import it.univaq.mobileprogramming.myweather.InternetConnection.InternetConnection;
import it.univaq.mobileprogramming.myweather.adapters.ViewPagerAdapter;
import it.univaq.mobileprogramming.myweather.json.ParsingToday;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.Today;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DetailsFragment det =  new DetailsFragment();
    TodayFragment tod = new TodayFragment();
    MapFragment map = new MapFragment();
    private String Namecity;
    private Snackbar snackbar;
    private View lay;
    private Today weather;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //creazione toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lay = findViewById(R.id.main_ac);

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);

        if (!InternetConnection.haveNetworkConnection(MainActivity.this)) { //se non c'è connessione
            snackbar = Snackbar.make(lay, R.string.connection_error, snackbar.LENGTH_INDEFINITE);
            snackbar.setDuration(3000);
            snackbar.setAction(R.string.snakebar_remove, new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

        else { //se c'è connessione crea il view pager con i fragment
            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment("OGGI", tod);
            adapter.addFragment("DETTAGLI", det);
            adapter.addFragment("MAPPA", map);

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager); }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (InternetConnection.haveNetworkConnection(MainActivity.this)) { //se c'è connessione download dati
                Namecity = getIntent().getStringExtra("cityID");
                downloadData();
        }
        else swipeRefreshLayout.setRefreshing(false);
    }

    /** download dati e parsing **/
    private void downloadData() {
        swipeRefreshLayout.setRefreshing(true);

        VolleyRequest.getInstance(getApplicationContext())
                .downloadCityName(Namecity, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            try {
                                Log.d("req", "downloadCity: ciao2");
                                ParsingToday pars = new ParsingToday(response); //parsing
                                weather = pars.getToday_object();
                                giveData(); //passa i dati ai 3 fragment del tab layout
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        error.printStackTrace();
                    }
                });
    }

    /** passa i dati ai 3 fragment del tab layout **/
    private void giveData() {
        Bundle t = new Bundle(); //bundle destinato a today
        Bundle d = new Bundle(); //bundle destinato a details
        Bundle m = new Bundle(); //bundle destinato a map

        /* parametri da passare al TODAY fragment */
        t.putString("nome", weather.getCity());
        t.putString("country", weather.getCountry());
        t.putInt("imm", weather.getIcon());
        t.putString("temp", weather.getTemp());
        t.putString("desc", weather.getWeatherResult());
        t.putString("date", weather.getDate());
        /* parametri da passare al DETAILS fragment */
        d.putString("nome", weather.getCity());
        d.putString("country", weather.getCountry());
        d.putString("nuv", weather.getClouds());
        d.putString("desc", weather.getWeatherResult());
        d.putString("temp", weather.getTemp());
        d.putString("min", weather.getMin());
        d.putString("max", weather.getMax());
        d.putString("vento", weather.getWind());
        d.putString("press", weather.getPressure());
        d.putString("sunset", weather.getSunset());
        d.putString("sunrise", weather.getSunrise());
        d.putString("umid", weather.getHumidity());
        d.putInt("imm", weather.getIcon());
        d.putString("id", weather.getcode());
        /* parametri da passare al MAP fragment */
        m.putString("lat", weather.getLat());
        m.putString("lon", weather.getLon());
        m.putString("cName", weather.getCity());
        m.putString("desc", weather.getWeatherResult());
        m.putInt("icon", weather.getIcon());
        m.putString("temperatura", weather.getTemp());

        tod.setArguments(t); //today
        map.setArguments(m); //map
        det.setArguments(d); //details

        swipeRefreshLayout.setRefreshing(false);
    }

    /** options menù **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /** options drawer **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_button:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.position_button:
                Intent intent1 = new Intent(getApplicationContext(), AroundMeActivity.class);
                startActivity(intent1);
                finish();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onRefresh() {
        onResume();
    }
}
