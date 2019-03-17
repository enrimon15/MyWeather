package it.univaq.mobileprogramming.myweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import it.univaq.mobileprogramming.myweather.json.ParsingFiveDays;
import it.univaq.mobileprogramming.myweather.json.ParsingToday;
import it.univaq.mobileprogramming.myweather.json.RequestCityCountry;
import it.univaq.mobileprogramming.myweather.json.RequestFiveDays;
import it.univaq.mobileprogramming.myweather.model.Five_Days;
import it.univaq.mobileprogramming.myweather.model.Today;


public class TodayFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Five_Days> giorni;

    private TextView t2_city,t4_date;
    private CircleView t1_temp;
    private ImageView icon;

    private Snackbar snackbar;
    private View lay;

    private static Today weather;


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

        //stringa 5 giorni json
        RequestFiveDays richiesta2 = new RequestFiveDays("L'Aquila","it", getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS));
        String url2 = richiesta2.getUrl();

        //stringa today weather json
        RequestCityCountry richiesta = new RequestCityCountry("L'Aquila","it", getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS));
        String url = richiesta.getUrl();


        //richiama il metodo per popolare la vista principale (meteo odierno)
        try {
            find_weather(url);
            Log.d("enri", "sopra");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //popola la recycle view con il meteo dei prossimi 5 giorni
        fiveDays();

        return view;

    }


    //json request
    public void find_weather(String url) throws JSONException {

        // Request a string response
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.length() > 0) {
                            try {
                                ParsingToday pars = new ParsingToday(response);
                                weather = pars.getToday_object();
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


    private void fiveDays(){

        //stringa 5 giorni json
        RequestFiveDays richiesta2 = new RequestFiveDays("L'Aquila","it", getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS));
        String url2 = richiesta2.getUrl();
        Log.d("fragment", url2);

        // Request a string response
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    { Log.d("dentro", response);
                        if (response.length() > 0) {
                            try {
                                Log.d("dentro2", response);
                                ParsingFiveDays pars = new ParsingFiveDays(response);
                                giorni = pars.getListaDay();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
                                    @Override
                                    public boolean canScrollHorizontally() {
                                        return false;
                                    } };

                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(new RecyclerViewAdapter_hour(giorni, getActivity()));
                                recyclerView.setHasFixedSize(true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                //Toast.makeText(getContext(), "Errore: Inserire Città valida!", Toast.LENGTH_SHORT).show();

                error.printStackTrace();
            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private void setView() {
        icon.setImageResource(weather.getIcon());
        t1_temp.setTitleText(weather.getTemp());
        t2_city.setText(weather.getCity() + ", " + weather.getCountry());
        t1_temp.setSubtitleText(weather.getWeatherResult());
        t4_date.setText(weather.getDate());
        DetailsFragment.setOggi(weather);
        Log.d("fine", "setView: ");
    }


}
