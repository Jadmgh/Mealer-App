
package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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


    private Uri imageUri;

    private FirebaseAuth mAuth;

    private EditText editFirstName, editLastName, editInputEmail, editMakePassword, editAddress, editDescription;
    private Button btnMakeCook;
    private ImageView imgCheque;
    private Button btnBack, openCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cook);
        btnBack=(Button) findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();

        editFirstName= (EditText) findViewById(R.id.editFirstName);
        editLastName= (EditText) findViewById(R.id.editLastName);
        editInputEmail = (EditText) findViewById(R.id.editInputEmail);
        editMakePassword = (EditText) findViewById(R.id.editMakePassword);
        editDescription= (EditText) findViewById(R.id.editDescription);
        editAddress = (EditText) findViewById(R.id.editAddress);

        btnMakeCook = (Button) findViewById(R.id.btnMakeCook) ;

        imgCheque = (ImageView) findViewById(R.id.imgCheque);

        openCamera = (Button) findViewById(R.id.openCamera);

        btnMakeCook.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgCheque.setOnClickListener(this);
        openCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMakeCook:
                makeCook();
                break;
            case R.id.openCamera:
                Intent camera = new Intent();
                camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,100);
                break;
            case R.id.btnBack:
                Intent back = new Intent(RegisterCook.this, MainActivity.class);
                startActivity(back);
                break;
            case R.id.imgCheque:
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,10);

        }
    }

    private  void makeCook() {

        boolean verifiedInfo = true;


        String email = editInputEmail.getText().toString().trim();
        String password = editMakePassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        Drawable cheque = imgCheque.getDrawable();

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

        if (description.isEmpty()) {
            editDescription.setError("Description is required");
            editDescription.requestFocus();
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
//        Cook user = new Cook(firstName, lastName, email, password, address, description);
//        user.setChequeUri(imageUri);

        if (verifiedInfo == true) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                    } else {
                        Toast.makeText(RegisterCook.this, "Failed to register cook", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }

    public boolean isAlpha(String name) {

        return name.matches("[a-zA-Z]+");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null ){
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            imgCheque.setImageBitmap(finalPhoto);
        }

        if (requestCode == 10 && data != null && data.getData() != null){

            imageUri = data.getData();

            imgCheque.setImageURI(imageUri);

        }
    }
}