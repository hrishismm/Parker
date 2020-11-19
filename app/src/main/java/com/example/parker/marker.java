package com.example.parker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class marker extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {
private  static final String TAG="marker";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private boolean mLocationgranted = false;
    private GoogleMap nmap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ImageView mgps;
    ArrayList<LatLng>arrayList=new ArrayList<>();
    LatLng Delhi=new LatLng(28.704060,77.102493);
    LatLng Chennai=new LatLng(13.082680,80.270721);
    LatLng Mumbai=new LatLng(19.075983,72.877655);
    ArrayList<String> title=new ArrayList<String>();

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        getLocationPermission();
        editText=(EditText)findViewById(R.id.input_search);
        mgps=(ImageView)findViewById(R.id.loc);
        Places.initialize(getApplicationContext(),"AIzaSyCCd9RfsdTepSkf-cfNDGMO9R-uqLBfFNQ");
       // arrayList.add(Mumbai);
       // arrayList.add(Delhi);
        //arrayList.add(Chennai);

//        title.add("Mumbai");
  //      title.add("Delhi");
    //    title.add("Chennai");


    }

    private void getdevicelocation() {
        Log.d(TAG, "getdevicelocation:Gets location of the user");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationgranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if ((task.isSuccessful())) {
                            Log.d(TAG, "onComplete:Found Location");
                            Location currentLocation = (Location) task.getResult();
                            movecamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                        } else {
                            Log.d(TAG, "onComplete:Found NO  Location");
                            Toast.makeText(marker.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getdevicelocation:Security Exceptiom");

        }

    }
    private void movecamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "movecamera:moving camera t=latitude is" + latlng.latitude + "lomgitude is" + latlng.longitude);
        Toast.makeText(this, "Latitude is"+latlng.latitude+"Longitude is"+latlng.longitude, Toast.LENGTH_SHORT).show();
        nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if(!title.equals("My Location"))
        {
            /*Dropping a pin*/

            MarkerOptions markerOptions=new MarkerOptions().position(latlng).title(title);
            nmap.addMarker(markerOptions);
        }
        hidekeyboard();
    }


    private void getLocationPermission() {
        Log.d(TAG, "Getting Permissions:getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationgranted = true;
                initmap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }
    private void initmap() {
        Log.d(TAG, "Map is initializing:map is initializing");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapmarker);
        mapFragment.getMapAsync(marker.this);

    }
    private void init()
    {
        Log.d(TAG,"init:Initializingssss");
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE||keyEvent.getAction()==keyEvent.ACTION_DOWN||keyEvent.getAction()==KeyEvent.KEYCODE_ENTER)
                {
                  //  Toast.makeText(marker.this, "GeoLocating started", Toast.LENGTH_SHORT).show();
                    geolocate();
                }
                return  false;
            }


        });
/*editText.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
        //Create Intent
        Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(MapActivity.this);
        startActivityForResult(intent,100);
    }
});
*/
        mgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"GPS:GPS icon");
                getdevicelocation();
            }
        });
        hidekeyboard();
    }
    private  void  geolocate()
    {
        Log.d(TAG,"Geolocating:Started");
        String searchString=editText.getText().toString();
        Geocoder geocoder=new Geocoder(marker.this);

        List<Address> list= new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e)
        {
            Log.e(TAG,"Geolocating:IOException"+e.getMessage());
        }
        if(list.size()>0)
        {
            Address address=list.get(0);
            Log.d(TAG,"Geolocating:Found location"+address.toString());
       //     Toast.makeText(this, "Adress"+address.toString(), Toast.LENGTH_SHORT).show();
            movecamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult:called");
        mLocationgranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "Permission:Failed");
                            mLocationgranted = false;
                            return;
                        }
                    }
                    Log.d(TAG, "Permissiom:grantedG");
                    mLocationgranted = true;
                    //initialize our maps
                    initmap();
                }
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Map is ready:map is ready");
        nmap = googleMap;
       for(int i=0;i<arrayList.size();i++) {


            nmap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(String.valueOf(title.get(i))));


            nmap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
        }
       nmap.setOnMapLongClickListener(this);
       nmap.setOnMarkerDragListener(this);
        nmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markertitle=marker.getTitle();
                Intent i=new Intent(marker.this,DetailsActivity.class);
                i.putExtra("title",markertitle);
                startActivity(i);
                return false;
            }
        });



        if (mLocationgranted) {
            getdevicelocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            nmap.setMyLocationEnabled(true);
            nmap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }

    }
    private  void hidekeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        nmap.clear();
        nmap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        Intent intent=new Intent(getApplicationContext(),NewNoteActivity.class);
        String a= String.valueOf(latLng.latitude);
        String b= String.valueOf(latLng.longitude);

        intent.putExtra("Latitude",a);

        intent.putExtra("Longitude",b);
        startActivity(intent);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
            LatLng latLng=marker.getPosition();
       // Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();

    }

}