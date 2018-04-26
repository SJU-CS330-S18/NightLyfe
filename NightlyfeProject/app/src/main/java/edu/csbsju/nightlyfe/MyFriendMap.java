package edu.csbsju.nightlyfe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import java.util.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyFriendMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public SQLiteDatabase mydatabase;
    public String user, friend;
    public int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_map);

        //Opens database associated with NightLyfe application via SQLite
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        user = getIntent().getStringExtra("user");
        friend = getIntent().getStringExtra("friend");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions marker = null;

        Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '" + friend + "';", null);
        resultSet.moveToFirst();
        int destination = resultSet.getInt(4);
        if (destination != 0){
            Cursor resultSet2 = mydatabase.rawQuery("Select b.latitude, b.longitude, u.username from users u, business b where u.username = '" + friend + "' and u.destination = b.id;", null);
            resultSet2.moveToFirst();
            // Adds a business marker and moves/zooms the camera
            LatLng business = new LatLng(resultSet2.getFloat(0), resultSet2.getFloat(1));
            marker = new MarkerOptions().position(business).title(friend);
            mMap.addMarker(marker);
            resultSet.moveToNext();
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        //Allows user to use zoom buttons on screen
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Allows user to use zooming gestures
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Allows all map gestures
        mMap.getUiSettings().setAllGesturesEnabled(true);
        //Changes map mode to hybrid
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Check Permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, )
            ;
        }
*/
        //mMap.setMyLocationEnabled(true);
        // mMap.setOnMyLocationButtonClickListener(this);

    }
}