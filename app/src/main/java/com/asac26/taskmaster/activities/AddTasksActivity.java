package com.asac26.taskmaster.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.asac26.taskmaster.R;
import com.asac26.taskmaster.database.TaskDatabase;
import com.asac26.taskmaster.enums.TaskState;
import com.asac26.taskmaster.models.Task;

public class AddTasksActivity extends AppCompatActivity {

    int z = 0;
    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        taskDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskDatabase.class
                        , "tasks_stuff")
                .allowMainThreadQueries()
                .build();
        @SuppressLint({"WrongViewCast", "CutPasteId"}) Spinner taskCategorySpinner = findViewById(R.id.counter);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Button addTaskBackButton = findViewById(R.id.backButtonAdd);
        Button submitButton = findViewById(R.id.addTaskButton);


        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
            Task newTask = new Task(
                    ((EditText) findViewById(R.id.taskNameEdite)).getText().toString(),
                    ((EditText) findViewById(R.id.descriptionEdite)).getText().toString(),
                    TaskState.fromString(taskCategorySpinner.getSelectedItem().toString())
            );
            taskDatabase.taskDao().insertATask(newTask);
            @SuppressLint("CutPasteId") TextView count = findViewById(R.id.counter);
            count.setText(String.valueOf(z++));
        });

        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTasksActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }
}