package com.asac26.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {
    int z = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        Button submitButton = findViewById(R.id.add);

        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Task Added",Toast.LENGTH_SHORT).show();
            TextView count = findViewById(R.id.counter);
            count.setText(String.valueOf(z++));
        });

        addTaskButton.setOnClickListener(view -> {
            Intent goBack = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(goBack);
        });
    }
}
