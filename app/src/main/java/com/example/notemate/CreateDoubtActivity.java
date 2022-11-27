package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemate.Models.DoubtData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateDoubtActivity extends AppCompatActivity {


    private final String TAG = "CreateDoubtActivity";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doubt);


        findViewById(R.id.createDoubtBackButton).setOnClickListener(view -> {
            finish();
        });


        TextView title = findViewById(R.id.createDoubtTitle);
        TextView desc = findViewById(R.id.createDoubtDesc);
        Button submit = findViewById(R.id.createDoubtSubmitButton);

        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            if ( title.getText().toString().isEmpty() )
            {
                submit.setEnabled(true);
                Toast.makeText(this, "Cannot have title as empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( desc.getText().toString().isEmpty() )
            {
                submit.setEnabled(true);
                Toast.makeText(this, "Cannot have description as empty.", Toast.LENGTH_SHORT).show();
                return;
            }


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            db.collection("doubts")
                    .add(new DoubtData(title.getText().toString(), desc.getText().toString(), auth.getCurrentUser().getUid()))
                    .addOnSuccessListener(task -> {
                        // adding the id to the current post.
                        db.collection("doubts").document(task.getId()).update("doubtId", task.getId()).addOnSuccessListener(t -> {
                            Toast.makeText(this, "Doubt added successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        }).addOnFailureListener(e -> Log.i(TAG, "onCreate: "+e.getMessage()));
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "There was some error while uploading the doubt.", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onCreate: " + e.getMessage());
                    });


        });


    }
}