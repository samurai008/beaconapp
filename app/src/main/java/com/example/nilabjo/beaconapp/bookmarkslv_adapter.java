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

public class bookmarkslv_adapter extends ArrayAdapter<String> {

    public bookmarkslv_adapter(Context context, List<String> beacon) {
        super(context, R.layout.bookmarkslv_layout, beacon);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View cstView = myInflater.inflate(R.layout.bookmarkslv_layout, parent, false);

        String singleItem = getItem(position);
        TextView title = (TextView) cstView.findViewById(R.id.bookmarktitletv);
        TextView creationDate = (TextView) cstView.findViewById(R.id.createdontv);

        title.setText(singleItem);
        creationDate.setText("Created on 9/11/2001");

        return cstView;
    }
}
