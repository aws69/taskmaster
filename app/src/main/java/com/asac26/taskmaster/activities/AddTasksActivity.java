package com.asac26.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.asac26.taskmaster.R;


public class AddTasksActivity extends AppCompatActivity {
    int x=0;
    public static final String TAG="AddTaskActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        SubmitButton();
        backButton();
    }

    private void SubmitButton(){
        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Button submitButton= findViewById(R.id.addTaskButton);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
            String title=((android.widget.TextView) findViewById(R.id.textView3)).getText().toString();
            String body= ((android.widget.TextView) findViewById(R.id.textView5)).getText().toString();
            TaskState state=(TaskState) taskCategorySpinner.getSelectedItem();
            Task newTask=Task.builder()
                    .name(title)
                    .body(body)
                    .state(state).build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): created a task successfully"),
                    failResponse -> Log.i(TAG,"AddTaskActivity.onCreate(): failed to create a task"+failResponse)
            );

            TextView count=findViewById(R.id.counter);
            count.setText(String.valueOf(x++));
        });
    }

    private void backButton(){
        Button addTaskBackButton= findViewById(R.id.backButtonAdd);
        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTasksActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }

}