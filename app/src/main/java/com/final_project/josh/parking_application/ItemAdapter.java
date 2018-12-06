package com.final_project.josh.parking_application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.final_project.josh.parking_application.models.PlaceInfo;

public class ItemAdapter extends ArrayAdapter {

    private ArrayList<PlaceInfo> objects;
    private String measurement;

    public ItemAdapter(Context context, int textViewResourceId, ArrayList<PlaceInfo> objects, String measurement) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.measurement = measurement;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_list_locations, null);
        }

        PlaceInfo p = objects.get(position);

        if (p != null) {

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView address = (TextView) v.findViewById(R.id.address);
            TextView distance = (TextView) v.findViewById(R.id.distance);
            name.setTextColor(Color.BLACK);
            address.setTextColor(Color.BLACK);
            distance.setTextColor(Color.BLACK);
            if (name != null){
                name.setText(p.getName());
            }
            if (address != null){
                address.setText(p.getAddress());
            }
            if (distance != null){
                String unit = "Mi";
                if(measurement.equals("metric")){
                    unit = "Km";
                }
                distance.setText(Double.toString(round(p.getDistance(),2))+" "+unit);
            }
        }

        return v;

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
