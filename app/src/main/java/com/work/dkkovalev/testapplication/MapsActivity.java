package com.work.dkkovalev.testapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Point> pointsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pointsList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        pointsList = (ArrayList<Point>) bundle.getSerializable("key");

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < pointsList.size(); i++) {

            Double lat = pointsList.get(i).getLat();
            Double lng = pointsList.get(i).getLng();
            String title = pointsList.get(i).getTitle();
            String description = pointsList.get(i).getDescription();
            LatLng latLng = new LatLng(lat, lng);

            mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(description)
            );
        }
    }
}
