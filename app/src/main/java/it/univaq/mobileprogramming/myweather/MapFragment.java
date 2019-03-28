package it.univaq.mobileprogramming.myweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements  OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private String lon;
    private String lat;
    private String name;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        return  view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("PAVAN", "ENTROSIIII");
        mMap = googleMap;
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);

        if(name == null) return;
        Log.d("POS", latitude + "");
        Log.d("POS", longitude + "");
        LatLng position = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(position).title("POSTO"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        lat = args.getString("lat");
        Log.d("PAVAN", lat);
        lon = args.getString("lon");
        name = args.getString("cName");
        if(mapFragment == null) Log.d("STEFANO", "setArguments: ");

        if(mapFragment != null) mapFragment.getMapAsync(this);

    }
}
