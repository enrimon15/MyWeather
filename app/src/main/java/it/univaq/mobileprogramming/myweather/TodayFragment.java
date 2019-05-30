package it.univaq.mobileprogramming.myweather;


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
import it.univaq.mobileprogramming.myweather.model.Today;


public class TodayFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Five_Days> giorni = new ArrayList<Five_Days>();
    private TextView t2_city,t4_date;
    private CircleView t1_temp;
    private ImageView icon;
    private CircleView circle;
    private  RecyclerViewAdapter_hour adapter;
    private String nameC;
    private int iconC;
    private String countryC;
    private String tempC;
    private String descC;
    private String dateC;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new RecyclerViewAdapter_hour(giorni);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nameC != null) setView(); //popola view
    }

    /** prende dati da main activity **/
    @Override
    public void setArguments(@Nullable Bundle args) {
        Log.d("dentro0", "ciao");
        super.setArguments(args);
        nameC = args.getString("nome");
        iconC = args.getInt("imm");
        countryC = args.getString("country");
        tempC = args.getString("temp");
        descC = args.getString("desc");
        dateC = args.getString("date");
        fiveDays(); //download next 5 days
    }

    /** download e parsing next five days **/
    private void fiveDays(){
        VolleyRequest.getInstance(getActivity())
                .downloadFive(nameC, getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            try {
                                Log.d("dentro", response);
                                ParsingFiveDays pars = new ParsingFiveDays(response); //parsing
                                giorni.addAll(pars.getListaDay());
                                setView(); //popola vista
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /** popola view **/
    private void setView() {
        circle.setVisibility(View.VISIBLE);
        icon.setImageResource(iconC);
        t1_temp.setTitleText(tempC);
        t2_city.setText(nameC + ", " + countryC);
        t1_temp.setSubtitleText(descC);
        t4_date.setText(dateC);
        if (adapter != null) adapter.notifyDataSetChanged();
    }


}
