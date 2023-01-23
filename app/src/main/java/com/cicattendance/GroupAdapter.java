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
        holder.groupName_tv.setText(model.getGroupName());
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false);
        return new GroupHolder(view);
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


//    private static Context context;
//    static List<GroupModel> groupModelList;
//
//    public GroupAdapter( List<GroupModel> groupModelList){
//        this.groupModelList = groupModelList;
//    }
//
//
//    @NonNull
//    @Override
//    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false);
//        context = parent.getContext();
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {
//
//        GroupModel model = groupModelList.get(position);
//        String groupName =  model.getGroupName();
//        holder.setData(groupName);
//
//        return;
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return groupModelList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        public TextView groupName_tv;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            groupName_tv = itemView.findViewById(R.id.groupName);
//            groupName_tv.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            int position = this.getAdapterPosition();
//            GroupModel groupModel = groupModelList.get(position);
//            String groupName = groupModel.getGroupName();
//
//            Intent intent = new Intent(context, Datess.class);
//            intent.putExtra("group_name", groupName);
//            context.startActivity(intent);
//
//        }
//        private void setData(String groupName){
//
//            groupName_tv.setText(groupName);
//
//        }
//    }



}
