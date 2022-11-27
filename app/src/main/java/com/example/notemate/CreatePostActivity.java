package com.example.notemate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.notemate.Models.PostData;
import com.example.notemate.Models.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class CreatePostActivity extends AppCompatActivity {

    private final String TAG = "CreatePostActivity";
    TextView title;
    ProgressBar progressBar;
    TextView description;
    Bitmap pdfPreview;
    Button submitBtn;
    Button chooseBtn;
    Uri pdfUri;
    StorageReference storageReference;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        submitBtn = findViewById(R.id.createSubmitButton);
        chooseBtn = findViewById(R.id.createChooseBtn);
        progressBar = findViewById(R.id.progressBar);
        storageReference = FirebaseStorage.getInstance().getReference();
        title = findViewById(R.id.createPostName);
        description = findViewById(R.id.createPostDesc);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri temp = data.getData();
                        pdfUri = temp;

                        pdfPreview = Utils.getBitmapFromPdf(this, pdfUri);

                        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/PDF_icon.svg/1792px-PDF_icon.svg.png";
                        ImageView imageView = findViewById(R.id.createImageView);
                        if ( pdfPreview == null )
                            Glide.with(this).load(imageUrl).into(imageView);
                        else
                            Glide.with(this).load(pdfPreview).into(imageView);
                    }
                });


        findViewById(R.id.createBackButton).setOnClickListener(view -> {
            finish();
        });


        submitBtn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            chooseBtn.setEnabled(false);
            submitBtn.setVisibility(View.GONE);
            if ( pdfUri == null )
            {
                progressBar.setVisibility(View.INVISIBLE);
                chooseBtn.setEnabled(true);
                submitBtn.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Cannot Upload without any attachments.", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( title.getText().toString().isEmpty() )
            {
                progressBar.setVisibility(View.INVISIBLE);
                chooseBtn.setEnabled(false);
                submitBtn.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Title cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if ( description.getText().toString().isEmpty() )
            {
                progressBar.setVisibility(View.INVISIBLE);
                chooseBtn.setEnabled(true);
                submitBtn.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }


            // Upload the current pdf to firebase storage;
            StorageReference pdfReference = storageReference.child("pdfs/" + System.currentTimeMillis() + "-doc.pdf");
            pdfReference.putFile(pdfUri).addOnSuccessListener(taskSnapshot -> {
                // retrieve the pdf url from there.
                pdfReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Create a post object with the url and add it to the firestore collection document.
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("posts").add(
                            new PostData(FirebaseAuth.getInstance().getUid(), title.getText().toString(), description.getText().toString(), uri)
                    ).addOnSuccessListener(task -> {
                        String currId = task.getId();

                        // Upload the thumbnail of the file to the firebase storage.
                        StorageReference thumbnailReference = storageReference.child("images/"+currId+".jpg");
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        pdfPreview.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] data = bos.toByteArray();
                        thumbnailReference.putBytes(data).addOnSuccessListener(t -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            chooseBtn.setEnabled(true);
                            submitBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(this, "Post Uploaded.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "There was an error while uploading the thumbnail." + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


                        firestore.collection("posts").document(currId).update("postId", currId).addOnSuccessListener(t -> {

                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "There was an error while adding the postId", Toast.LENGTH_SHORT).show();
                        });


                    }).addOnFailureListener(task -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        chooseBtn.setEnabled(true);
                        submitBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "There was some error while uploading the post.", Toast.LENGTH_SHORT).show();
                    });
                });
            });
        });


        chooseBtn.setOnClickListener(view -> {
            Intent pickPdf = new Intent(Intent.ACTION_GET_CONTENT);
            pickPdf.setType("application/pdf");

            if ( pickPdf.resolveActivity(getPackageManager()) != null )
            {
                activityResultLauncher.launch(pickPdf);
            }
            else
            {
                Toast.makeText(this, "There was an error while choosing the file.", Toast.LENGTH_SHORT).show();
            }

        });

    }

}