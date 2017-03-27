package com.example.nikki.powerescue;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    MyDbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;
    public static  String fulladd;
    Button sendBtn;
    EditText txtphoneNo;
    // Google Map
    private GoogleMap googleMap;
    GPSTracker gps;

    public static double latitude;
    public static double longitude;

    double r = 300;
    Location current;
    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new MyDbHelper(this);


        Button button1 = (Button) findViewById(R.id.button1);
         Button button2 = (Button) findViewById(R.id.button);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, Contacts.class);
                MainActivity.this.startActivity(myIntent);

            }
        });

       button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDb = mHelper.getReadableDatabase();
                String[] columns = new String[] {"_id",com.example.nikki.powerescue.MyDbHelper.COL_DATE,com.example.nikki.powerescue.MyDbHelper.COL_DATE};
                mCursor = mDb.query(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
                mCursor.moveToFirst();

                while(!mCursor.isAfterLast())
                {
                    String number=mCursor.getString(1);
                    Toast.makeText(getApplicationContext(),"number"+number, Toast.LENGTH_LONG).show();
                    sendSMSMessage(fulladd,number);
                    mCursor.moveToNext();
                }

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myIntent = new Intent(MainActivity.this, Maps.class);
                MainActivity.this.startActivity(myIntent);

            }
        });



        try {
            // Loading map
            current = new Location("LocationManager.GPS_PROVIDER");
            // tts = new TextToSpeech(this, this);
            gps = new GPSTracker(MainActivity.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                current.setLatitude(latitude);
                current.setLongitude(longitude);

                Toast.makeText(getApplicationContext(), "latitiude : " + latitude + "longitude :" + longitude, Toast.LENGTH_LONG).show();


                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses;
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        fulladd = "In Danger:"+ address.getAddressLine(0)+address.getAddressLine(1)+"latitude:"+latitude+"  "+"longitude"+longitude;
                        double lat = address.getLatitude();
                        double lon = address.getLongitude();
                        Toast.makeText(getApplicationContext(),fulladd, Toast.LENGTH_LONG).show();

                       /* mDb = mHelper.getReadableDatabase();
                        String[] columns = new String[] {"_id",com.example.nikki.powerescue.MyDbHelper.COL_DATE,com.example.nikki.powerescue.MyDbHelper.COL_DATE};
                        mCursor = mDb.query(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
                        mCursor.moveToFirst();

                        while(!mCursor.isAfterLast())
                        {
                            String number=mCursor.getString(1);
                            Toast.makeText(getApplicationContext(),"number"+number, Toast.LENGTH_LONG).show();
                            sendSMSMessage(fulladd,number);
                            mCursor.moveToNext();
                        }*/

                    }
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {

            mDb = mHelper.getReadableDatabase();
            String[] columns = new String[] {"_id",com.example.nikki.powerescue.MyDbHelper.COL_DATE,com.example.nikki.powerescue.MyDbHelper.COL_DATE};
            mCursor = mDb.query(com.example.nikki.powerescue.MyDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast())
            {
                String number=mCursor.getString(1);
                Toast.makeText(getApplicationContext(),"number"+number, Toast.LENGTH_LONG).show();
                sendSMSMessage(fulladd,number);
                mCursor.moveToNext();
            }
            event.startTracking(); // Needed to track long presses
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void sendSMSMessage(String address,String phoneNo) {
        Log.i("Send SMS", "");

        String message =address;
        // String message ="hello";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
