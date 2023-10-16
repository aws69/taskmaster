package com.asac26.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class AllTaskActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_task_activity);

        Button allTasksBackButton = findViewById(R.id.allTaskBack);
        allTasksBackButton.setOnClickListener(view -> {
            Intent goBack = new Intent(AllTaskActivity.this, MainActivity.class);
            startActivity(goBack);
        });
    }
}
