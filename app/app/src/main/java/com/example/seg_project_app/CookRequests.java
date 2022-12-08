package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CookRequests extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RequestRecyclerAdapter.OnRequestListener {

    private TextView txtSignOut;
    private TextView text;
    private  Cook cook;
    private Button btnAccept, btnDecline, btnBack;
    private String[] userValues;
    public ArrayList<Request> requests;
    private RecyclerView recyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public TextView mealName, clientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);

        Intent intent = getIntent();
        userValues = intent.getStringArrayExtra("userInfo");
        cook = new Cook(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7],userValues[8],userValues[9]);
        recyclerView = findViewById(R.id.recyclerMealsResult);

        updateList();
    }

    public void updateList(){
        requests = new ArrayList<Request>();
        FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Requests Received").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    requests.add(new Request(requestSnapshot.child("mealName").getValue().toString(),requestSnapshot.child("cookUID").getValue().toString(), requestSnapshot.child("clientUID").getValue().toString(),requestSnapshot.child("status").getValue().toString(),requestSnapshot.child("clientName").getValue().toString(),requestSnapshot.child("cookName").getValue().toString()));
                }
                updateRequestRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateRequestRecyclerView() {
        RequestRecyclerAdapter adapter = new RequestRecyclerAdapter(requests,this );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
        Request request = requests.get(position);

        dialogBuilder = new AlertDialog.Builder(this);
        final View requestPopupView = getLayoutInflater().inflate(R.layout.activity_request_cook, null);

        mealName = (TextView) requestPopupView.findViewById(R.id.txtMealName);
        clientName = (TextView) requestPopupView.findViewById(R.id.txtClientName);


        dialogBuilder.setView(requestPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mealName.setText(request.mealName);
        clientName.setText(request.clientName);

        btnAccept = (Button) requestPopupView.findViewById(R.id.btnAcceptRequest);
        btnDecline = (Button) requestPopupView.findViewById(R.id.btnDeclineRequest);
        btnBack = (Button) requestPopupView.findViewById(R.id.btnBack);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request.acceptRequest();
                String clientUID = request.clientUID;
                String cookUID = request.cookUID;
                String path = cookUID + " " + clientUID + " " + request.mealName;
                FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("Requests Received").child(path).removeValue();
                FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Requests Sent").child(path).removeValue();
                FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Request History").child(path).setValue(request);
                updateList();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CookRequests.this, "gor to delete", Toast.LENGTH_SHORT).show();
                request.declineRequest();
                String clientUID = request.clientUID;
                String cookUID = request.cookUID;
                String path = cookUID + " " + clientUID + " " + request.mealName;
                FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("Requests Received").child(path).removeValue();
                FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Requests Sent").child(path).removeValue();
                FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Request History").child(path).setValue(request);
                updateList();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


    }
}