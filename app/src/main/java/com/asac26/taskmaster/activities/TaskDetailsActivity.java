package com.asac26.taskmaster.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.asac26.taskmaster.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity {
    public static final String TAG = "TaskDetailsActivity";
    private String s3ImageKey = "";
    private TextView locationTextView;
    static final int LOCATION_POLLING_INTERVAL = 5 * 1000;

    FusedLocationProviderClient locationProviderClient = null;

    Geocoder geocoder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        locationTextView = findViewById(R.id.taskLocationTextView);

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_POLLING_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                try {
                    String address = geocoder.getFromLocation(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude(),
                                    1)
                            .get(0)
                            .getAddressLine(0);
                    runOnUiThread(() -> {
                        locationTextView.setText("Current Location: " + address);
                    });
                    Log.i(TAG, "Repeating current location is: " + address);
                } catch (IOException ioe) {
                    Log.e(TAG, "Could not get subscribed location: " + ioe.getMessage(), ioe);
                }
            }
        };

        TaskDetails();
        BackButton();
    }

    private void TaskDetails(){
        TextView title=findViewById(R.id.textViewTitle);
        TextView body = findViewById(R.id.textViewDescription);
        TextView status = findViewById(R.id.textViewStatus);
        TextView team = findViewById(R.id.textViewTeam);
        ImageView taskImageView = findViewById(R.id.taskDetailsImageView);

        Intent intent=getIntent();
        String taskTitle=intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskStatus = intent.getStringExtra("taskStatus");
        String taskTeam = intent.getStringExtra("taskTeam");
        String taskImage = intent.getStringExtra("taskImage");

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_POLLING_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                try {
                    String address = geocoder.getFromLocation(
                                    locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude(),
                                    1)
                            .get(0)
                            .getAddressLine(0);

                    runOnUiThread(() -> {
                        locationTextView.setText("Current Location: " + address);
                    });

                    Log.i(TAG, "Repeating current location is: " + address);
                } catch (IOException ioe) {
                    Log.e(TAG, "Could not get subscribed location: " + ioe.getMessage(), ioe);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());

        if(taskTitle!=null)
            title.setText(taskTitle);
        if (taskBody != null)
            body.setText(taskBody);
        if (taskStatus != null)
            status.setText(taskStatus);
        if (taskTeam != null)
            team.setText(taskTeam);
        if (taskImage != null) {
            Log.d("TaskDetailActivity", "Image URL: " + taskImage);
            String imagePath = ""+taskImage;
            Log.d("imagePath", "Image path: " + imagePath);
            Glide.with(this).load(imagePath).into(taskImageView);
        }
    }

    private void BackButton(){
        Button detailsBackButton=findViewById(R.id.backButtonDescription);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,MainActivity.class);
            startActivity(backToHomeFromDetails);
        });
    }
}
