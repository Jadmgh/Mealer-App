package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    private  Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        text = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        String[] userValues = intent.getStringArrayExtra("userValue");
        client = new Client(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7]);

        text.setText("Hello, "+client.firstName+ ",you are a client");
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ClientActivity.this, MainActivity.class));
                return;
        }

    }
}