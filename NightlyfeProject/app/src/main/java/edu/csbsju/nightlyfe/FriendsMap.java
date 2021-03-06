package edu.csbsju.nightlyfe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FriendsMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public SQLiteDatabase mydatabase;
    public String user;
    public int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_map);

        //Opens database associated with NightLyfe application via SQLite
        mydatabase = openOrCreateDatabase("NightLyfe",MODE_PRIVATE,null);

        user = getIntent().getStringExtra("user");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ArrayList<Integer> locations = new ArrayList<Integer>();
        ArrayList<Integer> count = new ArrayList<Integer>();
        ArrayList<String> last = new ArrayList<String>();

        Cursor friendsResultSet = mydatabase.rawQuery("Select user2 from friends where user1 = '"+user+"';", null);
        friendsResultSet.moveToFirst();
        int size = friendsResultSet.getCount();
        //System.out.println(size);
        mMap = googleMap;

        MarkerOptions marker = null;

        for(int i = 0; i < size ; i++) {
            String friend = friendsResultSet.getString(0);
            Cursor resultSet = mydatabase.rawQuery("Select * from users where username = '" + friend + "';", null);
            resultSet.moveToFirst();
            int destination = resultSet.getInt(4);
            if (destination != 0){
                if(locations.contains(destination)){
                    last.set(locations.indexOf(destination), friend);
                    count.set(locations.indexOf(destination),count.get(locations.indexOf(destination))+1);
                }
                else {
                    locations.add(destination);
                    count.add(1);
                    last.add(friend);
                }
                resultSet.moveToNext();
            }
            friendsResultSet.moveToNext();
        }

        for(int i = 0 ; i < locations.size(); i++){
            Cursor resultSet2 = mydatabase.rawQuery("Select b.latitude, b.longitude, u.username from users u, business b where u.username = '" + last.get(i) + "' and u.destination = b.id;", null);
            resultSet2.moveToFirst();
            // Adds a business marker and moves/zooms the camera
            LatLng business = new LatLng(resultSet2.getFloat(0), resultSet2.getFloat(1));
            Cursor businessname = mydatabase.rawQuery("Select b.name from business b where b.id = " + locations.get(i) + ";", null);
            businessname.moveToFirst();
            String name = businessname.getString(0);
            if(count.get(i) == 1){
                marker = new MarkerOptions().position(business).title(name).snippet(last.get(i));
            }
            else if (count.get(i) == 2){
                marker = new MarkerOptions().position(business).title(name).snippet(last.get(i) + " + " + (count.get(i)-1) + " friend");
            }
            else{
                marker = new MarkerOptions().position(business).title(name).snippet(last.get(i) + " + " + (count.get(i)-1) + " friends");
            }

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    Context context = getApplicationContext();

                    LinearLayout info = new LinearLayout(context);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(context);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(context);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });

            mMap.addMarker(marker);
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
