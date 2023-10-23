package com.asac26.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/** @noinspection ALL*/
public class UserSettingsActivity extends AppCompatActivity {
    Button saveButton;
    EditText username;
    String usernameStr;
    SharedPreferences sp;
    public static final String USERNAME_TAG="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        saveButton= findViewById(R.id.buttonSaveUsername);
        username= findViewById(R.id.editTextUsername);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        saveButton.setOnClickListener(view ->  {

            usernameStr=username.getText().toString();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(USERNAME_TAG,usernameStr);
            editor.apply();
            Snackbar.make(findViewById(R.id.userSettingsActivity), "Username Saved", Snackbar.LENGTH_SHORT).show();
        });

        Button backButton = findViewById(R.id.backButtonSettings);
        backButton.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }
}
