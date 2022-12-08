package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ClientRequestHistory extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, View.OnClickListener, RequestRecyclerAdapter.OnRequestListener {

    private String[] userValues;
    public ArrayList<Request> requestHistory;
    private RecyclerView recyclerView, historyRecyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public TextView mealName, clientName;
    private  Client client;
    public Button btnSubmitRating;
    public Button btnSubmitComplaint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_request_history);


        Intent intent = getIntent();
        userValues = intent.getStringArrayExtra("userInfo");
        client = new Client(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7]);


        historyRecyclerView = findViewById(R.id.recyclerRequestHistory);

        updateRequests();

    }

    private void updateRequests() {
        requestHistory = new ArrayList<Request>();
        FirebaseDatabase.getInstance().getReference("Users").child(client.userID).child("Request History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Request request = new Request(requestSnapshot.child("mealName").getValue().toString(),requestSnapshot.child("cookUID").getValue().toString(), requestSnapshot.child("clientUID").getValue().toString(),requestSnapshot.child("status").getValue().toString(),requestSnapshot.child("clientName").getValue().toString(),requestSnapshot.child("cookName").getValue().toString());
                    if (requestSnapshot.child("ratingGiven").getValue() !="" && requestSnapshot.child("ratingGiven").getValue() != null ) {
                        request.rateCook(requestSnapshot.child("ratingGiven").getValue().toString());
                    }
                    requestHistory.add(request);
                }
                updateRequestRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateRequestRecyclerView() {
        RequestRecyclerAdapter adapter2 = new RequestRecyclerAdapter(requestHistory,this );
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        historyRecyclerView.setLayoutManager(layoutManager2);
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyRecyclerView.setAdapter(adapter2);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRequestClick(int position) {
        Request request = requestHistory.get(position);

        if (!request.status.equals("declined")) {
            dialogBuilder = new AlertDialog.Builder(this);
            final View requestPopupView = getLayoutInflater().inflate(R.layout.activity_old_request, null);

            TextView mealName = (TextView) requestPopupView.findViewById(R.id.txtMealName);
            TextView cookName = (TextView) requestPopupView.findViewById(R.id.txtCookName);
            EditText complaintDecription = (EditText) requestPopupView.findViewById(R.id.editComplaintDescription);
            btnSubmitRating = (Button) requestPopupView.findViewById(R.id.btnSubmitRating);
            btnSubmitComplaint = (Button) requestPopupView.findViewById(R.id.btnSubmitComplaint);

            mealName.setText(request.mealName);
            cookName.setText(request.cookName);

            RatingBar RatingBar;
            RatingBar ratingBar = (RatingBar) = (RatingBar) requestPopupView.findViewById(R.id.clientToCookRating);
            String ratingGiven = request.ratingGiven;
            if (!request.ratingGiven.equals("")) {
                ratingBar.setRating(Float.parseFloat(ratingGiven));
            }

            dialogBuilder.setView(requestPopupView);
            dialog = dialogBuilder.create();
            dialog.show();

            btnSubmitRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!request.ratingGiven.equals("")) {

                        FirebaseDatabase.getInstance().getReference("Users").child(request.cookUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Float currentRating;
                                Integer nbOfRatings;
                                currentRating = Float.parseFloat(snapshot.child("rating").getValue().toString());
                                nbOfRatings = Integer.parseInt(snapshot.child("nbOfReviews").getValue().toString());
                                changeRating(ratingBar.getRating(),request.cookUID,currentRating,nbOfRatings,request.mealName,request.clientUID);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {

                        FirebaseDatabase.getInstance().getReference("Users").child(request.cookUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer nbOfRatings;
                                nbOfRatings = Integer.parseInt(snapshot.child("nbOfReviews").getValue().toString());
                                String currentRating =snapshot.child("rating").getValue().toString();
                                addRating(ratingBar.getRating(),request.cookUID,currentRating,nbOfRatings,request.mealName,request.clientUID);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });


            btnSubmitComplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = request.cookUID + " " + client.userID + " " + request.mealName;
                    Complaint complaint = new Complaint(client.userID, request.cookUID, complaintDecription.getText().toString(), "Complaint " + request.clientName + " -> " + request.cookName + " on meal " + request.mealName);
                    FirebaseDatabase.getInstance().getReference("Complaints").child("Complaint " + request.clientName + " -> " + request.cookName + " on meal " + request.mealName).setValue(complaint);
                    Toast.makeText(ClientRequestHistory.this, path, Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference("Users").child(request.clientUID).child("Request History").child(path).removeValue();
                    updateRequests();
                    Toast.makeText(ClientRequestHistory.this, "Complaint submitted and request removed from history.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    public void addRating (Float inputedRating, String cookUID, String currentRating, int nbOfRatings, String mealName,String clientUID) {
        String path = cookUID + " " + clientUID + " " + mealName;
        Integer denominator = nbOfRatings+1;
        Float newRating;
        if (!currentRating.equals("")) {
            newRating =( (Float.parseFloat(currentRating)* nbOfRatings) + inputedRating) / denominator;
        }
        else{
            newRating = inputedRating;
        }
        FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("rating").setValue(Float.toString(newRating));
        FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("nbOfReviews").setValue(nbOfRatings+1);
        FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Request History").child(path).child("ratingGiven").setValue("ratingBar.getRating()");
        Toast.makeText(ClientRequestHistory.this, "Rating Submitted!", Toast.LENGTH_SHORT).show();
    }

    public void changeRating(Float inputedRating, String cookUID, Float currentRating, int nbOfRatings, String mealName,String clientUID){
        String path = cookUID + " " + clientUID + " " + mealName;
        Float newRating = (currentRating * (nbOfRatings-1) + inputedRating) / nbOfRatings;
        FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("rating").setValue(Float.toString(newRating));
        FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Request History").child(path).child("ratingGiven").setValue(inputedRating);
        Toast.makeText(ClientRequestHistory.this, "Rating Changed!", Toast.LENGTH_SHORT).show();
    }

}