package edu.csbsju.nightlyfe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusinessLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public SQLiteDatabase mydatabase;
    public String user;
    public int key;
    public double latitude;
    public double longitude;
    public String businessName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);
        user = getIntent().getStringExtra("user");
        key = getIntent().getIntExtra("key", -1);

        Cursor resultSet = mydatabase.rawQuery("Select * from business where id = " + key, null);
        resultSet.moveToFirst();

        latitude = resultSet.getDouble(4);
        longitude = resultSet.getDouble(5);
        businessName = resultSet.getString(1);

        System.out.println(latitude);
        System.out.println(longitude);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adds a business marker and moves/zooms the camera
        LatLng business = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(business).title(businessName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(business));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
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
