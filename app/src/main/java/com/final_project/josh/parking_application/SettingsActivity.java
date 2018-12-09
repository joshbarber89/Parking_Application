package com.final_project.josh.parking_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static SeekBar seekBar;
    private static TextView sr;
    private static Switch switcher;
    private static TextView metric;
    private static TextView imperial;
    private static TextView longans;
    private static TextView latans;
    private static Button save;
    private static Button exit;
    private static TextView autoon;
    private static TextView autooff;
    private static Switch autoswitch;
    private static DatabaseBuilder settingsdb;

    private static String storemori;
    private static String storerange;
    private static String storelong;
    private static String storelat;
    private static String storeauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        sr = (TextView)findViewById(R.id.sr);
        switcher = (Switch)findViewById(R.id.switchmori);
        metric = (TextView)findViewById(R.id.metric);
        imperial = (TextView)findViewById(R.id.imperial);
        sr.setText("Search Radius Proximity: ");
        imperial.setVisibility(TextView.INVISIBLE);
        longans = (TextView)findViewById(R.id.longans);
        latans = (TextView)findViewById(R.id.latans);
        save = (Button)findViewById(R.id.save);
        exit = (Button)findViewById(R.id.exit);
        autoon = (TextView)findViewById(R.id.autoon);
        autooff = (TextView)findViewById(R.id.autooff);
        autoon.setVisibility(TextView.INVISIBLE);
        autoswitch = (Switch)findViewById(R.id.switchauto);
        settingsdb = new DatabaseBuilder(this);

        storemori = "FALSE";
        storeauto = "FALSE";
        storerange = "";
        storelong = "";
        storelat = "";


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        autoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    autoon.setVisibility(TextView.VISIBLE);
                    autooff.setVisibility(TextView.INVISIBLE);
                    storeauto = "TRUE";
                }
                else{
                    autoon.setVisibility(TextView.INVISIBLE);
                    autooff.setVisibility(TextView.VISIBLE);
                    storeauto = "FALSE";
                }
            }
        });

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    imperial.setVisibility(TextView.VISIBLE);
                    metric.setVisibility(TextView.INVISIBLE);
                    storemori = "TRUE";
                }
                else {
                    imperial.setVisibility(TextView.INVISIBLE);
                    metric.setVisibility(TextView.VISIBLE);
                    storemori = "FALSE";
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressvalue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressvalue = progress;
                sr.setText("Search Radius Proximity: " + progressvalue);
                storerange = String.valueOf(progressvalue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sr.setText("Search Radius Proximity: " + progressvalue);
                storerange = String.valueOf(progressvalue);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storelat = String.valueOf(latans.getText());
                storelong = String.valueOf(longans.getText());

                boolean insertData = settingsdb.addData(storemori, storerange, storelong, storelat, storeauto);

                if (insertData == true){
                    Toast.makeText(SettingsActivity.this, "DATA SUCCESSFULLY STORED!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "ERROR! NOT SAVED.", Toast.LENGTH_LONG).show();
                }


            }
        });

    }


}
