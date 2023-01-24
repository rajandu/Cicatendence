package com.cicattendance;


import  android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{

    private Context context;
    private ArrayList<GroupModel> groupModelList;

    public GroupAdapter( ArrayList<GroupModel> groupModelList, Context context){
        this.groupModelList = groupModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {

        GroupModel model = groupModelList.get(position);
        holder.groupName_tv.setText(model.getgroup_name());
    }



    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView groupName_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName_tv = itemView.findViewById(R.id.groupName);
            groupName_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getBindingAdapterPosition();
            GroupModel groupModel = groupModelList.get(position);
            String groupName = groupModel.getgroup_name();

            Intent intent = new Intent(context, Datess.class);
            intent.putExtra("group_name", groupName);
            context.startActivity(intent);

        }
    }

}





