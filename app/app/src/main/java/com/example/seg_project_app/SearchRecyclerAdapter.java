package com.example.seg_project_app;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {

    private ArrayList<Meal> mealList;
    private  OnMealListener onMealListener;
    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }
    public  SearchRecyclerAdapter(ArrayList<Meal> mealList, OnMealListener onMealListener){
        this.mealList = mealList;
        this.onMealListener = onMealListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView txtMealName, txtCookName, txtCookRating, txtMealType, txtMealCuisine, txtMealPrice,txtMealALlergens;
        OnMealListener onMealListener;
        public MyViewHolder(final View view, OnMealListener onMealListener){
            super(view);
            txtMealALlergens = view.findViewById(R.id.txtMealAllergens);
            txtMealName = view.findViewById(R.id.txtMealName);
            txtCookName = view.findViewById(R.id.txtCookName);
            txtCookRating = view.findViewById(R.id.txtRequestStatus);
            txtMealType = view.findViewById(R.id.txtMealType);
            txtMealCuisine = view.findViewById(R.id.txtMealCuisine);
            txtMealPrice = view.findViewById(R.id.txtMealPrice);
            this.onMealListener = onMealListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMealListener.onMealClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SearchRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_in_recycler_view, parent, false);
        return new MyViewHolder(itemView, onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerAdapter.MyViewHolder holder, int position) {
        String cookName = mealList.get(position).getName();
        holder.txtCookName.setText(cookName);

        String txtCookRating = mealList.get(position).cookRating;
        holder.txtCookRating.setText(txtCookRating+"/5");

        String mealType= mealList.get(position).mealType;
        holder.txtMealType.setText(mealType);

        String mealCuisine = mealList.get(position).mealCuisine;
        holder.txtMealCuisine.setText(mealCuisine);


        String mealPrice = mealList.get(position).mealPrice;
        holder.txtMealPrice.setText(mealPrice);

        String mealName = mealList.get(position).mealName;
        holder.txtMealName.setText(mealName);

        String mealAllergens = mealList.get(position).mealAllergens;
        holder.txtMealALlergens.setText(mealAllergens);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public interface OnMealListener{
        void onMealClick (int position);
    }
}

