package com.example.seg_project_app;

import java.util.ArrayList;
import java.util.List;

public class Administrator extends User{

    public Administrator(String email, String password, String userID){
        super(email,password,"administrator",userID);
    }
}
