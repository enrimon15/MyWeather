package it.univaq.mobileprogramming.myweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.univaq.mobileprogramming.myweather.model.Today;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment{
    private  static Today oggi;
    private static TextView city;
    private static  TextView description;
    private static  TextView temp;
    private static  TextView mintemp;
    private static  TextView maxtemp;
    private static  TextView cloud;
    private static  TextView hum;
    private static  TextView press;
    private static  TextView wind;
    private static  TextView sunrise;
    private static  TextView sunset;
    private static ImageView icon;

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

        return view;
    }

    public static void setOggi(Today today){
        Log.d("fragment", "setOggi: ");
        oggi = today;
        Log.d("fragment", "setOggi: " + oggi.getCity());

        city.setText(oggi.getCity());
        description.setText(oggi.getWeatherResult());
        temp.setText(oggi.getTemp());
        mintemp.setText("MIN: " + oggi.getMin());
        maxtemp.setText("MAX: " + oggi.getMax());
        cloud.setText(oggi.getClouds());
        hum.setText(oggi.getHumidity());
        press.setText(oggi.getPressure());
        wind.setText(oggi.getWind());
        sunrise.setText(oggi.getSunrise());
        sunset.setText(oggi.getSunset());
        icon.setImageResource(oggi.getIcon());
    }

}
