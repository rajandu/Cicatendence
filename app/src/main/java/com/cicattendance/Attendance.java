package com.cicattendance;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Attendance extends Fragment {


    public Attendance(){

    }

    private FirebaseFirestore db;
    private CollectionReference groupRef ;
    private GroupAdapter adapter;
    private ArrayList<GroupModel> groupModelArrayList;
    private RecyclerView recyclerView;
    
    private FloatingActionButton addGroupFlb;
    TextView textView;

    FirebaseAuth mAuth;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendance, container, false);
        addGroupFlb = view.findViewById(R.id.addGroupflb);
        textView = view.findViewById(R.id.textView3);
        recyclerView = view.findViewById(R.id.group_recyclerView);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        groupRef = db.collection("USERS").document(mAuth.getCurrentUser().getUid())
                .collection("GROUP");

        groupModelArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter =new GroupAdapter(groupModelArrayList, getContext());
        recyclerView.setAdapter(adapter);

        groupRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    // after getting this list we are passing
                                    // that list to our object class.
                                    GroupModel groupModel = d.toObject(GroupModel.class);

                                    // and we will pass this object class
                                    // inside our arraylist which we have
                                    // created for recycler view.
                                    groupModelArrayList.add(groupModel);
                                }
                                // after adding the data to recycler view.
                                // we are calling recycler view notifyDataSetChanged
                                // method to notify that data has been changed in recycler view.
                                adapter.notifyDataSetChanged();
                            } else {
                                // if the snapshot is empty we are displaying a toast message.
                                Toast.makeText(getContext(), "No data found in Database",
                                        Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(getContext(), "Fail to get the data.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


        //Floating action button to add the Group
        addGroupFlb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        return view;
    }


    // Invoked when floating action button (Add Group) is clicked
    void showCustomDialog(){
        final Dialog dialog =new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_add_group);
        Button addGroupbutton = dialog.findViewById(R.id.addGroupButton);
        EditText addGroupET = dialog.findViewById(R.id.addGroupNameEditText);

        dialog.show();

        // Query for adding group to the firestore
         addGroupbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Map<String , String> groups = new HashMap<>();
                 groups.put("group_name",addGroupET.getText().toString().trim());
                 
                 db.collection("USERS").document(mAuth.getCurrentUser().getUid())
                         .collection("GROUP")
                         .document(addGroupET.getText().toString().trim().toUpperCase())
                         .set(groups)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            addGroupET.getText().toString().trim()+" Added Successfully",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Group Not Added", Toast.LENGTH_SHORT).show();
                                }
                             }
                         });

                 dialog.dismiss();
             }
         });

    }
}