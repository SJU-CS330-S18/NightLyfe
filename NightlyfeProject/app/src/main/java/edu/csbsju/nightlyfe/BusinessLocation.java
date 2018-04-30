/*
BusinessLocation class is used to dynamically locate each restaurant that the user is interested in and
display in googleMaps
@author Grant Salk
 */

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
import com.google.android.gms.maps.model.Marker;
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

        //Opens database associated with NightLyfe application via SQLite
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        //Retrieves user ID from recent page
        user = getIntent().getStringExtra("user");

        //Retrieves business key from recent page
        key = getIntent().getIntExtra("key", -1);

        //Call to database to select all columns from corresponding business business id that matches the business key
        Cursor resultSet = mydatabase.rawQuery("Select * from business where id = " + key, null);
        resultSet.moveToFirst();

        //Gets the latitude from the business table in DB
        latitude = resultSet.getDouble(4);
        //Gets the longitude from the business table in DB
        longitude = resultSet.getDouble(5);
        //Gets the business name from the business table in DB
        businessName = resultSet.getString(1);

        //Test print latitude and longitude
        System.out.println(latitude);
        System.out.println(longitude);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adds a business marker and moves/zooms the camera
        LatLng business = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(business).title(businessName);
        mMap.addMarker(marker);
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
