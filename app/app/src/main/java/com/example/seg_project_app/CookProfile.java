


package com.example.seg_project_app;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CookProfile extends AppCompatActivity {

    public String[] userValues;
    public  Cook cook;
    public String rating;
    private double ratingSum = 0;
    private boolean suspended = false;
    private int completedOrders, numReviews, numNumStars;
    public TextView txtCookName,txtCookAddress,txtCookDescription;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);

        RatingBar RatingBar;
        RatingBar simpleRatingBar = (RatingBar) = (RatingBar) findViewById(R.id.clientToCookRating);
        Intent intent = getIntent();

        userValues = intent.getStringArrayExtra("userInfo");
        cook = new Cook(userValues[0], userValues[1], userValues[2], userValues[3], userValues[4],
                userValues[5], userValues[6], userValues[7], userValues[8], userValues[9]);
        rating = userValues[10];

        txtCookAddress = (TextView) findViewById(R.id.txtCookAddress);
        txtCookName = (TextView) findViewById(R.id.txtCookName);
        txtCookDescription = (TextView) findViewById(R.id.txtCookDescription);

        txtCookDescription.setText(cook.description);
        txtCookAddress.setText(cook.address);
        txtCookName.setText(cook.firstName + " "+cook.lastName);

        if (rating !="") {
            simpleRatingBar.setRating(Float.parseFloat(rating));
            simpleRatingBar.setIsIndicator(true);
        }
    }

//    public double getRating(){
//        return ratingSum/completedOrders;
//    }
//
//    public void setSuspended() {
//        suspended = true;
//    }
//
//    public boolean addRating(double x){
//        if (x>5 ||x<0){
//            return false;
//        }
//        ratingSum +=x;
//        numReviews++;
//        return true;
//
//    }


}