package com.example.seg_project_app;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class Cook extends User {

    public String firstName, lastName,email, password, address, description;
    public Drawable cheque;

    public Cook(String firstName, String lastName, String email, String password, String address, String description, Drawable cheque) {
        super(email, password, "cook");
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.description = description;
        this.address = address;
        this.cheque = cheque;
    }




}
//TODO: class cook
//TODO: figure out image for cheque