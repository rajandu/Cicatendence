package com.cicattendance;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class studentAdapter extends FirestoreRecyclerAdapter<studentModel, studentAdapter.studentHolder> {


    public studentAdapter(@NonNull FirestoreRecyclerOptions<studentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull studentHolder holder, int position, @NonNull studentModel model) {
        holder.studentTv.setText(model.getName());
    }

    @NonNull
    @Override
    public studentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student,parent,false);
        return new studentHolder(v);
    }

    class studentHolder extends RecyclerView.ViewHolder{

        TextView studentTv ;

        public studentHolder(@NonNull View itemView) {
            super(itemView);

             studentTv = itemView.findViewById(R.id.studentTV);
        }
    }

}
