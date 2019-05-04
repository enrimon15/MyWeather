package it.univaq.mobileprogramming.myweather;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.pavlospt.CircleView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_hour;
import it.univaq.mobileprogramming.myweather.json.ParsingFiveDays;
import it.univaq.mobileprogramming.myweather.json.ParsingToday;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.Five_Days;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Today;


public class TodayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Five_Days> giorni = new ArrayList<Five_Days>();

    private TextView t2_city,t4_date;
    private CircleView t1_temp;
    private ImageView icon;
    private CircleView circle;

    private Snackbar snackbar;
    private View lay;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static Today weather;
    private  String cityArgument;
    private  RecyclerViewAdapter_hour adapter;

    private  OnMyListner myListner;

    @Override
    public void onRefresh() {
        onResume();
    }

    public  interface OnMyListner{
        void passCity(Today today);
    }

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
        circle = (CircleView) view.findViewById(R.id.weather_result);
        recyclerView = view.findViewById(R.id.weather_list);
        lay = view.findViewById(R.id.view_today);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            } };

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter_hour(giorni);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = view.findViewById(R.id.main_swipe);
        swipeRefreshLayout.setOnRefreshListener(TodayFragment.this);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        //richiama il metodo per popolare la vista principale (meteo odierno)

        find_weather();

        //metodo che popola la recycle view con il meteo dei prossimi 5 giorni

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            myListner = (OnMyListner) context;
        } catch (ClassCastException e){
            throw new ClassCastException("Devi implementare l'interfaccia");
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        cityArgument = args.getString("nameCity");
        Log.d("prova", cityArgument);
    }


    //json request
    public void find_weather() {
        giorni.clear();
        swipeRefreshLayout.setRefreshing(true);
        // Request a string response
        VolleyRequest.getInstance(getActivity())
                .downloadCityName(cityArgument, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.length() > 0) {
                            try {
                                Log.d("req", "downloadCity: ciao2");
                                ParsingToday pars = new ParsingToday(response);
                                weather = pars.getToday_object();
                                fiveDays();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                t2_city.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);
                circle.setVisibility(View.INVISIBLE);
                t4_date.setVisibility(View.INVISIBLE);

                snackbar = Snackbar.make(lay, "Città non trovata", snackbar.LENGTH_INDEFINITE);
                snackbar.setDuration(3000);
                snackbar.show();
                error.printStackTrace();
            }
        });

    }


    private void fiveDays(){

        // Request a string response
        VolleyRequest.getInstance(getActivity())
                .downloadFive(cityArgument, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    { Log.d("dentro", response);
                        if (response.length() > 0) {
                            try {
                                Log.d("dentro2", response);
                                ParsingFiveDays pars = new ParsingFiveDays(response);
                                giorni.addAll(pars.getListaDay());
                                setView();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void setView() {
        circle.setVisibility(View.VISIBLE);
        icon.setImageResource(weather.getIcon());
        t1_temp.setTitleText(weather.getTemp());
        t2_city.setText(weather.getCity() + ", " + weather.getCountry());
        t1_temp.setSubtitleText(weather.getWeatherResult());
        t4_date.setText(weather.getDate());
        if (adapter != null) adapter.notifyDataSetChanged();

        myListner.passCity(weather);
    }


}
