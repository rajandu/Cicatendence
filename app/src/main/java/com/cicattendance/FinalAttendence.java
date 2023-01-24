package com.cicattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FinalAttendence extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference studentRef ;
    private studentAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<studentModel> studentModelArrayList;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_attendence);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.student_recycler_view);

        studentModelArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter =new studentAdapter(studentModelArrayList, this);
        recyclerView.setAdapter(adapter);



        String date_uid = getIntent().getStringExtra("date_uid");
        String group_id = getIntent().getStringExtra("group_uid");

        studentRef = db.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP").document(group_id).collection("DATES").document(date_uid).collection("ATTENDENCE");
       studentRef.get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                       if (!queryDocumentSnapshots.isEmpty()) {

                           List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                           for (DocumentSnapshot d : list) {
                               studentModel c = d.toObject(studentModel.class);
                               studentModelArrayList.add(c);
                           }

                           adapter.notifyDataSetChanged();
                       } else {
                           // if the snapshot is empty we are displaying a toast message.
                           Toast.makeText(FinalAttendence.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                       }

                   }
               });
    }

//    private void setUpRecyclerView(){
//
//        Query query = studentRef.orderBy("student_name", Query.Direction.ASCENDING);
//        FirestoreRecyclerOptions<studentModel> options = new FirestoreRecyclerOptions.Builder<studentModel>()
//                .setQuery(query, studentModel.class)
//                .build();
//
//        adapter = new studentAdapter(options);
//
//        RecyclerView recyclerView = findViewById(R.id.student_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
}