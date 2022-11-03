package com.example.seg_project_app;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Complaint {

    public String clientUID;                       // client that sent the complaint
    public String cookUID;                           // cook that received the complaint
    public String description;
    public Cook cook;
    public String complaintName;

    public Complaint(String clientUID, String cookUID, String description, String complaintName) {
        this.clientUID = clientUID;
        this.cookUID = cookUID;
        this.description = description;
        this.complaintName = complaintName;
    }

}


