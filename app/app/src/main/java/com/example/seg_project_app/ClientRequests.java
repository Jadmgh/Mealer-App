package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientRequests extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RequestRecyclerAdapter.OnRequestListener{
    private TextView txtSignOut;
    private TextView text;
    private  Client client;
    private Button btnAccept, btnDecline, btnBack;
    private String[] userValues;
    public ArrayList<Request> requests, requestHistory;
    private RecyclerView recyclerView, historyRecyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public TextView mealName, clientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);

        Intent intent = getIntent();
        userValues = intent.getStringArrayExtra("userInfo");
        client = new Client(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7]);

        recyclerView = findViewById(R.id.recyclerMealsResult);

        requests = new ArrayList<Request>();
        FirebaseDatabase.getInstance().getReference("Users").child(client.userID).child("Requests Sent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    Request request= new Request(requestSnapshot.child("mealName").getValue().toString(),requestSnapshot.child("cookUID").getValue().toString(), requestSnapshot.child("clientUID").getValue().toString(),
                            requestSnapshot.child("status").getValue().toString(),requestSnapshot.child("clientName").getValue().toString(),requestSnapshot.child("cookName").getValue().toString());
                    if( request.status.equals("pending")){
                        requests.add(request);
                    }
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

    }
}
