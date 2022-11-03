package com.example.seg_project_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtRegisterClient, txtRegisterCook;

    private EditText editEmail, editPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private DatabaseReference reference;
    private String userID;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth.getInstance().signOut();

        txtRegisterClient = (TextView) findViewById(R.id.txtRegisterClient);
        txtRegisterClient.setOnClickListener(this);
        txtRegisterCook = (TextView) findViewById(R.id.txtRegisterCook);
        txtRegisterCook.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.txtRegisterClient:
                startActivity(new Intent(this, com.example.seg_project_app.RegisterClient.class));
                break;
            case R.id.btnLogin:
                requirementCheck();
                break;
            case R.id.txtRegisterCook:
                startActivity(new Intent(this, com.example.seg_project_app.RegisterCook.class));
                break;
        }
    }


    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();

        //Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload(currentUser);
//        }

    }

    public void requirementCheck(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please enter a valid email!");
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        if(password.length() <6){
            editPassword.setError("Password has to be at least 6 characters");
            editPassword.requestFocus();
            return;
        }
        userLogin();
    }


    private void userLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Wrong password or email. Please try again!",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editPassword.getText().clear();
                }
            }
        });



    }
    private void updateUI(FirebaseUser user) {

        userID = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userType = user.type;
                if (userType.equals("client")) {
                    startActivity(new Intent(MainActivity.this, ClientProfileActivity.class));
                } else if (userType.equals("administrator")) {
                    Intent i = new Intent(MainActivity.this, AdministratorProfileActivity.class);
                    String[] userValues= {snapshot.child("email").getValue().toString(), snapshot.child("email").getValue().toString(),snapshot.child("userID").getValue().toString()};
                    i.putExtra("userValue", userValues);
                    startActivity(i);
                } else if (userType.equals("cook")) {
                    Intent i = new Intent(MainActivity.this, CookProfileActivity.class);
                    String[] userValues= {snapshot.child("firstName").getValue().toString(), snapshot.child("lastName").getValue().toString(), snapshot.child("email").getValue().toString()
                            ,snapshot.child("password").getValue().toString(),snapshot.child("address").getValue().toString(),snapshot.child("description").getValue().toString(),snapshot.child("userID").getValue().toString()};
                    i.putExtra("userValue", userValues);
                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}