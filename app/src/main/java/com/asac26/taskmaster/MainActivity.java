package com.asac26.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.addTaskButton);

        addButton.setOnClickListener(view -> {
            Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskIntent);
        });

        Button allButton = (Button) findViewById(R.id.allTasksButton);

        allButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTaskActivity.class);
            startActivity(goToAllTasksIntent);
        });
    }
}