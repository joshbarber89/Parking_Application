package com.final_project.josh.parking_application;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.final_project.josh.parking_application.models.User_Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends Globals {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            init();
        }

    }
    private void init(){
        Button findParking = findViewById(R.id.findParking);
        Button settings = findViewById(R.id.settings);
        User_Settings us = new User_Settings();
        if(us.checkIfDefaultValuesNeeded(this)){
            us.setDefaultValues(this);
            setSettings(us);
        }
        else{
            us = us.getSettingValues(this);
            setSettings(us);
        }


        findParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: checking google services version");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this,"You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void setSettings(User_Settings us){
        if(us.getAuto().equals("TRUE")) {
            setAUTO_SELECT(true);
        }else{
            setAUTO_SELECT(false);
        }
        if(us.getMori().equals("TRUE")){
            setMEASUREMENT("imperial");
        }
        else{
            setMEASUREMENT("metric");
        }
        if(!us.getRadius().equals("") && Integer.parseInt(us.getRadius()) >= 0){
            setPROXIMITY_RADIUS(Integer.parseInt(us.getRadius()));
        }
        else{
            setPROXIMITY_RADIUS(10);
        }
        if(!us.getLatitude().equals("") && Double.parseDouble(us.getLatitude()) != -1){
            setPARKING_LOCATION_LAT(Double.parseDouble(us.getLatitude()));
        }
        else{
            setPARKING_LOCATION_LAT(-1);
        }
        if(!us.getLongitude().equals("") && Double.parseDouble(us.getLongitude()) != -1){
            setPARKING_LOCATION_LNG(Double.parseDouble(us.getLongitude()));
        }
        else{
            setPARKING_LOCATION_LNG(-1);
        }
    }
}
