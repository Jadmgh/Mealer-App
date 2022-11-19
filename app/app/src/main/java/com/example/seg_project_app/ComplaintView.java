package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComplaintView extends AppCompatActivity implements View.OnClickListener {

    public TextView txtDescription, txtComplaintName, txtCook, txtClient;
    public Button btnDismiss, btnTempBan, btnPermanentlyBan;
    public Complaint complaint;
    public Cook cook;
    public EditText editNumberOfDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_view);

        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtComplaintName = (TextView) findViewById(R.id.txtComplaintName);
        txtClient = (TextView) findViewById(R.id.txtClient);
        txtCook = (TextView) findViewById(R.id.txtCook);

        editNumberOfDays = (EditText) findViewById(R.id.editInputDays);
        btnDismiss = (Button) findViewById(R.id.btnDismiss);
        btnTempBan = (Button) findViewById(R.id.btnTempBan);
        btnPermanentlyBan = (Button) findViewById(R.id.btnPermanentlyBan);

        btnPermanentlyBan.setOnClickListener(this);
        btnDismiss.setOnClickListener(this);
        btnTempBan.setOnClickListener(this);
        Intent intent = getIntent();
        String[] complaintName = intent.getStringArrayExtra("complaintInfo");

        complaint = new Complaint(complaintName[0],complaintName[1],complaintName[2],complaintName[3]);
        setInfoText();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDismiss:
                dismissComplaint();
                return;
            case R.id.btnPermanentlyBan:
                permanentlyBanCook();
                return;
            case R.id.btnTempBan:
                temporarilyBanCook();
                return;
        }
    }


    private void setInfoText() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(complaint.clientUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String clientName = snapshot.child("firstName").getValue().toString()+snapshot.child("lastName").getValue().toString();
                reference.child(complaint.cookUID).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cook = new Cook(snapshot.child("firstName").getValue().toString(), snapshot.child("lastName").getValue().toString(), snapshot.child("email").getValue().toString(),snapshot.child("password").getValue().toString(),snapshot.child("address").getValue().toString(),
                                snapshot.child("description").getValue().toString(),snapshot.child("userID").getValue().toString(),snapshot.child("permanentlyBanned").getValue().toString(),snapshot.child("tempBanned").getValue().toString(),snapshot.child("unbanDate").getValue().toString());
                        String cookName = snapshot.child("firstName").getValue().toString()+snapshot.child("lastName").getValue().toString();
                        txtClient.setText(clientName);
                        txtCook.setText(cookName);
                        txtDescription.setText(complaint.description);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void permanentlyBanCook() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(complaint.cookUID).child("permanentlyBanned").setValue("true");
        dismissComplaint();
    }
    public boolean isNumeric(String characters){
        if (characters == null) {
            return false;
        }
        try {
            Double.parseDouble(characters);
            return true;
        } catch (NumberFormatException nfe) {

            return false;
        }
    }

    public void temporarilyBanCook() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(complaint.cookUID).child("tempBanned").setValue("true");
        try {
            if (editNumberOfDays.getText()!= null) {
                if (this.isNumeric(editNumberOfDays.getText().toString())== false){

                    editNumberOfDays.setError("must be a numeric value");
                    editNumberOfDays.requestFocus();
                    return;

                }
                int numberOfDays = Integer.parseInt(editNumberOfDays.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(Calendar.getInstance().getTime());
                c.add(Calendar.DATE, numberOfDays);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String unbanDate = dateFormat.format(c.getTime());

                reference.child(complaint.cookUID).child("permanentlyBanned").setValue("true");
                reference.child(complaint.cookUID).child("unbanDate").setValue(unbanDate);
                dismissComplaint();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Please input number of days", Toast.LENGTH_SHORT).show();
        }

    }

    public void dismissComplaint() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Complaints").child(complaint.complaintName).removeValue();
        startActivity(new Intent(ComplaintView.this, AdministratorProfileActivity.class));
    }

};