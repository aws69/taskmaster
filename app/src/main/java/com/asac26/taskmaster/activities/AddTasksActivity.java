package com.asac26.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.asac26.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTasksActivity extends AppCompatActivity {
    int x=1;
    public static final String TAG="AddTaskActivity";
    CompletableFuture<List<Team>> teamFuture=new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        SubmitButton();
        backButton();
    }

    private void SubmitButton(){

        Spinner taskCategorySpinner = findViewById(R.id.spinner);
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Spinner teamSpinner = findViewById(R.id.teamSpinner);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                successResponse->{
                    Log.i(TAG,"Read Team Successfully");
                    ArrayList<String> teamNames=new ArrayList<>();
                    ArrayList<Team> teams=new ArrayList<>();
                    for (Team team:successResponse.getData()){
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    teamFuture.complete(teams);
                    runOnUiThread(()-> teamSpinner.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            teamNames
                    )));
                },
                failure->
                {
                    teamFuture.complete(null);
                    Log.i(TAG,"Failed to read team");
                }
        );

        Button submitButton= findViewById(R.id.addTaskButton);
        submitButton.setOnClickListener(view -> {
            Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();

            String title=((EditText) findViewById(R.id.taskNameEdite)).getText().toString();
            String body= ((EditText) findViewById(R.id.descriptionEdite)).getText().toString();
            TaskState state=(TaskState) taskCategorySpinner.getSelectedItem();
            String selectedTeamString= teamSpinner.getSelectedItem().toString();

            List<Team> teams=null;
            try {
                teams=teamFuture.get();
            }catch (InterruptedException e){
                Log.e(TAG,"Interruption exception while getting teams");
            }catch (ExecutionException ee){
                Log.e(TAG,"Execution exception while getting teams");
            }

            assert teams != null;
            Team selectedTeams= teams.stream().filter(c->c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);
            Task newTask=Task.builder()
                    .name(title)
                    .body(body)
                    .state(state)
                    .teamTask(selectedTeams)
                    .build();
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
        Button addTaskBackButton= findViewById(R.id.addTaskButton);
        addTaskBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AddTasksActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }

}