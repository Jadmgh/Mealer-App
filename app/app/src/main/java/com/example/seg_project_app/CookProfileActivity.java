package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.prefs.PreferenceChangeEvent;

public class CookProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    public  Cook cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);
        text = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();

        String[] userValues = intent.getStringArrayExtra("userInfo");
        cook = new Cook(userValues[0], userValues[1], userValues[2], userValues[3], userValues[4],
                userValues[5], userValues[6], userValues[7], userValues[8], userValues[9]);
        if (cook.tempBanned.equals("true")) {
            if (cook.pastUnbanDate()){
                text.setText(cook.firstName + " " + cook.lastName + ", you are temporarily banned from this app. You will be unbanned on " + cook.getUnbanDateAsString());
            }
            else{
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(cook.userID).child("unbanDate").setValue("");
                reference.child(cook.userID).child("tempBanned").setValue("false");
                text.setText("Hello, " + cook.firstName + " ,you are a cook");
            }
        } else if (cook.permanentlyBanned.equals("true")) {
            text.setText(cook.firstName + " " + cook.lastName + ", you are permanently banned from this app. You can no longer use Mealer");
        } else {
            text.setText("Hello, " + cook.firstName + " ,you are a cook");

        }
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CookProfileActivity.this, MainActivity.class));
                return;
        }

    }
}