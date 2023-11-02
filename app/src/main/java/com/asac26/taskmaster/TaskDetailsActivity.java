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

        Button detailsBackButton=findViewById(R.id.backButtonDescription);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,MainActivity.class);
            startActivity(backToHomeFromDetails);
        });

        TextView title=findViewById(R.id.textViewTitle);
        TextView description = findViewById(R.id.textViewDescription);
        TextView status = findViewById(R.id.textViewStatus);

        Intent intent=getIntent();
        String taskTitle=intent.getStringExtra("taskTitle");
        String taskDescription = intent.getStringExtra("taskDescription");
        String taskStatus = intent.getStringExtra("taskStatus");

        if(taskTitle!=null)
            title.setText(taskTitle);
        if (taskDescription != null)
            description.setText(taskDescription);
        if (taskStatus != null)
            status.setText(taskStatus);
    }
}
