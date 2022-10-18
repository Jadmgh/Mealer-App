//TODO: Register Cook java + CookProfileActivity + activity_cook_profile.xml


package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterCook extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editFirstName, editLastName, editInputEmail, editMakePassword, editAddress, editDescription;
    private Button btnMakeCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cook);

        mAuth = FirebaseAuth.getInstance();

        editFirstName= (EditText) findViewById(R.id.editFirstName);
        editLastName= (EditText) findViewById(R.id.editLastName);
        editInputEmail = (EditText) findViewById(R.id.editInputEmail);
        editMakePassword = (EditText) findViewById(R.id.editMakePassword);
        editDescription= (EditText) findViewById(R.id.editDescription);
        editAddress = (EditText) findViewById(R.id.editAddress);

        btnMakeCook = (Button) findViewById(R.id.btnMakeCook) ;

        btnMakeCook.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMakeCook:
                makeCook();
                break;
        }
    }

    private  void makeCook(){

        String email = editInputEmail.getText().toString().trim();
        String password = editMakePassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

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

        if (description.isEmpty()){
            editDescription.setError("Description is required");
            editDescription.requestFocus();
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
                    User user = new Cook(firstName, lastName, email, password, address, description);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterCook.this, "You have succsefully been registered as a cook", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterCook.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(RegisterCook.this, "Failed to register cook. Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(RegisterCook.this, "Failed to register client", Toast.LENGTH_LONG).show();
                }

            }

        });
    }
}