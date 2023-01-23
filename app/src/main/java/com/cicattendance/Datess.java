package com.cicattendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datess extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference groupRef ;
    private DatessAdapter adapter;
    FirebaseAuth mAuth;
    String group_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datess);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String group_id = getIntent().getStringExtra("group_uid");

        groupRef = db.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP").document(group_id).collection("DATES");

        mAuth = FirebaseAuth.getInstance();

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {

        Query query = groupRef.orderBy("dates", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<DatessModel> options = new FirestoreRecyclerOptions.Builder<DatessModel>()
                .setQuery(query, DatessModel.class)
                .build();

        adapter = new DatessAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.datesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Datess.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemCliqListener(new DatessAdapter.OnItemCliqListener() {
            @Override
            public void onItemCliq(DocumentSnapshot documentSnapshot, int position) {

                DatessModel groupAdapter = documentSnapshot.toObject(DatessModel.class);
                String id = documentSnapshot.getId();
                Intent intent=new Intent(Datess.this,FinalAttendence.class);
                intent.putExtra("date_uid",id);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.stopListening();
    }
}