package com.final_project.josh.parking_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.final_project.josh.parking_application.models.User_Settings;
import com.google.android.gms.maps.model.LatLng;


public class FindCar extends AppCompatActivity {

    private Button btnFindCar, btnCarFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_car);

        btnFindCar = (Button) findViewById(R.id.find_car);
        btnCarFound = (Button) findViewById(R.id.found_car);

        btnFindCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Settings us = new User_Settings();
                us = us.getSettingValues(FindCar.this);
                if(!us.getLongitude().equals("") && !us.getLatitude().equals("")){
                    startWalkNavigation(new LatLng(Double.parseDouble(us.getLatitude()),Double.parseDouble(us.getLongitude())));
                }
            }
        });

        btnCarFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Settings us = new User_Settings();
                us = us.getSettingValues(FindCar.this);
                us.setLatitude("-1");
                us.setLongitude("-1");
                us.updateSettingValues(FindCar.this);
                Intent main = new Intent(FindCar.this,MainActivity.class);
                startActivity(main);

            }
        });

    }
    private void startWalkNavigation(LatLng latlng){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlng.latitude+","+latlng.longitude+"&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
