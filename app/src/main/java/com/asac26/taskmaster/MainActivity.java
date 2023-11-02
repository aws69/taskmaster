package com.asac26.taskmaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asac26.taskmaster.adapter.TasksRecyclerViewAdapter;
import com.asac26.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button addTaskButton= findViewById(R.id.addTaskHome);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(MainActivity.this, AddTasksActivity.class);
            startActivity(goToAddTaskFormIntent);
        });

        //===============================================================

        Button allTaskButton=findViewById(R.id.allTasksHome);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksIntent);
        });

        //================================================================

        ImageButton settingsPage=findViewById(R.id.userProfile);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings=new Intent(MainActivity.this,UserSettingsActivity.class);
            startActivity(goToSettings);
        });

        //=================================================================

        TextView user=findViewById(R.id.userName);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

        //==================================================================

        Button firstTask=findViewById(R.id.task1);
        firstTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Cleaning");
            startActivity(sendTitle);
        });


        Button secondTask=findViewById(R.id.task2);
        secondTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Studying");
            startActivity(sendTitle);
        });


        Button thirdTask=findViewById(R.id.task3);
        thirdTask.setOnClickListener(view -> {
            Intent sendTitle=new Intent(MainActivity.this,TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle","Playing");
            startActivity(sendTitle);
        });

        //==========================================================================================

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        TasksRecyclerViewAdapter taskAdapter = new TasksRecyclerViewAdapter(Collections.singletonList((Task) createSampleTasks()), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

    }
    private List<Task> createSampleTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Chores", "Washing the dishes", "new"));
        tasks.add(new Task("Entertainment", "Listening to music", "assigned"));
        tasks.add(new Task("Groceries", "Buy bread", "in progress"));
        return tasks;
    }
}
