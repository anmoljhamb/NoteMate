package com.example.notemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notemate.Adapters.DoubtsAdapter;
import com.example.notemate.Models.DoubtData;
import com.example.notemate.Models.PostData;
import com.example.notemate.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DoubtFragment extends Fragment {


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<DoubtData> doubtDataList;


    public DoubtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doubt, container, false);


        recyclerView = view.findViewById(R.id.recycleViewDoubtFragment);
        layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("doubts").orderBy("uploadTime", Query.Direction.DESCENDING).addSnapshotListener(((value, error) -> {
            doubtDataList = new ArrayList<>();

            for (DocumentSnapshot documentSnapshot : value.getDocuments())
            {
                doubtDataList.add(new DoubtData(documentSnapshot));
            }

            DoubtsAdapter doubtsAdapter = new DoubtsAdapter(doubtDataList);
            recyclerView.setAdapter(doubtsAdapter);
            doubtsAdapter.notifyDataSetChanged();
        }));

        return view;
    }
}