package com.asac26.taskmaster.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asac26.taskmaster.R;

/**
 * @noinspection ALL
 */
public class UserSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        EditText usernameEditText = findViewById(R.id.userName);

        Button saveUsername = findViewById(R.id.buttonSaveUsername);
        saveUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                Toast.makeText(UserSettingsActivity.this, "UserName Changed!", Toast.LENGTH_SHORT).show();

            }
        });


        Button settingsBackButton = findViewById(R.id.backButtonSettings);
        settingsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromSettings = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(backToHomeFromSettings);
        });


    }
}
