package it.univaq.mobileprogramming.myweather;


import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
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
import android.widget.Toast;

import java.io.Serializable;

import it.univaq.mobileprogramming.myweather.database.FavDatabase;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Preferiti;
import it.univaq.mobileprogramming.myweather.model.Today;


/**
 * A simple {@link Fragment} subclass.
 */
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
    private FavDatabase favdb;
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

            favdb = Room.databaseBuilder(getActivity(), FavDatabase.class, "favouriteDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                Preferiti p = favdb.favouriteDAO().getFav(n,c);
                if (p != null) {
                    snack = Snackbar.make(lay, "Città già presente nei preferiti", Snackbar.LENGTH_SHORT);
                    snack.setDuration(3000);
                    snack.show();
                }
                else {
                    snack = Snackbar.make(lay, "Città aggiunta ai preferiti", Snackbar.LENGTH_SHORT);
                    snack.setDuration(3000);
                    snack.show();
                    favdb.favouriteDAO().save(new Preferiti(n, c, id));
                }
            }
        });
    }

    public void setOggi(Bundle today){
        Log.d("fragment", "setOggi: ");
        //oggi = today;
        n = today.getString("nome");
        c = today.getString("country");
        id = today.getString("id");
        city.setText(today.getString("nome") + ", " + today.getString("country"));
        description.setText(today.getString("desc"));
        temp.setText(today.getString("temp"));
        mintemp.setText("MIN: " + today.getString("min"));
        maxtemp.setText("MAX: " + today.getString("max"));
        cloud.setText(today.getString("nuv"));
        hum.setText(today.getString("umid"));
        press.setText(today.getString("press"));
        wind.setText(today.getString("vento"));
        sunrise.setText(today.getString("sunrise"));
        sunset.setText(today.getString("sunset"));
        icon.setImageResource(today.getInt("imm"));
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        setOggi(args);
    }
}
