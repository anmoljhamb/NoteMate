package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.notemate.Models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        EditText userName = findViewById(R.id.profileUserNameInput);
        EditText userCollege = findViewById(R.id.profileUserCollegeInput);
        EditText userCourse = findViewById(R.id.profileUserCourseInput);
        EditText userEmail = findViewById(R.id.profileUserEmailInput);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
            Log.i(TAG, "onCreate: dbcollection");
            if ( task.isSuccessful() )
            {
                DocumentSnapshot documentSnapshot = task.getResult();
                Map<String, Object> map = documentSnapshot.getData();
                UserData currUserData = new UserData(map);
                userName.setText(currUserData.getName());
                userEmail.setText(currUserData.getEmail());
                userCollege.setText(currUserData.getCollege());
                userCourse.setText(currUserData.getCourse());
            }
            else
            {
                Log.i(TAG, "onCreate: " + task.getException());
            }
        });




        Button back = findViewById(R.id.profileBackButton);
        Button save = findViewById(R.id.profileSaveButton);
        Button logOut = findViewById(R.id.profileLogoutButton);


        back.setOnClickListener(view -> {
            if ( userName.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCollege.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCollege.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCourse.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Utils.openHome(this);
            finish();
        });


        save.setOnClickListener(view -> {
            save.setEnabled(false);
            if ( userName.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCollege.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCollege.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( userCourse.getText().toString().isEmpty() )
            {
                save.setEnabled(true);
                Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users")
                    .document(firebaseUser.getUid())
                    .update(Map.of(
                            "name", userName.getText().toString(),
                            "college", userCollege.getText().toString(),
                            "email", userEmail.getText().toString(),
                            "course", userCourse.getText().toString()
                    )).addOnSuccessListener(task -> {
                        save.setEnabled(true);
                        Toast.makeText(this, "Values Updated Successfully.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(task -> {
                        save.setEnabled(true);
                        Toast.makeText(this, "There was some error.", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, ""+task.getMessage());
                    });
        });



        logOut.setOnClickListener(view -> {
            Toast.makeText(this, "Logging out.", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });


    }
}