package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    FirebaseAuth firebaseAuth;
    TextView signUp;
    Button logIn;
    EditText email, password;

    @Override
    protected void onStart() {
        super.onStart();
        Utils.userSignedIn(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Utils.userSignedIn(this);

        // Initialising all the elements
        signUp = findViewById(R.id.logInOtherOptionCall);
        logIn = findViewById(R.id.logInButton);
        email = findViewById(R.id.logInEmailInput);
        password = findViewById(R.id.logInPasswordInput);
        firebaseAuth = FirebaseAuth.getInstance();

        // Callback listeners.
        signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });


        logIn.setOnClickListener(view -> {
            logIn.setEnabled(false);
            if (email.getText().toString().isEmpty())
            {
                logIn.setEnabled(true);
                Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().isEmpty())
            {
                logIn.setEnabled(true);
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                logIn.setEnabled(true);

                if ( task.isSuccessful() )
                {
                    Toast.makeText(this, "Log in Successful.", Toast.LENGTH_SHORT).show();
                    Utils.openHome(this);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Email and Password do not match.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "logIn Error: " + task.getException() );
                }
            });
        });

    }
}