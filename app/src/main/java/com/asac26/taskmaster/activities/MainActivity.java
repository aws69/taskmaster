package com.asac26.taskmaster.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.asac26.taskmaster.R;
import com.asac26.taskmaster.adapter.TasksRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_ID_TAG="task_Id_Tag";
    private String selectedTeam;
    public static final String TAG="homeActivity";
    private TasksRecyclerViewAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedTeam = sharedPreferences.getString(UserSettingsActivity.TEAM_TAG, "");

        createFile();
        setUpLoginAndLogOutButton();
        amplifier();
        setUpTaskListRecyclerView();
        queryTasks();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();
    }

    private ActivityResultLauncher<Intent> getImagePickActivityResultLauncher() {
        return null;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.userName);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

        AuthUser authUser=Amplify.Auth.getCurrentUser();
        if(authUser==null){
            Button loginButton=findViewById(R.id.buttonLogIn);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton=findViewById(R.id.buttonLogOut);
            logoutButton.setVisibility(View.INVISIBLE);
        }else {
            String nickName =authUser.getUsername();
            Log.i(TAG,"UserName"+nickName);
            Button loginButton=findViewById(R.id.buttonLogIn);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton=findViewById(R.id.buttonLogOut);
            logoutButton.setVisibility(View.VISIBLE);
        }
        //==================================================================
        Amplify.Auth.fetchUserAttributes(
                success ->
                {
                    Log.i(TAG, "Fetch user attributes succeeded for username: "+ username);
                    for (AuthUserAttribute userAttribute: success){
                        if(userAttribute.getKey().getKeyString().equals("email")){
                            String userEmail = userAttribute.getValue();
                            runOnUiThread(() ->
                                    ((TextView)findViewById(R.id.userNikName)).setText(userEmail));
                        }
                    }
                },
                failure ->
                        Log.i(TAG, "Fetch user attributes failed: "+ failure)
        );
    }

    public void createFile(){
        String emptyFilename= "emptyTestFileName";
        File emptyFile = new File(getApplicationContext().getFilesDir(), emptyFilename);

        try {
            BufferedWriter emptyFileBufferedWriter= new BufferedWriter(new FileWriter(emptyFile));

            emptyFileBufferedWriter.append("this will write in the file \n aws wrote this ");

            emptyFileBufferedWriter.close();
        }catch (IOException ioe){
            Log.i(TAG, "could not write locally with filename: "+ emptyFilename);
        }

        String emptyFileS3Key = "someFileOnS3.txt";
        Amplify.Storage.uploadFile(
                emptyFileS3Key,
                emptyFile,
                success ->
                        Log.i(TAG, "S3 upload succeeded and the Key is: " + success.getKey()),
                failure ->
                        Log.i(TAG, "S3 upload failed! " + failure.getMessage())
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    public void amplifier(){
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success->{
                    Log.i(TAG,"Read tasks successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() -> taskAdapter.notifyDataSetChanged());
                },
                failure-> Log.i(TAG,"failed to read tasks")
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void queryTasks() {
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read Task successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        Team teamTask = databaseTask.getTeamTask();
                        if (teamTask != null && teamTask.getName().equals(selectedTeam)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> taskAdapter.notifyDataSetChanged());
                },
                failure -> Log.i(TAG, "Couldn't read tasks from DynamoDB ")
        );
    }


    private void setUpTaskListRecyclerView(){
        RecyclerView taskListRecycleReview = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TasksRecyclerViewAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }

    private void AddTaskButton() {
        Button addTaskButton = findViewById(R.id.allTasksHome);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(MainActivity.this, AddTasksActivity.class);
            startActivity(goToAddTaskFormIntent);
        });
    }

    private void AllTasksButton() {
        Button allTaskButton = findViewById(R.id.allTasksHome);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }


    private void SettingsButton() {
        ImageButton settingsPage = findViewById(R.id.userProfile);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(goToSettings);
        });
    }
    public void setUpLoginAndLogOutButton(){
        Button loginButton=findViewById(R.id.buttonLogIn);
        loginButton.setOnClickListener(v->{
            Intent goToLogin = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(goToLogin);
        });

        Button logoutButton=findViewById(R.id.buttonLogOut);
        logoutButton.setOnClickListener(v-> Amplify.Auth.signOut(
                ()-> {Log.i(TAG,"logOut successfully");
                    runOnUiThread(()-> ((TextView)findViewById(R.id.userNikName)).setText(""));
                    Intent goToLogin = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(goToLogin);
                },
                failure->{Log.i(TAG,"logOut failed");
                    runOnUiThread(()-> Toast.makeText(MainActivity.this,"Logout Failed",Toast.LENGTH_LONG));
                }));
    }
}