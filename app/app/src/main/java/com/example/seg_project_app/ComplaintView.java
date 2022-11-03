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

    }


    @Override
    public void onClick(View view) {

    }




}