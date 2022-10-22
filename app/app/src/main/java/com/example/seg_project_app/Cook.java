package com.example.seg_project_app;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

public class Cook extends User {

    public String firstName, lastName,email, password, address, description;
   public Uri chequeUri;

    public Cook(String firstName, String lastName, String email, String password, String address, String description) {
        super(email, password, "cook");
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.description = description;
        this.address = address;
    }

//    public void setChequeUri(Uri uri) {
//        chequeUri = uri;
//    }

}