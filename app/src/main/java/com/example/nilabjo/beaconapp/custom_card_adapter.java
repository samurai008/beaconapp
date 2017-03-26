package com.example.nilabjo.beaconapp;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nilabjo on 19/12/16.
 */

public class custom_card_adapter extends ArrayAdapter<String> {

    public custom_card_adapter(Context context, List<String> beacon) {
        super(context, R.layout.custom_card, beacon);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View cstView = myInflater.inflate(R.layout.custom_card, parent, false);

        String singleItem = getItem(position);
        TextView title = (TextView) cstView.findViewById(R.id.beacon_title);
        //TextView noOfDevices = (TextView) cstView.findViewById(R.id.no_of_devices);

        title.setText(singleItem);
        //noOfDevices.setText("");

        return cstView;
    }
}
