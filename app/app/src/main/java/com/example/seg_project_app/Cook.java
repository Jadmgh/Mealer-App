package com.example.seg_project_app;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Cook extends User {

    public String firstName, lastName, address, description,userID;
    public Uri chequeUri;
    private Boolean banned, tempBanned;
    private Date unbanDate;

    public Cook(String firstName, String lastName, String email, String password, String address, String description,String userID) {
        super(email, password, "cook", userID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.address = address;
        banned = false;
        tempBanned = false;
        unbanDate =null;
    }

//    public void setChequeUri(Uri uri) {
//        chequeUri = uri;
//    }

    public Boolean isPermanentlyBanned(){
        return banned;
    }

    public boolean isTempBanned(){
        return tempBanned;
    }

    public String getUnbanDateAsString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(unbanDate);
        return date;
    }

    public void banCook(){
        banned = true;
    }

    public void tempBanCook(int numberOfDays){
        tempBanned=true;
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.DATE,numberOfDays);
        unbanDate = c.getTime();    //gets date after numberOfDays date //TODO: test calendar +
    }

//    public void signIn(Intent intent){
//        //TODO: move sign In to cook, when sign in, check if unbanned
//        CookProfileActivity newActivity = new CookProfileActivity(this);
//        startActivity(new Intent(Cook.this.getActivity(), CookProfileActivity.class));
//    }
}