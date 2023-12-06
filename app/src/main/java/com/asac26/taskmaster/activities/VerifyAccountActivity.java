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

public class VerifyAccountActivity extends AppCompatActivity {
    public static final String TAG="verifyActivity";
    public static final String VERIFY_EMAIL_TAG="verify_email_Tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        verifyButton();
        backButton();
    }

    public void verifyButton(){
        Intent callingIntent=getIntent();
        String email=callingIntent.getStringExtra(SignUpActivity.SIGNUP_EMAIL_TAG);
        EditText usernameEditText= findViewById(R.id.editTextTextEmailAddressVerify);
        usernameEditText.setText(email);

        //================================================
        Button verifyAccountButton=findViewById(R.id.verify);
        verifyAccountButton.setOnClickListener(v->{
            String username=usernameEditText.getText().toString();
            String verificationCode=((EditText)findViewById(R.id.editTextNumberVerify)).getText().toString();
            Amplify.Auth.confirmSignUp(username,
                    verificationCode,
                    success->{
                        Log.i(TAG,"Verification succeed"+ success);
                        Intent goToLoginIntent=new Intent(VerifyAccountActivity.this,LogInActivity.class);
                        goToLoginIntent.putExtra(VERIFY_EMAIL_TAG,username);
                        startActivity(goToLoginIntent);
                    },
                    failure->{Log.i(TAG,"Verification Failed" + failure);
                        runOnUiThread(()-> Toast.makeText(VerifyAccountActivity.this,"Verification Failed",Toast.LENGTH_LONG));
                    });
        });
    }

    public void backButton(){
        Button verifyBackButton= findViewById(R.id.backVerify);
        verifyBackButton.setOnClickListener(view -> {
            Intent goBackToHome = new Intent(VerifyAccountActivity.this, MainActivity.class);
            startActivity(goBackToHome);
        });
    }
}