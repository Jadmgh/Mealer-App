package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CookProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    private Cook cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);

        text = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        String[] userValues = intent.getStringArrayExtra("userValue");
        cook = new Cook(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6]);

        text.setText("Hello, "+cook.firstName+ ",you are a cook"+ "uid = "+cook.userID);
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