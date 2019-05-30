package it.univaq.mobileprogramming.myweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import it.univaq.mobileprogramming.myweather.database.FavDatabase;
import it.univaq.mobileprogramming.myweather.model.Preferiti;

public class DetailsFragment extends Fragment{
    private TextView city;
    private TextView description;
    private TextView temp;
    private TextView mintemp;
    private TextView maxtemp;
    private TextView cloud;
    private TextView hum;
    private TextView press;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private ImageView icon;
    private ImageButton pref;
    private Snackbar snack;
    private View lay;

    private String n;
    private String c;
    private String id;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        city = view.findViewById(R.id.city_details);
        description = view.findViewById(R.id.description_details);
        temp = view.findViewById(R.id.temp_details);
        maxtemp = view.findViewById(R.id.max_details);
        mintemp = view.findViewById(R.id.min_details);
        cloud = view.findViewById(R.id.clouds_details);
        hum = view.findViewById(R.id.humidity_details);
        press = view.findViewById(R.id.pressure_details);
        wind = view.findViewById(R.id.wind_details);
        sunrise = view.findViewById(R.id.sunrise_details);
        sunset = view.findViewById(R.id.sunset_details);
        icon = view.findViewById(R.id.icon_weather_details);
        pref = view.findViewById(R.id.star);
        lay = view.findViewById(R.id.view_details);

        /** gestione tocco su "star" aggiungi a preferiti **/
        pref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Preferiti p = FavDatabase.getInstance(getContext()).favouriteDAO().getFav(n, c); //check se già presente (name, country)
                        if (p != null) {
                            snack = Snackbar.make(lay, R.string.city_already_favourite, Snackbar.LENGTH_SHORT);
                            snack.setDuration(3000);
                            snack.show();
                        } else {
                            snack = Snackbar.make(lay, R.string.city_added_favourite, Snackbar.LENGTH_SHORT);
                            snack.setDuration(3000);
                            snack.show();
                            FavDatabase.getInstance(getContext()).favouriteDAO().save(new Preferiti(n, c, id));
                        }
                    }
                    }).start();
                }
            });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /** popola view **/
    public void setDetails(Bundle today){
        Log.d("fragmentDeatils", "setOggi: ");
        n = today.getString("nome");
        c = today.getString("country");
        id = today.getString("id");

        city.setText(n + ", " + c);
        description.setText(today.getString("desc"));
        temp.setText(today.getString("temp"));
        mintemp.setText("MAX: " + " " + today.getString("min"));
        maxtemp.setText("MIN: " + today.getString("max"));
        cloud.setText(today.getString("nuv"));
        hum.setText(today.getString("umid"));
        press.setText(today.getString("press"));
        wind.setText(today.getString("vento"));
        sunrise.setText(today.getString("sunrise"));
        sunset.setText(today.getString("sunset"));
        icon.setImageResource(today.getInt("imm"));
    }

    /** prende dati passati da main **/
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        setDetails(args); //popola view
    }
}
