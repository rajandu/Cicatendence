package com.cicattendance;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBqueries {

    public static List<GroupModel> groupModelList = new ArrayList<>();
    public static List<DatessModel> datessModelList = new ArrayList<>();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static String userEmail = firebaseAuth.getCurrentUser().getEmail().toString();



//    public static void groupData(final Context context) {
//
//        firebaseFirestore.collection("GROUP").orderBy("group_name").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//
//                            if(task.isSuccessful()){
//                                if(userEmail.equals(documentSnapshot.get("group_owner").toString())){
//                                    groupModelList.add(new GroupModel(
//                                            documentSnapshot.get("group_name").toString()
//                                    ));
//                                    Attendance.groupAdapter.notifyDataSetChanged();
//                                }
//                            }else{
//                                String error=task.getException().getMessage();
//                                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//
//                        QuerySnapshot documentSnapshot=task.getResult();
//
//                    }
//                });
//    }

//    public static void datesData(final Context context, final String group_name){
//
//        firebaseFirestore.collection("GROUP").document(group_name.toUpperCase().trim()).collection("DATE").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                            if(task.isSuccessful()){
//                                if(userEmail.equals(documentSnapshot.get("group_owner").toString())){
//                                    datessModelList.add(new DatessModel(
//                                            documentSnapshot.get("date").toString()
//                                    ));
//                                    Datess.datessAdapter.notifyDataSetChanged();
//                                }
//                            }else{
//                                String error=task.getException().getMessage();
//                                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        QuerySnapshot documentSnapshot=task.getResult();
//                    }
//                });
//
//    }

}
