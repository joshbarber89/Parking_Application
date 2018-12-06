package com.final_project.josh.parking_application;

import android.support.v7.app.AppCompatActivity;

public class Globals extends AppCompatActivity {
    private String MEASUREMENT = "metric";
    private int PROXIMITY_RADIUS = 10000;
    private boolean AUTO_SELECT = false;

    public String getMEASUREMENT() {
        return MEASUREMENT;
    }

    public int getPROXIMITY_RADIUS() {
        return PROXIMITY_RADIUS;
    }

    public boolean getAUTO_SELECT(){
        return AUTO_SELECT;
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
}
