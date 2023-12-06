package com.asac26.taskmaster.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.asac26.taskmaster.R;
import com.asac26.taskmaster.adapter.TasksRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_ID_TAG="task_Id_Tag";
    private String selectedTeam;
    public static final String TAG="homeActivity";
    private TasksRecyclerViewAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedTeam = sharedPreferences.getString(UserSettingsActivity.TEAM_TAG, "");

        amplifier();
        setUpTaskListRecyclerView();
        queryTasks();
        setAddTaskButton();
        setAllTasksButton();
        setSettingsButton();
        setLoginAndLogOutButton();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.userName);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");
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

    private void setAddTaskButton() {
        Button addTaskButton = findViewById(R.id.addTaskHome);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(MainActivity.this, AddTasksActivity.class);
            startActivity(goToAddTaskFormIntent);
        });
    }

    private void setAllTasksButton() {
        Button allTaskButton = findViewById(R.id.allTasksHome);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }


    private void setSettingsButton() {
        ImageButton settingsPage = findViewById(R.id.userProfile);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(goToSettings);
        });
    }

    public void setLoginAndLogOutButton(){
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