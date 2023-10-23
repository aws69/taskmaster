package com.asac26.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    public static final String TASK_TAG="taskName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton userProfile = (ImageButton) findViewById(R.id.userProfile);
        userProfile.setOnClickListener(view -> {
            Intent goToUserSettings = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettings);
        });

        Button task1 = findViewById(R.id.task1);
        task1.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task1.getText().toString());
            startActivity(goToTaskDetails);
        });
        Button task2 = findViewById(R.id.task2);
        task2.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task2.getText().toString());
            startActivity(goToTaskDetails);
        });
        Button task3 = findViewById(R.id.task3);
        task3.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task3.getText().toString());
            startActivity(goToTaskDetails);
        });

        Button addTaskButton = findViewById(R.id.addTaskHome);
        addTaskButton.setOnClickListener(view -> {
            System.out.println("Add Task Button Clicked");
            Intent goToNewTaskFormIntent = new Intent(MainActivity.this, AddTasksActivity.class);
            startActivity(goToNewTaskFormIntent);
        });
        Button allTasksButton = findViewById(R.id.allTasksHome);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksFormIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksFormIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView userTasks;
        userTasks = findViewById(R.id.userName);
        String name = sp.getString(UserSettingsActivity.USERNAME_TAG, "no name");
        userTasks.setText(name.isEmpty() ? "tasks" : name + "'s tasks");
    }
}
