package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemate.Adapters.CommentsAdapter;
import com.example.notemate.Adapters.DoubtsAdapter;
import com.example.notemate.Models.CommentData;
import com.example.notemate.Models.DoubtData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle bundle = getIntent().getExtras();
        String doubtId = bundle.get("doubtId").toString();
        TextView doubtTitle = findViewById(R.id.commentDoubtItemTitle);
        TextView userName = findViewById(R.id.commentDoubtItemUser);
        TextView uploadTime = findViewById(R.id.commentDoubtItemTime);
        TextView doubtDesc = findViewById(R.id.commentDoubtItemTextDesc);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();


        db.collection("doubts").document(doubtId).get().addOnSuccessListener(t -> {
            DoubtData doubtData = new DoubtData(t.getData());
            doubtTitle.setText(doubtData.getDoubtTitle());
            doubtDesc.setText(doubtData.getDoubtDesc());
            db.collection("users").document(doubtData.getUid()).get().addOnSuccessListener(task -> {
                userName.setText(task.get("name").toString());
            });
            uploadTime.setText(DateUtils.getRelativeTimeSpanString(doubtData.getUploadTime()));
        });


        Button button = findViewById(R.id.commentPostButton);
        TextView content = findViewById(R.id.editCommentContent);

        button.setOnClickListener(view -> {
            button.setEnabled(false);
            if ( content.getText().toString().isEmpty() )
            {
                button.setEnabled(true);
                Toast.makeText(this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("comments").add(new CommentData(auth.getUid(), doubtId, content.getText().toString())).addOnSuccessListener(t -> {
                button.setEnabled(true);
                Toast.makeText(this, "Comment Posted Successfully.", Toast.LENGTH_SHORT).show();
                content.setText("");
            }).addOnFailureListener(e -> {
                button.setEnabled(true);
                Toast.makeText(this, "There was an error while posting the content.", Toast.LENGTH_SHORT).show();
            });
        });


        RecyclerView recyclerView = findViewById(R.id.recycleViewComments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        db.collection("comments").orderBy("uploadTime", Query.Direction.DESCENDING).addSnapshotListener(((value, error) -> {
            List<CommentData> commentDataList = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : value.getDocuments())
            {
                if ( doubtId.equals(documentSnapshot.get("doubtId").toString()) )
                {
                    commentDataList.add(new CommentData(documentSnapshot));
//                    Toast.makeText(this, ""+documentSnapshot.get("doubtId"), Toast.LENGTH_SHORT).show();
                }
            }
            CommentsAdapter commentsAdapter = new CommentsAdapter(commentDataList);
            recyclerView.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();
        }));

    }
}