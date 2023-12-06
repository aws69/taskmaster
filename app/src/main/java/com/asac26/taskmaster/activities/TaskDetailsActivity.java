package com.asac26.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asac26.taskmaster.R;

public class TaskDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TaskDetails();
        BackButton();
        EditTask();
    }

    private void TaskDetails(){
        TextView title=findViewById(R.id.textViewTitle);
        TextView body = findViewById(R.id.textViewDescription);
        TextView status = findViewById(R.id.textViewStatus);
        TextView team = findViewById(R.id.textViewTeam);

        Intent intent=getIntent();
        String taskTitle=intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskStatus = intent.getStringExtra("taskStatus");
        String taskTeam = intent.getStringExtra("taskTeam");

        if(taskTitle!=null)
            title.setText(taskTitle);
        if (taskBody != null)
            body.setText(taskBody);
        if (taskStatus != null)
            status.setText(taskStatus);
        if (taskTeam != null)
            team.setText(taskTeam);
    }

    private void BackButton(){
        Button detailsBackButton=findViewById(R.id.backButtonDescription);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,MainActivity.class);
            startActivity(backToHomeFromDetails);
        });
    }
    private void EditTask(){
        Button detailsBackButton=findViewById(R.id.editButton);
        detailsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromDetails= new Intent(TaskDetailsActivity.this,EditTaskActivity.class);
            startActivity(backToHomeFromDetails);
        });
    }
}
