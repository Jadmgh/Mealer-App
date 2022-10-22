package com.example.seg_project_app;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class User {

    public String email, password, type;

    public User(){

    }

    public User(String email, String password, String type){
        this.email = email;
        this.password = password;
        this.type = type;
    }

}
