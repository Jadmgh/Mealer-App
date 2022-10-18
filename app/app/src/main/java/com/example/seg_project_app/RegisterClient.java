package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editFirstName, editLastName, editInputEmail, editMakePassword, editAddress, editCreditCard, editCCV;
    private Button btnMakeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        mAuth = FirebaseAuth.getInstance();

        editFirstName= (EditText) findViewById(R.id.editFirstName);
        editLastName= (EditText) findViewById(R.id.editLastName);
        editInputEmail = (EditText) findViewById(R.id.editInputEmail);
        editMakePassword = (EditText) findViewById(R.id.editMakePassword);
        editCreditCard = (EditText) findViewById(R.id.editCreditCard);
        editCCV = (EditText) findViewById(R.id.editCVV);
        editAddress = (EditText) findViewById(R.id.editAddress);

        btnMakeClient = (Button) findViewById(R.id.btnMakeClient) ;

        btnMakeClient.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMakeClient:
                makeClient();
                break;
        }
    }

    private  void makeClient(){

        String email = editInputEmail.getText().toString().trim();
        String password = editMakePassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String creditCard = editCreditCard.getText().toString().trim();
        String CVV = editCCV.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        if (email.isEmpty()){
            editInputEmail.setError("Email is required");
            editInputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editMakePassword.setError("Password is required");
            editMakePassword.requestFocus();
            return;
        }

        if (firstName.isEmpty()){
            editFirstName.setError("First Name is required");
            editFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()){
            editLastName.setError("Last Name is required");
            editLastName.requestFocus();
            return;
        }

        if (creditCard.isEmpty()){
            editCreditCard.setError("Credit Card is required");
            editCreditCard.requestFocus();
            return;
        }

        if (CVV.isEmpty()){
            editCCV.setError("CVV is required");
            editCCV.requestFocus();
            return;
        }

        if (address.isEmpty()){
            editAddress.setError("Address is required");
            editAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editInputEmail.setError("Please provide valid email");
            editInputEmail.requestFocus();
            return;
        }

        if (password.length() <6){
            editMakePassword.setError("Minimum password length should be 6 characters!");
            editMakePassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new Client(firstName,lastName,email,password,address,creditCard,CVV);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterClient.this, "You have succsefully been registered as a client", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterClient.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(RegisterClient.this, "Failed to register client. Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(RegisterClient.this, "Failed to register client", Toast.LENGTH_LONG).show();
                }

            }

        });
    }
}