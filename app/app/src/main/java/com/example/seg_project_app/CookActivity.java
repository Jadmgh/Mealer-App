package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CookActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    public  Cook cook;
    private Button btnSeeMeals, btnSeeYourRequests, btnOpenProfile;
    public String[] userValues;

    public CookActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        text = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();

        userValues = intent.getStringArrayExtra("userInfo");
        cook = new Cook(userValues[0], userValues[1], userValues[2], userValues[3], userValues[4],
                userValues[5], userValues[6], userValues[7], userValues[8], userValues[9]);

        cook.rating = userValues[10];
        if (cook.permanentlyBanned.equals("true")) {
            Intent i = new Intent(this, CookBanned.class);
            i.putExtra("ban info", "");
            startActivity(i);
        }
        else if (cook.tempBanned.equals("true")) {
            if (cook.pastUnbanDate()) {
                Intent i = new Intent(this, CookBanned.class);
                i.putExtra("ban info", cook.getUnbanDateAsString());
                startActivity(i);
            } else {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(cook.userID).child("unbanDate").setValue("");
                reference.child(cook.userID).child("tempBanned").setValue("false");
                text.setText("Hello, " + cook.firstName + " ,you are a cook");
            }
        } else {
            text.setText("Hello, " + cook.firstName + " ,you are a cook");

        }

        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);

        btnSeeMeals = (Button) findViewById(R.id.btnOpenMeals);
        btnSeeMeals.setOnClickListener(this);

        btnSeeYourRequests = (Button) findViewById(R.id.btnOpenRequests);
        btnSeeYourRequests.setOnClickListener(this);

        btnOpenProfile = (Button) findViewById(R.id.btnOpenProfile);
        btnOpenProfile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CookActivity.this, MainActivity.class));
                return;
            case R.id.btnOpenMeals:
                Intent i = new Intent(CookActivity.this,CookMealsActivity.class);
                i.putExtra("userInfo", userValues);
                startActivity(i);
                return;
            case R.id.btnOpenRequests:
                Intent cookRequestsIntent = new Intent(CookActivity.this,CookRequests.class);
                cookRequestsIntent.putExtra("userInfo", userValues);
                startActivity(cookRequestsIntent);
                return;
            case R.id.btnOpenProfile:
                Intent cookProfileIntent = new Intent(CookActivity.this,CookProfile.class);
                cookProfileIntent.putExtra("userInfo", userValues);
                startActivity(cookProfileIntent);
                return;
        }
    }

}