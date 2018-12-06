package com.final_project.josh.parking_application;

import android.os.AsyncTask;

import com.final_project.josh.parking_application.models.PlaceInfo;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaceData extends AsyncTask<Object, String, String>{

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;
    private AsyncResponse delegate = null;
    private ArrayList<PlaceInfo> arrayList = new ArrayList<PlaceInfo>();
    private Marker mMarker;

    public GetNearbyPlaceData(AsyncResponse  asyncResponse){
        this.delegate = asyncResponse;
    }
    @Override
    protected String doInBackground(Object... objects){
        mMarker = delegate.getMarker();
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser(mMarker);
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
        Collections.sort(arrayList, new SortByDistance());
        delegate.processFinish(arrayList);

    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble( googlePlace.get("lat"));
            double lng = Double.parseDouble( googlePlace.get("lng"));
            double distance = Double.parseDouble( googlePlace.get("distance"));

            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            PlaceInfo mPlace = new PlaceInfo();
            mPlace.setName(placeName);
            mPlace.setAddress(vicinity);
            mPlace.setLatlng(latLng);
            mPlace.setDistance(distance);

            arrayList.add(mPlace);

            mMap.addMarker(markerOptions);
            builder.include(latLng);

        }
        builder.include(mMarker.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,50);
        mMap.animateCamera(cu);
    }
    class SortByDistance implements Comparator<PlaceInfo>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(PlaceInfo a, PlaceInfo b)
        {
            if (a.getDistance() < b.getDistance()) return -1;
            if (a.getDistance() > b.getDistance()) return 1;
            return 0;
        }
    }
}
