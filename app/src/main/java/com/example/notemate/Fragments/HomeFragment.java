package com.example.notemate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.notemate.Adapters.PostsAdapter;
import com.example.notemate.CreateDoubtActivity;
import com.example.notemate.CreatePostActivity;
import com.example.notemate.Models.PostData;
import com.example.notemate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PostsAdapter postsAdapter;
    private List<PostData> postList;
    private FloatingActionButton createBtn;
    private FloatingActionButton addBtn;
    private FloatingActionButton doubtBtn;
    private Boolean clicked = true;

    Animation rotate_open;
    Animation rotate_close;
    Animation from_bottom;
    Animation to_bottom;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View VIEW = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = VIEW.getContext();


        rotate_open = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotate_close = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        from_bottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        to_bottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        addBtn = VIEW.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(view -> {
            setVisibility(clicked);
            setAnimation(clicked);
            clicked = !clicked;
        });


        createBtn = VIEW.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(curr -> {
            Intent intent = new Intent(context, CreatePostActivity.class);
            startActivity(intent);
            getActivity().getFragmentManager().popBackStack();
        });


        doubtBtn = VIEW.findViewById(R.id.doubtBtn);
        doubtBtn.setOnClickListener(curr -> {
            startActivity(new Intent(context, CreateDoubtActivity.class));
            getActivity().getFragmentManager().popBackStack();
        });


        recyclerView = VIEW.findViewById(R.id.recycleViewPosts);
        layoutManager = new LinearLayoutManager(VIEW.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Fetch data from posts collection, and make a new postsList.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("posts");
        postReference.orderBy("uploadTime", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            postList = new ArrayList<>();
            for ( DocumentSnapshot documentSnapshot : value.getDocuments() )
            {
                postList.add(new PostData(documentSnapshot));
            }
            postsAdapter = new PostsAdapter(postList);
            recyclerView.setAdapter(postsAdapter);
            postsAdapter.notifyDataSetChanged();
        });


        return VIEW;
    }


    private void setVisibility(boolean clicked) {

        createBtn.setClickable(clicked);
        doubtBtn.setClickable(clicked);

        if ( !clicked )
        {
            createBtn.setVisibility(View.VISIBLE);
            doubtBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            createBtn.setVisibility(View.INVISIBLE);
            doubtBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {

        if ( clicked )
        {
            addBtn.startAnimation(rotate_open);
            createBtn.startAnimation(from_bottom);
            doubtBtn.startAnimation(from_bottom);
        }
        else
        {
            addBtn.startAnimation(rotate_close);
            createBtn.startAnimation(to_bottom);
            doubtBtn.startAnimation(to_bottom);
        }

    }
}