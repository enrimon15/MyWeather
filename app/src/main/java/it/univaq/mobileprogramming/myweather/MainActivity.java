package it.univaq.mobileprogramming.myweather;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.pavlospt.CircleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_hour;
import it.univaq.mobileprogramming.myweather.adapters.ViewPagerAdapter;
import it.univaq.mobileprogramming.myweather.model.Five_Day;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //private Snackbar snackbar;
    //private View view;

    /*private RecyclerView recyclerView;
    private List<Five_Day> ore;

    private TextView t2_city,t4_date;
    private CircleView t1_temp;
    private ImageView icon;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //view = findViewById(R.id.view);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment("ITEM ONE", new TodayFragment());
        adapter.addFragment("ITEM TWO", new DetailsFragment());
        adapter.addFragment("ITEM THREE", new MapFragment());


        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        /*t1_temp = (CircleView)findViewById(R.id.weather_result);
        t2_city = (TextView)findViewById(R.id.city_country);
        t4_date = (TextView)findViewById(R.id.current_date);
        icon = findViewById(R.id.weather_icon);
        Log.d("ENRI", "ciao ");

        try {
            find_weather();
            Log.d("ENRI", "ciao1 ");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.weather_list);
        ore = new ArrayList<>();

        ore.add(new Five_Day("GIO","21°","18°",R.drawable.cloudy));
        ore.add(new Five_Day("VEN","26°","18°",R.drawable.snow));
        ore.add(new Five_Day("SAB","28°","18°",R.drawable.sun));
        ore.add(new Five_Day("DOM","25°","18°",R.drawable.cloudy));
        ore.add(new Five_Day("LUN","22°","18°",R.drawable.cloudy));
        Log.d("ENRI", "ciao2 ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        Log.d("ENRI", "ciao3 ");
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("ENRI", "ciao4 ");

        recyclerView.setAdapter(new RecyclerViewAdapter_hour(ore, this));
        recyclerView.setHasFixedSize(true);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    /*public void find_weather() throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.openweathermap.org/data/2.5/weather?q=L'aquila,it&appid=73f45256d96f6980fc804cca915873ea&units=metric&lang=it", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                // called when response HTTP status is "200 OK"
                String in = new String(responseBody);
                String response = new String(responseBody);
                try {
                    parseMethod(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "404 error"
                /*snackbar = Snackbar.make(view, "errore", snackbar.LENGTH_INDEFINITE);
                snackbar.setDuration(3000);
                snackbar.setAction("RIMUOVI", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                Toast.makeText(getApplicationContext(), "Errore: Inserire Città valida!", Toast.LENGTH_SHORT).show();
            }

        }); }

        public void parseMethod(String apiString) throws JSONException {
        String url = apiString;
        JSONObject jor = new JSONObject(url);

                    JSONObject main_object = jor.getJSONObject("main");
                    JSONObject country_object = jor.getJSONObject("sys");
                    JSONArray array = jor.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    Double temp= main_object.getDouble("temp");
                    String country= String.valueOf(country_object.getString("country"));
                    String description = object.getString("description");
                    String im = object.getString("icon");
                    setIconWeather(im, icon);
                    String city = jor.getString("name");

                    String temperatura = String.format ("%.1f", temp);
                    t1_temp.setTitleText(temperatura);
                    t2_city.setText(city + ", " + country);
                    t1_temp.setSubtitleText(description);

                    Date data = new  Date();
                    Locale.setDefault(Locale.ITALIAN);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM ");
                    String formatted_date = sdf.format(data);

                    t4_date.setText(formatted_date);
            }



         public void dailyHours(String city){

         }

    public void setIconWeather(String condition, ImageView image) {
        if (condition.equals("01d")) image.setImageResource(R.drawable.sun);
        else if (condition.equals("01n")) image.setImageResource(R.drawable.sun);
        else if (condition.equals("02d")) image.setImageResource(R.drawable.cloudy);
        else if (condition.equals("02n")) image.setImageResource(R.drawable.cloudy_night);
        else if (condition.equals("03d")) image.setImageResource(R.drawable.cloud);
        else if (condition.equals("03n")) image.setImageResource(R.drawable.cloud);
        else if (condition.equals("04d")) image.setImageResource(R.drawable.cloud);
        else if (condition.equals("04n")) image.setImageResource(R.drawable.cloud);
        else if (condition.equals("09d")) image.setImageResource(R.drawable.rain);
        else if (condition.equals("09n")) image.setImageResource(R.drawable.rain);
        else if (condition.equals("10d")) image.setImageResource(R.drawable.rain_sun);
        else if (condition.equals("10n")) image.setImageResource(R.drawable.rain_night);
        else if (condition.equals("11d")) image.setImageResource(R.drawable.lightning);
        else if (condition.equals("11n")) image.setImageResource(R.drawable.lightning_night);
        else if (condition.equals("13d")) image.setImageResource(R.drawable.snowing);
        else if (condition.equals("13n")) image.setImageResource(R.drawable.snowing_night);
        else if (condition.equals("50d")) image.setImageResource(R.drawable.windy);
        else if (condition.equals("50n")) image.setImageResource(R.drawable.windy);
    }*/

    }
