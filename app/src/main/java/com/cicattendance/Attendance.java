package com.cicattendance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class Attendance extends Fragment {


    public Attendance(){

    }

    private FirebaseFirestore db;
    private CollectionReference groupRef ;
    private GroupAdapter adapter;
    
    private FloatingActionButton addGroupFlb;
    TextView textView;

    LinearLayoutManager linearLayoutManager;

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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        groupRef = db.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP");

        setUpRecyclerView();

        addGroupFlb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        return view;
    }

    private void setUpRecyclerView() {

        Query query = groupRef.orderBy("group_name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<GroupModel> options = new FirestoreRecyclerOptions.Builder<GroupModel>()
                .setQuery(query, GroupModel.class)
                .build();

        adapter = new GroupAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.group_recyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Do you want to Delete  ?");
                builder.setTitle("Delete Group !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    adapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
                    dialog.cancel();
                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemCliqListener(new GroupAdapter.OnItemCliqListener() {
            @Override
            public void onItemCliq(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();

                Intent intent = new Intent(getActivity().getApplicationContext(),Datess.class);
                intent.putExtra("group_uid",id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    void showCustomDialog(){
        final Dialog dialog =new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_add_group);
        Button addGroupbutton = dialog.findViewById(R.id.addGroupButton);
        EditText addGroupET = dialog.findViewById(R.id.addGroupNameEditText);

        dialog.show();



         addGroupbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Map<String , String> groups = new HashMap<>();
                 groups.put("group_name",addGroupET.getText().toString().trim());


                 db.collection("USERS").document(mAuth.getCurrentUser().getUid()).collection("GROUP").document(addGroupET.getText().toString().trim().toUpperCase()).set(groups)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity().getApplicationContext(), addGroupET.getText().toString().trim()+" Added Successfully",Toast.LENGTH_SHORT).show();
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