package com.final_project.josh.parking_application;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Globals extends AppCompatActivity  {


    private String MEASUREMENT = "metric";
    private int PROXIMITY_RADIUS = 10000;
    private boolean AUTO_SELECT = false;
    private static double PARKING_LOCATION_LAT = -1;
    private static double PARKING_LOCATION_LNG = -1;


    public String getMEASUREMENT() {
        return MEASUREMENT;
    }

    public int getPROXIMITY_RADIUS() {
        return PROXIMITY_RADIUS;
    }

    public boolean getAUTO_SELECT(){
        return AUTO_SELECT;
    }

    public double getPARKING_LOCATION_LAT(){
        return PARKING_LOCATION_LAT;
    }

    public double getPARKING_LOCATION_LNG(){
        return PARKING_LOCATION_LNG;
    }

    public void setMEASUREMENT(String measurement){
        this.MEASUREMENT = measurement;
    }

    public void setPROXIMITY_RADIUS(int radius){
        this.PROXIMITY_RADIUS = radius;
    }

    public void setAUTO_SELECT(boolean auto_select){
        this.AUTO_SELECT = auto_select;
    }

    public void setPARKING_LOCATION_LAT(double parking_lat){
        this.PARKING_LOCATION_LAT = parking_lat;
    }

    public void setPARKING_LOCATION_LNG(double parking_lng){
        this.PARKING_LOCATION_LNG = parking_lng;
    }



}
