package com.asac26.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddTasksActivity extends AppCompatActivity {

    int z = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        Toast toast = Toast.makeText(this, "submitted!", Toast.LENGTH_SHORT);
        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> toast.show());
        addTaskButton.setOnClickListener(view -> {
            Toast.makeText(this, "Task Added",Toast.LENGTH_SHORT).show();
            TextView count = findViewById(R.id.counter);
            count.setText(String.valueOf(z++));
        });


        Button backButton = findViewById(R.id.backButtonAdd);
        backButton.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(AddTasksActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }
}