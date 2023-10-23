package com.asac26.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
