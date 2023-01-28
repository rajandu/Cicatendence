package com.cicattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FinalAttendence extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studentRef ;
    private studentAdapter adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_attendence);

        mAuth = FirebaseAuth.getInstance();

        String date_uid = getIntent().getStringExtra("date_uid");
        String group_id = getIntent().getStringExtra("group_uid");


        studentRef = db.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP")
                .document(group_id).collection("DATES").document(date_uid).collection("ATTENDENCE");
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){

        Query query = studentRef.orderBy("student_name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<studentModel> options = new FirestoreRecyclerOptions.Builder<studentModel>()
                .setQuery(query, studentModel.class)
                .build();

        adapter = new studentAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.student_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}