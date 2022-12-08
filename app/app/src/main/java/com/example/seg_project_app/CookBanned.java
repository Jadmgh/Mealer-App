package com.example.seg_project_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CookBanned extends AppCompatActivity {

    private TextView txtSignOut;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_banned);

        textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();

        String banInfo = intent.getStringExtra("ban info");
        if(banInfo.equals("")){
            textView.setText("You are permanently banned from this app.");
        }
        else{
            textView.setText("You are temporarily banned from this app, you will be unbanned on "+banInfo+".");
        }
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this::onClick);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                return;
        }
    }
}