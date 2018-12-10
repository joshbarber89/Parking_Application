package com.final_project.josh.parking_application.models;

import android.app.Activity;
import android.content.Context;

import com.final_project.josh.parking_application.DatabaseBuilder;

public class User_Settings {
    private String mori;
    private String radius;
    private String longitude;
    private String latitude;
    private String auto;

    private static DatabaseBuilder settingsdb;

    public User_Settings(String mori, String radius, String longitude, String latitude, String auto) {
        this.mori = mori;
        this.radius = radius;
        this.longitude = longitude;
        this.latitude = latitude;
        this.auto = auto;
    }

    public boolean checkIfDefaultValuesNeeded(Context context){
        settingsdb = new DatabaseBuilder(context);
        User_Settings user_settings = settingsdb.getData();
        if(user_settings == null){
            return true;
        }
        return false;
    }
    public void setDefaultValues(Context context){
        settingsdb = new DatabaseBuilder(context);
        settingsdb.updateData("FALSE","10","-1","-1","FALSE");
        this.setMori("FALSE");
        this.setRadius("10");
        this.setLatitude("-1");
        this.setLongitude("-1");
        this.setAuto("FALSE");
    }
    public User_Settings getSettingValues(Context context){
        settingsdb = new DatabaseBuilder(context);
        User_Settings us = settingsdb.getData();
        return us;
    }
    public boolean updateSettingValues(Context context){
        settingsdb = new DatabaseBuilder(context);
        boolean saved = settingsdb.updateData(getMori(),getRadius(),getLongitude(),getLatitude(),getAuto());
        if(saved){
            return true;
        }
        return false;
    }
    public User_Settings(){

    }
    public String getMori() {
        return mori;
    }

    public void setMori(String mori) {
        this.mori = mori;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }
}
