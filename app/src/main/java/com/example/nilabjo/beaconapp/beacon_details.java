package com.example.nilabjo.beaconapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class beacon_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.beacon_title);
        TextView beacon_t = (TextView) findViewById(R.id.beacon_title);
        beacon_t.setText(message);


        List<String> users = new ArrayList<>();
        users.add("Nilabjo");

        getSupportActionBar().setTitle(message);

        ListAdapter device_adapter = new checkedinusers_adapter(this, users);
        ListView dev_list_view = (ListView) findViewById(R.id.userschecked);
        //device_adapter.notify();
        dev_list_view.setAdapter(device_adapter);
    }

}
