package com.final_project.josh.parking_application;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.final_project.josh.parking_application.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
        new LatLng(-40,-168), new LatLng(71,136)
    );
    private static final int PLACE_PICKER_REQUEST = 1;
    private final int PROXIMITY_RADIUS = 10000;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProvideClient;

    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private ListView parking;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private ArrayList<PlaceInfo> parking_places = new ArrayList<PlaceInfo>();

    private ItemAdapter parking_places_adapter;

    @Override
    protected void onResume() {
        super.onResume();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        getLocationPermission();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            init();

        }
    }
    private void init(){

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this,0,this)
                .build();
        mGoogleApiClient.connect();

        mMap.setPadding(0,100,0,0);

        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    mSearchText.dismissDropDown();
                    geoLocate();
                    return true;
                }
                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getDeviceLocation();
            }
        });

        parking = (ListView) findViewById(R.id.parking);

        parking_places_adapter = new ItemAdapter(MapActivity.this, R.layout.custom_info_window, parking_places);

        parking_places_adapter.notifyDataSetChanged();

        parking.setAdapter(parking_places_adapter);

        parking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceInfo place = parking_places.get(position);
                startNavigation(place.getLatlng());
            }
        });

        hideSoftKeyboard(MapActivity.this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate(){
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException ex){}
        if(list.size()>0){
            Address address = list.get(0);
            mPlace = new PlaceInfo();
            mPlace.setName(address.getAddressLine(0));
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,mPlace);
        }
        hideSoftKeyboard(MapActivity.this);
    }
    private void getDeviceLocation(){
        mFusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProvideClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener(){
                    @Override
                    public void onComplete(@NonNull Task task){
                        if(task.isSuccessful()  && task.getResult() != null){

                            Location currentLocation = (Location) task.getResult();
                            mPlace = new PlaceInfo();
                            mPlace.setName("My Location");
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM, mPlace);
                        }else {
                            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            if (currentLocation != null) {
                                mPlace = new PlaceInfo();
                                mPlace.setName("My Location");
                                moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM, mPlace);
                            } else {
                                Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

            }
        }catch(SecurityException ex){

        }
    }

    private void moveCamera(LatLng latlng, float zoom, PlaceInfo placeInfo){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

        if(placeInfo != null) {
            try{
                String snippet = (placeInfo.getAddress() != null) ? placeInfo.getAddress() : "";

                MarkerOptions options = new MarkerOptions()
                        .position(latlng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMap.addMarker(options);
                mMarker = mMap.addMarker(options);

                Object dataTransfer[] = new Object[2];
                GetNearbyPlaceData getNearbyPlacesData = new GetNearbyPlaceData(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        parking_places.clear();
                        parking_places.addAll((ArrayList<PlaceInfo>) output);

                        parking_places_adapter.notifyDataSetChanged();
                        mSearchText.setText("");
                    }
                    @Override
                    public Marker getMarker(){
                        return mMarker;
                    }
                });
                String parking = "parking";
                String url = getUrl(latlng.latitude, latlng.longitude, parking);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);

            }catch (NullPointerException ex){}
            catch (Exception ex){}

        }
        else{
            mMap.addMarker(new MarkerOptions().position(latlng));
        }
        hideSoftKeyboard(MapActivity.this);
    }
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission(){
       String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
       if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
           if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
               mLocationPermissionsGranted = true;
               initMap();
           }
           else{
               ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSIONS_REQUEST_CODE);
           }
       }
       else{
           ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSIONS_REQUEST_CODE);
       }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSIONS_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i <grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }
    private void hideSoftKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard(MapActivity.this);
            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                places.release();
                return;
            }
            final Place place = places.get(0);
            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getAddress().toString());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setRating(place.getRating());
                mPlace.setWebsiteURI(place.getWebsiteUri().toString());
                places.release();
            }
            catch(NullPointerException ex){}

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude),DEFAULT_ZOOM,mPlace);
        }
    };
    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.google_maps_API_key));

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    private void startNavigation(LatLng latlng){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlng.latitude+","+latlng.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}