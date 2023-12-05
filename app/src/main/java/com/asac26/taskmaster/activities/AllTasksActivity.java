package com.asac26.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.asac26.taskmaster.R;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        BackButton();
    }

    private void BackButton() {
        Button allTasksBackButton = findViewById(R.id.backButtonAll);
        allTasksBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(AllTasksActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }
}