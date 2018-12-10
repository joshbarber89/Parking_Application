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

import com.final_project.josh.parking_application.models.User_Settings;

public class SettingsActivity extends AppCompatActivity {

    private static SeekBar seekBar;
    private static TextView sr;
    private static Switch measurement_switch;
    private static TextView mesurement_text;
    private static Button save;
    private static Button exit;
    private static TextView auto_select_bool;
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

        settingsdb = new DatabaseBuilder(this);

        User_Settings user_settings = settingsdb.getData();

        //Measurement Switch
        measurement_switch = (Switch)findViewById(R.id.switchmori);
        mesurement_text = (TextView)findViewById(R.id.mesurement_text);

        //Proximity
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        sr = (TextView)findViewById(R.id.sr);

        //Auto Select
        auto_select_bool = (TextView)findViewById(R.id.auto_select_bool);
        autoswitch = (Switch)findViewById(R.id.switchauto);

        //longans = (TextView)findViewById(R.id.longans);
        //latans = (TextView)findViewById(R.id.latans);

        //Save or Exit
        save = (Button)findViewById(R.id.save);
        exit = (Button)findViewById(R.id.exit);



        storemori = "FALSE";
        storeauto = "FALSE";
        storerange = "";
        storelong = "-1";
        storelat = "-1";

        if(!user_settings.getMori().equals("") && user_settings.getMori().equals("TRUE")) {
            mesurement_text.setText(getResources().getText(R.string.imperial));
            measurement_switch.setChecked(true);
            storemori = user_settings.getMori();
        }

        //Measurement Switch
        measurement_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mesurement_text.setText(getResources().getText(R.string.imperial));
                    storemori = "TRUE";
                }
                else {
                    mesurement_text.setText(getResources().getText(R.string.metric));
                    storemori = "FALSE";
                }
            }
        });
        if(!user_settings.getRadius().equals("") && Integer.parseInt(user_settings.getRadius()) > 0) {
            seekBar.setProgress(Integer.parseInt(user_settings.getRadius()));
            sr.setText(getResources().getText(R.string.seek_proximity) +": "+ user_settings.getRadius()+" km/mi");
            storerange = user_settings.getRadius();
        }
        //Proximity
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressvalue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressvalue = progress;
                sr.setText(getResources().getText(R.string.seek_proximity) +": "+ Integer.toString(progressvalue)+" km/mi");
                storerange = String.valueOf(progressvalue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sr.setText(getResources().getText(R.string.seek_proximity) +": "+ Integer.toString(progressvalue)+"km/mi");
                storerange = String.valueOf(progressvalue);
            }
        });

        if(!user_settings.getAuto().equals("") && user_settings.getAuto().equals("TRUE")){
            autoswitch.setChecked(true);
            auto_select_bool.setText(getResources().getText(R.string.on));
            storeauto = user_settings.getAuto();
        }
        //Auto Select
        autoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    auto_select_bool.setText(getResources().getText(R.string.on));
                    storeauto = "TRUE";
                }
                else{
                    auto_select_bool.setText(getResources().getText(R.string.off));
                    storeauto = "FALSE";
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storelat = String.valueOf(storelat);
                storelong = String.valueOf(storelong);

                boolean insertData = settingsdb.updateData(storemori, storerange, storelong, storelat, storeauto);

                if (insertData == true){
                    Toast.makeText(SettingsActivity.this, "DATA SUCCESSFULLY STORED!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SettingsActivity.this, "ERROR! NOT SAVED.", Toast.LENGTH_LONG).show();
                }


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


}
