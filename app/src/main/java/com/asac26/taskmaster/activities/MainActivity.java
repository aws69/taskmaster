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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.asac26.taskmaster.R;
import com.asac26.taskmaster.adapter.TasksRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="homeActivity";
    private TasksRecyclerViewAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amplifier();
        setUpTaskListRecyclerView();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.userName);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Username");
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

    private void setUpTaskListRecyclerView(){
        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TasksRecyclerViewAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }

    private void AddTaskButton() {
        Button addTaskButton = findViewById(R.id.addTaskHome);
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
}
