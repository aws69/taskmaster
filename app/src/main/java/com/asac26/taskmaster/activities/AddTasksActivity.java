package com.asac26.taskmaster.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.asac26.taskmaster.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTasksActivity extends AppCompatActivity {
    int x=1;
    public static final String TAG="AddTaskActivity";
    static final int LOCATION_POLLING_INTERVAL = 5 * 1000;
    CompletableFuture<List<Team>> teamFuture=new CompletableFuture<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";
    FusedLocationProviderClient locationProviderClient = null;
    Geocoder geocoder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        activityResultLauncher = getImagePickingActivityResultLauncher();

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(location ->
        {
            if (location == null) {
                Log.e(TAG, "Location CallBack was null");
            }
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "Our userLatitude: " + location.getLatitude());
            Log.i(TAG, "Our userLongitude: " + location.getLongitude());
        });

        locationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        });

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
                    Log.i(TAG, "Repeating current location is: " + address);
                } catch (IOException ioe) {
                    Log.e(TAG, "Could not get subscribed location: " + ioe.getMessage(), ioe);
                }
            }
        };

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());

        setUpSpinner();
        setUpSubmitButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
        updateImageButtons();
        backButton();
    }

    private void setUpSpinner() {
        // TaskStateSpinner
        Spinner taskCategorySpinner = findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        // TeamSpinner
        Spinner teamSpinner = findViewById(R.id.teamSpinner);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                successResponse -> {
                    Log.i(TAG, "Read Team Successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : successResponse.getData()) {
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    teamFuture.complete(teams);
                    runOnUiThread(() -> teamSpinner.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            teamNames
                    )));
                },
                failure ->
                {
                    teamFuture.complete(null);
                    Log.i(TAG, "Failed to read team");
                }
        );
    }
    private void setUpSubmitButton() {
        Button saveButton = findViewById(R.id.addTaskButton);
        saveButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String title=((EditText) findViewById(R.id.taskNameEdite)).getText().toString();
            String body= ((EditText) findViewById(R.id.descriptionEdite)).getText().toString();
            Spinner teamSpinner = findViewById(R.id.teamSpinner);
            String selectedTeamString= teamSpinner.getSelectedItem().toString();

            List<Team> teams=null;
            try {
                teams=teamFuture.get();
            }catch (InterruptedException e){
                Log.e(TAG,"Interruption exception while getting teams");
            }catch (ExecutionException ee){
                Log.e(TAG,"Execution exception while getting teams");
            }

            Team selectedTeams= teams.stream().filter(c->c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);
            locationProviderClient.getLastLocation().addOnSuccessListener(location ->
                            {
                                if (location == null) {
                                    Log.e(TAG, "Location CallBack was null");
                                }
                                String currentLatitude = Double.toString(location.getLatitude());
                                String currentLongitude = Double.toString(location.getLongitude());
                                Log.i(TAG, "Our userLatitude: " + location.getLatitude());
                                Log.i(TAG, "Our userLongitude: " + location.getLongitude());
                                SubmitButton(title, body, currentLatitude, currentLongitude, selectedTeams, s3ImageKey);
                            }
                    )
                    .addOnCanceledListener(() ->
                            Log.e(TAG, "Location request was Canceled"))
                    .addOnFailureListener(failure ->
                            Log.e(TAG, "Location request failed, Error was: " + failure.getMessage(), failure.getCause()))
                    .addOnCompleteListener(complete ->
                            Log.e(TAG, "Location request Completed"));
        });
    }

    private void SubmitButton(String title, String body, String latitude, String longitude, Team selectedTeams, String imageS3Key){
        Spinner taskCategorySpinner = findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        Task newTask=Task.builder()
                .name(title)
                .body(body)
                .state((TaskState) taskCategorySpinner.getSelectedItem())
                .teamTask(selectedTeams)
                .taskImageS3Key(imageS3Key)
                .taskLatitude(latitude)
                .taskLongitude(longitude)
                .build();
        Amplify.API.mutate(
                ModelMutation.create(newTask),
                successResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): created a task successfully"),
                failResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): failed to create a task"+failResponse)
        );
        Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
        TextView count=findViewById(R.id.counter);
        count.setText(String.valueOf(x++));
    }

    private void backButton(){
        Button addTaskBackButton= findViewById(R.id.backButtonAdd);
        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTasksActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }

    private void setUpAddImageButton(){
        Button addImageButton = findViewById(R.id.buttonAddImage);
        addImageButton.setOnClickListener(b ->
                launchImageSelectionIntent());

    }

    private void launchImageSelectionIntent()
    {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFilePickingIntent);

    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher()
    {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                Button addImageButton = findViewById(R.id.buttonAddImage);
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);

                                            switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);

                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());

                    s3ImageKey =success.getKey();
                    updateImageButtons();
                    ImageView taskImageView = findViewById(R.id.taskImageImageView);
                    InputStream pickedImageInputStreamCopy = null;
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure ->
                        Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage())
        );
    }



    private void setUpDeleteImageButton()
    {
        Button deleteImageButton = findViewById(R.id.buttonDeletImage);
        String s3ImageKey = this.s3ImageKey;
        deleteImageButton.setOnClickListener(v ->
        {
            Amplify.Storage.remove(
                    s3ImageKey,
                    success ->
                            Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey()),
                    failure ->
                            Log.e(TAG, "Failure in deleting file on S3 with key: " + s3ImageKey + " with error: " + failure.getMessage())
            );
            ImageView productImageView = findViewById(R.id.taskImageImageView);
            productImageView.setImageResource(android.R.color.transparent);

            SubmitButton("", "", "", "", null, "");
            switchFromDeleteButtonToAddButton(deleteImageButton);
        });
    }

    private void updateImageButtons() {
        Button addImageButton = findViewById(R.id.buttonAddImage);
        Button deleteImageButton = findViewById(R.id.buttonDeletImage);
        runOnUiThread(() -> {
            if (s3ImageKey.isEmpty()) {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            } else {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void switchFromDeleteButtonToAddButton(Button deleteImageButton) {
        Button addImageButton = findViewById(R.id.buttonAddImage);
        deleteImageButton.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }
    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.buttonDeletImage);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }


    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent callingIntent = getIntent();

        if (callingIntent != null) {
            if (callingIntent.getType() != null && callingIntent.getType().equals("text/plain")) {
                handleTextIntent(callingIntent);
            }
            if (callingIntent.getType() != null && callingIntent.getType().startsWith("image")) {
                handleImageIntent(callingIntent);
            }
        }
    }

    private void handleTextIntent(Intent intent) {
        String callingText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (callingText != null) {
            String cleanedText = cleanText(callingText);
            ((EditText) findViewById(R.id.taskNameEdite)).setText(cleanedText);
            ((EditText) findViewById(R.id.descriptionEdite)).setText(cleanedText);
        }
    }

    private void handleImageIntent(Intent intent) {
        Uri incomingImageFileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (incomingImageFileUri != null) {
            try {
                InputStream incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);
                ImageView taskImageView = findViewById(R.id.taskImageImageView);
                Log.d(TAG, "Image URI: " + incomingImageFileUri);


                if (taskImageView != null) {
                    taskImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                } else {
                    Log.e(TAG, "ImageView is null for some reason");
                }
            } catch (FileNotFoundException fnfe) {
                Log.e(TAG, "Could not get file stream from the URI " + fnfe.getMessage(), fnfe);
            }
        }
    }


    private String cleanText(String text) {
        text = text.replaceAll("\\b(?:https?|ftp):\\/\\/\\S+\\b", ""); // remove links
        text = text.replaceAll("\"", ""); // remove double quotation

        return text;
    }
}