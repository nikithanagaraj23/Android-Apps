package com.example.nikki.tringg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import static com.example.nikki.tringg.R.raw.tring;

public class Maps extends Activity {

    // Google Map
    GPSTracker gps;
    //   double alarm_latitutde = 12.8905978;
    //  double alarm_longitude = 77.6070198;
    double latitude;
    double longitude;

    double alarm_latitutde;
    double alarm_longitude;
    double r = 300;
    Location current;
    private GoogleMap googleMap;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        try {
            // Loading map
            initilizeMap();
            googleMap.setMyLocationEnabled(true);
            Location location=googleMap.getMyLocation();


        } catch (Exception e) {
            e.printStackTrace();
        }

        Button setalarm = (Button) findViewById(R.id.set_alarm);
        setalarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                EditText add = (EditText) findViewById(R.id.editText);
                TextView display = (TextView) findViewById(R.id.display);
                TextView display1 = (TextView) findViewById(R.id.display1);

                Toast.makeText(getApplicationContext(), add.getText().toString(), Toast.LENGTH_LONG).show();
                String check = add.getText().toString().trim();
               // display.setText("Alarm has been set to:");
//                display1.setText(check);

                if (check.equalsIgnoreCase("home")) {
                    alarm_latitutde = 12.8905978;
                    alarm_longitude = 77.6070198;

                    Toast.makeText(getApplicationContext(), "alarm st" + alarm_latitutde + " " + alarm_longitude, Toast.LENGTH_LONG).show();


                } else if (check.equalsIgnoreCase("college")) {
                     alarm_latitutde=12.933474;
                     alarm_longitude=77.535637;

                    //alarm_latitutde = 12.9338354;
                    //alarm_longitude = 77.5363723;

                    Toast.makeText(getApplicationContext(), "alarm set" + alarm_latitutde + " " + alarm_longitude, Toast.LENGTH_LONG).show();


                }

               /* else if (check.equalsIgnoreCase("bblock")) {
                    // alarm_latitutde=12.933474;
                    // alarm_longitude=77.535637;

                    alarm_latitutde = 12.9338354;
                    alarm_longitude = 77.5363723;

                    Toast.makeText(getApplicationContext(), "alarm set" + alarm_latitutde + " " + alarm_longitude, Toast.LENGTH_LONG).show();


                }
                */

                Button startalarm = (Button) findViewById(R.id.start);
                startalarm.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        current = new Location("LocationManager.GPS_PROVIDER");
                        // tts = new TextToSpeech(this, this);
                        gps = new GPSTracker(Maps.this);
                        // check if GPS enabled
                        try {
                            startCounter();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

    }


    public void startCounter() throws InterruptedException {
        final CountDownTimer start1 = new CountDownTimer(1000000, 3000) {

            public void onTick(long millisUntilFinished) {

                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    current.setLatitude(latitude);
                    current.setLongitude(longitude);

                    Toast.makeText(getApplicationContext(), "latitiude : " + latitude + "longitude :" + longitude, Toast.LENGTH_SHORT).show();


                    if ((latitude <= alarm_latitutde + 0.005 && latitude >= alarm_latitutde - 0.005) && (longitude <= alarm_longitude + 0.005 && longitude >= alarm_longitude - 0.005)) {
                        Toast.makeText(getApplicationContext(), "WORKS!!!" + latitude + "  " + alarm_latitutde + "    " + longitude + "   " + alarm_longitude, Toast.LENGTH_LONG).show();

                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), tring);
                        mp.start();

                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(500);

                    } else {

                        Toast.makeText(getApplicationContext(), "NOPE" + latitude + " " + longitude, Toast.LENGTH_LONG).show();

                    }

                }
            }


            public void onFinish() {

                // fire a Toast
                Toast.makeText(getApplicationContext(), "Time up",
                        Toast.LENGTH_LONG).show();

            }
        }.start();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                start1.cancel();
            }
        });
    }


    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
           // googleMap = ((MapFragment) getFragmentManager().findFragmentById(
          //          R.id.map)).getMap();
           // final LatLng TutorialsPoint = new LatLng(21 , 57);
            //Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
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
