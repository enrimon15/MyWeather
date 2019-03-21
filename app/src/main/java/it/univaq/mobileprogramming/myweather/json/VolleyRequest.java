package it.univaq.mobileprogramming.myweather.json;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyRequest {
    private RequestQueue queue;

    private static VolleyRequest instance = null;

    public static synchronized VolleyRequest getInstance(Context context){
        if (instance == null) instance = new VolleyRequest(context);
        return instance;
    }

    private VolleyRequest(Context context){

        queue = Volley.newRequestQueue(context);
    }

    public void downloadCity(String city, String country, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }

    public void downloadDetail(String city, String country, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/forecast?q="+ city + "," + country + "&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }

    public void downloadCoord(String city, String country, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "",
                listener,
                null);
        queue.add(request);
    }
}
