package com.asac26.taskmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView taskTitle;
    String taskTitleStr;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent callingIntent = getIntent();
        if (callingIntent != null)
            taskTitleStr = callingIntent.getStringExtra(MainActivity.TASK_TAG);
        taskTitle = findViewById(R.id.textViewTitle);
        if ((taskTitleStr != null))
            taskTitle.setText(taskTitleStr);
        else
            taskTitle.setText("Not Specified");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = findViewById(R.id.backButtonDescription);
        backButton.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }
}
