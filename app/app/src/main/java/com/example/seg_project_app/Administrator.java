package com.example.seg_project_app;

public class Administrator extends User{

    public Administrator(String email, String password){
        super(email,password,"administrator");
    }
    //TODO: figure out if Administrator should be a class, or a child or whatever.
}
