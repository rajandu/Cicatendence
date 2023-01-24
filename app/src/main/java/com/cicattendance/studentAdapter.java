package com.cicattendance;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;


public class studentAdapter extends RecyclerView.Adapter<studentAdapter.ViewHolder> {

    private ArrayList<studentModel> studentModelArrayList;
    private Context context;

    public studentAdapter(ArrayList<studentModel> studentModelArrayList, Context context){
        this.studentModelArrayList =  studentModelArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public studentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.student,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull studentAdapter.ViewHolder holder, int position) {

        studentModel studentModel = studentModelArrayList.get(position);
        holder.student_name.setText(studentModel.getName());
    }

    @Override
    public int getItemCount() {
        return studentModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
         TextView student_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name = itemView.findViewById(R.id.studentTV);
        }
    }


}
