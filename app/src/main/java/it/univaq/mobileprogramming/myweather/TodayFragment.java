package it.univaq.mobileprogramming.myweather;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pavlospt.CircleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_hour;
import it.univaq.mobileprogramming.myweather.model.Five_Day;


public class TodayFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Five_Day> ore;

    private TextView t2_city,t4_date;
    private CircleView t1_temp;
    private ImageView icon;

    private Snackbar snackbar;
    private View lay;


    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        t1_temp = (CircleView) view.findViewById(R.id.weather_result);
        t2_city = (TextView) view.findViewById(R.id.city_country);
        t4_date = (TextView) view.findViewById(R.id.current_date);
        icon = (ImageView) view.findViewById(R.id.weather_icon);
        recyclerView = view.findViewById(R.id.weather_list);
        lay = view.findViewById(R.id.view_today);

        //richiama il metodo per popolare la vista principale (meteo odierno)
        try {
            find_weather();
            Log.d("enri", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //popola la recycle view con il meteo dei prossimi 5 giorni
        ore = new ArrayList<>();

        ore.add(new Five_Day("GIO","21°","18°",R.drawable.cloudy));
        ore.add(new Five_Day("VEN","26°","18°",R.drawable.snow));
        ore.add(new Five_Day("SAB","28°","18°",R.drawable.sun));
        ore.add(new Five_Day("DOM","25°","18°",R.drawable.cloudy));
        ore.add(new Five_Day("LUN","22°","18°",R.drawable.cloudy));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            } };

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(new RecyclerViewAdapter_hour(ore, getActivity()));
        recyclerView.setHasFixedSize(true);

        return view;

    }

    public void find_weather() throws JSONException {

        String url = "https://api.openweathermap.org/data/2.5/weather?q=L'Aquila,it&appid=73f45256d96f6980fc804cca915873ea&units=metric&lang=it";

        // Request a string response
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.length() > 0) {
                            try {
                                parseMethod(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                snackbar = Snackbar.make(lay, "Città non trovata", snackbar.LENGTH_INDEFINITE);
                snackbar.setDuration(3000);
                snackbar.setAction("RIMUOVI", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                //Toast.makeText(getContext(), "Errore: Inserire Città valida!", Toast.LENGTH_SHORT).show();

                error.printStackTrace();
            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    public void parseMethod(String apiString) throws JSONException {
        Log.d("enri", "we");
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
        Log.d("enri", "x");

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
    }

}
