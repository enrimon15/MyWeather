package it.univaq.mobileprogramming.myweather;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements  OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private String lon;
    private String lat;
    private String name;
    private  int icon;
    private  String desc;
    private String temp;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return  view;

    }

    @Override
    public void onResume() {
        super.onResume();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) mapFragment.getMapAsync(this);
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
        int height = 140;
        int width = 140;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(icon);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mMap.addMarker(new MarkerOptions().position(position).title(name).snippet(desc + ", temperatura: " + temp).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,13), 5000, null);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
            lat = args.getString("lat");
            lon = args.getString("lon");
            name = args.getString("cName");
            desc = args.getString("desc");
            icon = args.getInt("icon");
            temp = args.getString("temperatura");

    }
}
