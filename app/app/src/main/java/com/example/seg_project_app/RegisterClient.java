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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editFirstName, editLastName, editInputEmail, editMakePassword, editAddress, editCreditCard, editCCV;
    private Button btnMakeClient;
    private Button regclient;

    private boolean newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        regclient = findViewById(R.id.regclient);
        regclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterClient.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editInputEmail = (EditText) findViewById(R.id.editInputEmail);
        editMakePassword = (EditText) findViewById(R.id.editMakePassword);
        editCreditCard = (EditText) findViewById(R.id.editCreditCard);
        editCCV = (EditText) findViewById(R.id.editCVV);
        editAddress = (EditText) findViewById(R.id.editAddress);

        btnMakeClient = (Button) findViewById(R.id.btnMakeClient);

        btnMakeClient.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMakeClient:
                makeClient();
                break;
        }
    }

    private void makeClient() {

        boolean verifiedInfo = true;

        String email = editInputEmail.getText().toString().trim();
        String password = editMakePassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String creditCard = editCreditCard.getText().toString().trim();
        String CVV = editCCV.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        if (email.isEmpty()) {
            editInputEmail.setError("Email is required");
            editInputEmail.requestFocus();
            verifiedInfo = false;
            return;
        }

        if (password.isEmpty()) {
            editMakePassword.setError("Password is required");
            editMakePassword.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (firstName.isEmpty()) {
            editFirstName.setError("First Name is required");
            editFirstName.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (lastName.isEmpty()) {
            editLastName.setError("Last Name is required");
            editLastName.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (!isAlpha(firstName)) {
            editFirstName.setError("First Name has to contain only letters");
            editFirstName.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (!isAlpha(lastName)) {
            editLastName.setError("Last Name has to contain only letters");
            editLastName.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (creditCard.isEmpty()) {
            editCreditCard.setError("Credit Card is required");
            editCreditCard.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (CVV.isEmpty()) {
            editCCV.setError("CVV is required");
            editCCV.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (address.isEmpty()) {
            editAddress.setError("Address is required");
            editAddress.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editInputEmail.setError("Please provide valid email");
            editInputEmail.requestFocus();
            verifiedInfo = false;


            return;
        }

        if (password.length() < 6) {
            editMakePassword.setError("Minimum password length should be 6 characters!");
            editMakePassword.requestFocus();
            verifiedInfo = false;

            return;

        }
        if (creditCard.length() != 16) {
            editCreditCard.setError("Credit card must be 16 digits");
            editCreditCard.requestFocus();
            verifiedInfo = false;

            return;
        }
        if (isNumeric(creditCard) == false) {
            editCreditCard.setError("Card requires only numbers");
            editCreditCard.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (CVV.length() != 3) {
            editCCV.setError("CVV must be 3 digits");
            editAddress.requestFocus();
            verifiedInfo = false;

            return;
        }

        if (!isNumeric(CVV)) {
            editCCV.setError("CVV requires only numbers");
            editAddress.requestFocus();
            verifiedInfo = false;

            return;
        }

//        if (verifiedInfo == true) {
//            mAuth.fetchSignInMethodsForEmail(email)
//                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                            newUser = task.getResult().getSignInMethods().isEmpty();
//
//                        }
//                    });
//            if (newUser == true) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            int creditCardNumber = Integer.parseInt(creditCard);
//                            int cvvNumber = Integer.parseInt(CVV);
                            User user = new Client(firstName, lastName, email, password, address, creditCard, CVV);

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
                        } else {
                            Toast.makeText(RegisterClient.this, "Failed to register client", Toast.LENGTH_LONG).show();
                        }

                    }

                });
//            } else {
//                Toast.makeText(this, "Account with that email already exists", Toast.LENGTH_LONG).show();
//                editInputEmail.getText().clear();
//            }
//        }
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

}