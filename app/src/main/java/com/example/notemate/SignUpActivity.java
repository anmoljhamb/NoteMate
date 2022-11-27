package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = "SignUpActivity";

    TextView logIn;
    Button signUp;
    EditText email, password1, password2;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        Utils.userSignedIn(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Utils.userSignedIn(this);

        // Initialising elements.
        auth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.signInButton);
        email = findViewById(R.id.signInEmailInput);
        password1 = findViewById(R.id.signInPasswordInput);
        password2 = findViewById(R.id.signInPasswordInput2);
        logIn = findViewById(R.id.signInOtherOptionCall);


        // Callback functions.
        logIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });


        signUp.setOnClickListener(view -> {

            signUp.setEnabled(false);

            if ( email.getText().toString().isEmpty() )
            {
                signUp.setEnabled(true);
                Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( password1.getText().toString().isEmpty() )
            {
                signUp.setEnabled(true);
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }


            // After making sure that both of them are non empty.
            // We need to make sure that password1 and password2 should match.


            if ( !password1.getText().toString().equals(password2.getText().toString()) )
            {
                signUp.setEnabled(true);
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString()).addOnCompleteListener(task -> {
                signUp.setEnabled(true);
                if ( task.isSuccessful() )
                {
                    auth.signInWithEmailAndPassword(email.getText().toString(), password1.getText().toString());
                    Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String ,Object> map = new HashMap<>();
                    map.put("uid", auth.getCurrentUser().getUid());
                    map.put("email", email.getText().toString());
                    map.put("college", "");
                    map.put("course", "");
                    map.put("name", "");


                    db.collection("users").document(auth.getCurrentUser().getUid())
                                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i(TAG, "onSuccess: user document created successfully.");
                                }
                            }).addOnFailureListener(exception -> {
                                Log.i(TAG, "onCreate: tasked failed." + exception.getMessage());
                            });

                    startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(this, "There was an error while signing up.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "SignUp Error: " + task.getException() );
                }
            });

        });

    }
}