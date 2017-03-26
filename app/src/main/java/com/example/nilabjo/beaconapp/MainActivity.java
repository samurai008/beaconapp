package com.example.nilabjo.beaconapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        public final static String beacon_title = "com.example.myfirstapp.MESSAGE";
        private final static int REQUEST_ENABLE_BT = 1;
        // Local Proximity variable
        private ProximityManagerContract proximityManager;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        List<String> discoveredBeacons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if bluetooth is enabled



        KontaktSDK.initialize("rQEoSaXNyYVKTZWAddVKLmuMsWCBKsiX");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            proximityManager = new ProximityManager(this);
            proximityManager.setIBeaconListener(createIBeaconListener());
            proximityManager.setEddystoneListener(createEddystoneListener());
        } else {
//            RelativeLayout relativeLayout =
//                    (RelativeLayout) findViewById(R.id.rootlayout);
//
//            TextView textView = new TextView(this);
//            textView.setText("Switch on the Bluetooth and restart!");
//
//            relativeLayout.addView(textView);

            proximityManager = new ProximityManager(this);
            proximityManager.setIBeaconListener(createIBeaconListener());
            proximityManager.setEddystoneListener(createEddystoneListener());
        }

        // List of devices
        //String[] beacons = {"Innov8", "Internet One", "Lorem Ipsum"};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    proximityManager = new ProximityManager(this);
                    proximityManager.setIBeaconListener(createIBeaconListener());
                    proximityManager.setEddystoneListener(createEddystoneListener());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rootlayout);
                    TextView textView = new TextView(this);
                    textView.setText("Switch on the Bluetooth and restart!");

                    relativeLayout.addView(textView);
                }
//                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /* Scan Beacon Code */

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
        TextView radar_tv = (TextView) findViewById(R.id.radar);
        radar_tv.setText("Scanning...");
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                //Log.i("Sample", "IBeacon discovered: " + ibeacon.getProfile());
                if (!discoveredBeacons.isEmpty()) {
                    discoveredBeacons.clear();
                } else {
                    TextView radar_tv = (TextView) findViewById(R.id.radar);
                    radar_tv.setText("Beacons near you");
                }
                for (IBeaconDevice e : iBeacons) {
                    HttpHandler sh = new HttpHandler();
                    String url = "http://api.androidhive.info/contacts/";
                    String jsonStr = sh.makeServiceCall(url);

                    Log.i("string", "Response from url : " + jsonStr);

                    discoveredBeacons.add(e.getUniqueId());
                    Log.i("string", "ibeacons : " + e.getUniqueId());
                }
                ListAdapter device_adapter = new custom_card_adapter(MainActivity.this, discoveredBeacons);
                ListView dev_list_view = (ListView) findViewById(R.id.beacon_device_list);
                //device_adapter.notify();
                dev_list_view.setAdapter(device_adapter);
                dev_list_view.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                String beacons = String.valueOf(parent.getItemAtPosition(pos));

                                //Toast.makeText(MainActivity.this, beacons, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(view.getContext(), beacon_details.class);
                                intent.putExtra(beacon_title, beacons);
                                startActivity(intent);
                            }
                        }
                );
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString());
            }
        };
    }

    /* End of Scan Beacon code */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_bookmarks) {
            Intent bookmarksIntent = new Intent(this, bookmarks.class);
            startActivity(bookmarksIntent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
