package com.example.nikki.powerescue;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class Maps extends Activity {
    MyDbHelper mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    SimpleCursorAdapter mAdapter;
    String fulladd;
    Button sendBtn;
    EditText txtphoneNo;
    // Google Map
    private GoogleMap googleMap;
    GPSTracker gps;

    double latitude1;
    double longitude1;
    double r = 300;
    Location current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mHelper = new MyDbHelper(this);

        sendBtn = (Button) findViewById(R.id.button);
        txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);


        try {
            // Loading map

            initilizeMap();
            googleMap.setMyLocationEnabled(true);

            Marker marker1 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(MainActivity.latitude,MainActivity.longitude ))
                    .title(MainActivity.fulladd)
                    .snippet(""));


            current = new Location("LocationManager.GPS_PROVIDER");
            // tts = new TextToSpeech(this, this);
            gps = new GPSTracker(Maps.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude1 = gps.getLatitude();
                longitude1 = gps.getLongitude();
                current.setLatitude(latitude1);
                current.setLongitude(longitude1);

                //Toast.makeText(getApplicationContext(), "latitiude : " + latitude + "longitude :" + longitude, Toast.LENGTH_LONG).show();


                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses;
                    addresses = geocoder.getFromLocation(latitude1, longitude1, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        fulladd = "In Danger:"+ address.getAddressLine(0)+address.getAddressLine(1)+"latitude"+latitude1+"  "+"longitude"+longitude1;
                      /*  String locality = address.getLocality();
                        String city = address.getCountryName();
                        String region_code = address.getCountryCode();
                        String zipcode = address.getPostalCode();*/
                        double lat = address.getLatitude();
                        double lon = address.getLongitude();



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
    /*

    protected void sendSMS(String address) {
        Log.i("Send SMS", "");

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");

        smsIntent.putExtra("address"  , new String ("9731371283"));
        smsIntent.putExtra("sms_body"  , address);
        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Maps.this,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
*/


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

           /*LatLng location = googleMap.getMyLocation();

            LatLng myLocation = null;

            if (current != null) {
                myLocation = new LatLng(latitude,
                        longitude);
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                    SyncStateContract.Constants.MAP_ZOOM));*/

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude1,longitude1 ))
                    .title(fulladd)
                    .snippet(""));

            event.startTracking(); // Needed to track long presses
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void sendSMSMessage(String address,String phoneNo) {
        Log.i("Send SMS", "");

       // String phoneNo = txtphoneNo.getText().toString();
       //String phoneNo = "9741047625";


        String message =address;

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
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            // final LatLng TutorialsPoint = new LatLng(21 , 57);
            //Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

}