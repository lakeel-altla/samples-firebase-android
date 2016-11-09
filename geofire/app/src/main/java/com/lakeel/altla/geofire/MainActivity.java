package com.lakeel.altla.geofire;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE:
        // Firebase requires user authentication.
        // This sample app does not authenticate to Firebase.
        // Therefore, you need to change Firebase rules that does not require authentication.
        // Rules here(https://firebase.google.com/docs/database/security/?hl=ja)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofire");
        final GeoFire geoFire = new GeoFire(ref);

        // Add
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add 3 locations to Firebase.
                geoFire.setLocation("geofire-01", new GeoLocation(35.86, 139.73), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Log.e(TAG, error.getMessage(), error.toException());
                    }
                });
                geoFire.setLocation("geofire-02", new GeoLocation(35.66, 139.92));
                geoFire.setLocation("geofire-03", new GeoLocation(80.51, -120.92));
            }
        });

        // Remove
        Button removeButton = (Button) findViewById(R.id.buttonRemove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove 3 locations from Firebase.
                geoFire.removeLocation("firebase-01");
                geoFire.removeLocation("firebase-02");
                geoFire.removeLocation("firebase-03");
            }
        });

        // Search
        Button searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Query Settings.
                // This is query that searches for locations in the radius 200 KM from the specified point.

                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(35, 139), 200);
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        Log.d(TAG, "onKeyEntered:key = " + key + " " + location.toString());
                    }

                    @Override
                    public void onKeyExited(String key) {
                        // May be found 2 locations (geofire-01, geofire-02).
                        Log.d(TAG, "onKeyExited:key = " + key);
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        Log.d(TAG, "onKeyMoved:key = " + key + " " + location.toString());
                    }

                    @Override
                    public void onGeoQueryReady() {
                        Log.d(TAG, "onGeoQueryReady");
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                        Log.e(TAG, "onGeoQueryError", error.toException());
                    }
                });
            }
        });
    }
}
