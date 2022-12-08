package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    private  Client client;
    private Button btnSearch;
    private Button btnRequests, btnRequestHistory;
    private String[] userValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        text = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        userValues = intent.getStringArrayExtra("userValue");
        client = new Client(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7]);

        text.setText("Hello, "+client.firstName+ ",you are a client");
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnRequestHistory = (Button) findViewById(R.id.btnRequestHistory);
        btnRequestHistory.setOnClickListener(this);

        btnRequests = (Button)findViewById(R.id.btnRequests);
        btnRequests.setOnClickListener(this);
        for(int i = 0; i <userValues.length; i ++){
            System.out.println(userValues[i]);
        }

    }


    @Override
    public void onClick(View view) {
        String[] clientInfo = {userValues[0], userValues[1],userValues[2],userValues[3],userValues[4], userValues[5], userValues[6],userValues[7]};
        switch (view.getId()){
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ClientActivity.this, MainActivity.class));
                return;
            case R.id.btnSearch:
                Intent searchIntent = new Intent(ClientActivity.this,SearchView.class);
                searchIntent.putExtra("userInfo", clientInfo);
                startActivity(searchIntent);
                return;

            case R.id.btnRequests:
                Intent requestIntent = new Intent(ClientActivity.this,ClientRequests.class);
                requestIntent.putExtra("userInfo", clientInfo);
                startActivity(requestIntent);
                return;
            case R.id.btnRequestHistory:
                Intent requestHistoryIntent = new Intent(ClientActivity.this,ClientRequestHistory.class);
                requestHistoryIntent.putExtra("userInfo", clientInfo);
                startActivity(requestHistoryIntent);
                return;
        }

    }
}