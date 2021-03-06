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

    /** download tramite nome città e stato **/
    public void downloadCity(String city, String country, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){
        Log.d("DB", "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + key + unit);
        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }

    /** download 5 next days **/
    public void downloadFive(String city, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){
        Log.d("DB", "https://api.openweathermap.org/data/2.5/forecast?"+ city + "&appid=" + key + unit);
        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/forecast?q="+ city + "&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }

    /** download tramite nome città **/
    public void downloadCityName(String city, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?" + city + "&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }

    /** download tramite lat e lon **/
    public void downloadAroundMe(String lat, String lon, String key, String unit, Response.Listener<String> listener, Response.ErrorListener error){

        StringRequest request = new StringRequest(
                StringRequest.Method.GET,
                "https://api.openweathermap.org/data/2.5/find?lat="+ lat + "&lon=" + lon + "&cnt=10&appid=" + key + unit,
                listener,
                error);
        queue.add(request);
    }
}
