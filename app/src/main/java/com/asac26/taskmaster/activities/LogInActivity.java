package com.asac26.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.asac26.taskmaster.R;

public class LogInActivity extends AppCompatActivity {
    public static final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginButton();
        signUpButton();
    }

    public void loginButton(){
        Intent callingIntent=getIntent();
        String email=callingIntent.getStringExtra(VerifyAccountActivity.VERIFY_EMAIL_TAG);
        EditText usernameEditText= findViewById(R.id.editTextTextEmailAddressVerfy);
        usernameEditText.setText(email);

        Button loginButton=findViewById(R.id.verify);
        loginButton.setOnClickListener(v->{
            String username=usernameEditText.getText().toString();
            String password=((EditText)findViewById(R.id.editTextTextPasswordSignUp)).getText().toString();
            Amplify.Auth.signIn(username,password,
                    success->{
                        Log.i(TAG,"login successfully"+ success);
                        Intent goToHomeIntent=new Intent(LogInActivity.this,MainActivity.class);
                        startActivity(goToHomeIntent);
                    },
                    failure->{Log.i(TAG,"login failed"+ failure);
                        Toast.makeText(LogInActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                    });
        });
    }

    public void signUpButton(){
        Button signupButton= findViewById(R.id.goSignUp);
        signupButton.setOnClickListener(view -> {
            Intent goToSignUp = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(goToSignUp);
        });
    }
}