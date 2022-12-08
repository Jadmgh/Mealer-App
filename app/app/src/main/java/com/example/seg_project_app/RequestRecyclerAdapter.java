package com.example.seg_project_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.MyViewHolder> {

    private ArrayList<Request> requestList;
    private  OnRequestListener onRequestListener;
    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }
    public  RequestRecyclerAdapter(ArrayList<Request> requestList, OnRequestListener onRequestListener){
        this.requestList = requestList;
        this.onRequestListener = onRequestListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView txtMealName, txtCookName, txtClientName,txtRequestStatus;
        OnRequestListener onRequestListener;
        public MyViewHolder(final View view, OnRequestListener onRequestListener){
            super(view);
            txtClientName = view.findViewById(R.id.txtClientName);
            txtCookName = view.findViewById(R.id.txtCookName);
            txtMealName = view.findViewById(R.id.txtMealName);
            txtRequestStatus = view.findViewById(R.id.txtRequestStatus);
            this.onRequestListener = onRequestListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRequestListener.onRequestClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RequestRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_in_recycler_view, parent, false);
        return new MyViewHolder(itemView, onRequestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestRecyclerAdapter.MyViewHolder holder, int position) {
        String cookName = requestList.get(position).cookName;
        holder.txtCookName.setText(cookName);

        String clientName = requestList.get(position).clientName;
        holder.txtClientName.setText(clientName);


        String txtRequestStatus = requestList.get(position).status;
        holder.txtRequestStatus.setText(txtRequestStatus);

        String mealName = requestList.get(position).mealName;
        holder.txtMealName.setText(mealName);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public interface OnRequestListener{
        void onRequestClick (int position);
    }
}

