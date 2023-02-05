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


public class GroupAdapter extends FirestoreRecyclerAdapter<GroupModel,GroupAdapter.GroupHolder> {

    private OnItemCliqListener listener;


    public GroupAdapter(@NonNull FirestoreRecyclerOptions<GroupModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupHolder holder, int position, @NonNull GroupModel model) {
        holder.groupName_tv.setText(model.group_name);
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false);
        return new GroupHolder(view);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class GroupHolder extends RecyclerView.ViewHolder{

        TextView groupName_tv;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            groupName_tv = itemView.findViewById(R.id.groupName);

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


    public void setOnItemCliqListener(OnItemCliqListener listener){
        this.listener = listener;
    }

}
