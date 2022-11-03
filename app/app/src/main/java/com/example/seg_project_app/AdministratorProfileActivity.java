package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdministratorProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private static List<Complaint> complaintList;
    public ListView listView;
    public int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_profile);
        complaintList = new ArrayList<>();
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.complaintList);

        updateUI();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String complaintName = (String) o;
//              getComplaintInfo(complaintName);
                openComplaintInfo(complaintName);

            }
        });
    }

    private void updateUI() {
        getComplaints();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdministratorProfileActivity.this, MainActivity.class));
                return;

        }

    }


    private void populateListView() {
        List<String> complaintNames = new ArrayList<String>();

        List<String> test = new ArrayList<>();
        for (int i = 0; i < complaintList.size(); i++) {
            complaintNames.add(complaintList.get(i).complaintName);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdministratorProfileActivity.this, android.R.layout.simple_list_item_1,complaintNames);

        listView.setAdapter(arrayAdapter);
    }

    public void getComplaints(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot complainSnapshot : snapshot.getChildren()) {
                    Complaint complaint= new Complaint(complainSnapshot.child("clientUID").getValue().toString()
                            ,complainSnapshot.child("cookUID").getValue().toString()
                            ,complainSnapshot.child("description").getValue().toString(),complainSnapshot.child("complaintName").getValue().toString() );
                    addComplaintToList(complaint);
                }
                populateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public void addComplaintToList(Complaint complaint){
        complaintList.add(complaint);
    }

    public void openComplaintInfo(String complaintName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints");
        reference.child(complaintName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot complainSnapshot) {
                Intent i = new Intent(AdministratorProfileActivity.this,ComplaintView.class);
                if (complainSnapshot.child("clientUID").getValue()!=null) {
                    String[] complaintInfo = {complainSnapshot.child("clientUID").getValue().toString()
                            , complainSnapshot.child("cookUID").getValue().toString()
                            , complainSnapshot.child("description").getValue().toString(), complainSnapshot.child("complaintName").getValue().toString()};
                    i.putExtra("complaintInfo", complaintInfo);
                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}