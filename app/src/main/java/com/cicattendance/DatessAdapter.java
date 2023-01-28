package com.cicattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class DatessAdapter extends FirestoreRecyclerAdapter<DatessModel, DatessAdapter.DatesHolder> {


    private OnItemCliqListener listener;

    public DatessAdapter(@NonNull FirestoreRecyclerOptions<DatessModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DatessAdapter.DatesHolder holder, int position, @NonNull DatessModel model) {
        holder.datess_tv.setText(model.date);
    }

    @NonNull
    @Override
    public DatessAdapter.DatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datess, parent, false);
        return new DatesHolder(view);
    }

    class DatesHolder extends RecyclerView.ViewHolder{

        public TextView datess_tv;
        public TextView datesNoTV;

        public DatesHolder(@NonNull View itemView) {
            super(itemView);
            datess_tv  = itemView.findViewById(R.id.datessTV);
            //datesNoTV = itemView.findViewById(R.id.datesNoTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getBindingAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemCliq(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemCliqListener{
        void onItemCliq(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemCliqListener(DatessAdapter.OnItemCliqListener listener){
        this.listener = listener;
    }
}
