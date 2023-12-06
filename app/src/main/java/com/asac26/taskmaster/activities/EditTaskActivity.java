package com.asac26.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.asac26.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity {

    public static final String TAG = "editTaskActivity";
    private CompletableFuture<Task> taskCompletableFuture = null;
    private CompletableFuture<List<Team>> teamFuture = null;
    private Task taskToEdit = null;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner taskStateSpinner = null;
    private Spinner taskTeamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamFuture = new CompletableFuture<>();

        setUpEditItems();
        setUpSaveButton();
        setUpDeleteButton();

    }

    private void setUpEditItems() {
        Intent callingIntent = getIntent();
        String taskId = null;
        if (callingIntent != null) {
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_TAG);
        }
        String taskId2 = taskId;
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read tasks Successfully");

                    for (Task databaseTask : success.getData()) {
                        if (databaseTask.getId().equals(taskId2)) {
                            taskCompletableFuture.complete(databaseTask);
                        }
                    }

                    runOnUiThread(() -> {
                    });
                },
                failure -> Log.i(TAG, "Did not read task successfully")
        );
        try {
            taskToEdit = taskCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting task");
        }

        nameEditText = findViewById(R.id.editTaskTitle);
        nameEditText.setText(taskToEdit.getName());

        descriptionEditText = findViewById(R.id.editTaskBody);
        descriptionEditText.setText(taskToEdit.getBody());
        setUpSpinners();
    }

    private void setUpSpinners() {
        taskTeamSpinner = findViewById(R.id.editTeamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read teams successfully!");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        contactNames.add(team.getName());
                    }
                    teamFuture.complete(teams);

                    runOnUiThread(() -> {
                        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames));
                        taskTeamSpinner.setSelection(getSpinnerIndex(taskTeamSpinner, taskToEdit.getTeamTask().getName()));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!");
                }
        );

        taskStateSpinner = findViewById(R.id.editStateSpinner);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        taskStateSpinner.setSelection(getSpinnerIndex(taskStateSpinner, taskToEdit.getState().toString()));
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }
        return 0;
    }
    private void setUpSaveButton() {
        Button saveButton = findViewById(R.id.edit);
        saveButton.setOnClickListener(v -> {
            if (taskToEdit != null) {
                updateTask();
            }
        });
    }


    private void updateTask() {
        List<Team> teams = null;
        String contactToSaveString = taskTeamSpinner.getSelectedItem().toString();
        try {
            teams = teamFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting product");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting product");
        }
        Team contactToSave = teams.stream().filter(c -> c.getName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);
        Task taskToSave = Task.builder()
                .name(nameEditText.getText().toString())
                .body(descriptionEditText.getText().toString())
                .teamTask(contactToSave)
                .state(taskStateFromString(taskStateSpinner.getSelectedItem().toString()))
                .id(taskToEdit.getId())
                .build();

        Amplify.API.mutate(
                ModelMutation.update(taskToSave),
                successResponse -> {
                    Log.i(TAG, "EditTaskActivity.onCreate(): edited a task successfully");
                    Snackbar.make(findViewById(R.id.editTask), "Task saved!", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                failureResponse -> Log.i(TAG, "EditTaskActivity.onCreate(): failed with this response: " + failureResponse)
        );
    }

    public static TaskState taskStateFromString(String inputProductCategoryText) {
        for (TaskState taskState : TaskState.values()) {
            if (taskState.toString().equals(inputProductCategoryText)) {
                return taskState;
            }
        }
        return null;
    }

    private void setUpDeleteButton() {
        Button deleteButton = findViewById(R.id.deleteTaskButton);
        deleteButton.setOnClickListener(v -> {
            if (taskToEdit != null) {
                Amplify.API.mutate(
                        ModelMutation.delete(taskToEdit),
                        successResponse -> {
                            Log.i(TAG, "EditTaskActivity.onCreate(): deleted a task successfully");
                            Intent goToProductListActivity = new Intent(EditTaskActivity.this, MainActivity.class);
                            startActivity(goToProductListActivity);
                        },
                        failureResponse -> Log.i(TAG, "EditTaskActivity.onCreate(): failed with this response: " + failureResponse)
                );
            }
        });
    }
}