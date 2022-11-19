package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

public class CookProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    public  Cook cook;
    public ArrayList<Meal> menuList, offeredMealsList;

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
                getMenuFromFirebase();
                getOfferedMealsFromFirebase();
            }
        } else if (cook.permanentlyBanned.equals("true")) {
            text.setText(cook.firstName + " " + cook.lastName + ", you are permanently banned from this app. You can no longer use Mealer");
        } else {
            text.setText("Hello, " + cook.firstName + " ,you are a cook");
            getMenuFromFirebase();
            getOfferedMealsFromFirebase();

        }
        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);
    }

    public void getOfferedMealsFromFirebase(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Offered meals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offeredMealsList = new ArrayList<Meal>();
                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                        ingredients.add(ingredientSnapshot.getValue().toString());
                    }
                    Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients);
                    offeredMealsList.add(meal);
                }
                //TODO: offered meals in list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getMenuFromFirebase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Menu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList = new ArrayList<Meal>();
                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                        ingredients.add(ingredientSnapshot.getValue().toString());
                    }
                    Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients);
                    menuList.add(meal);
                }
                //TODO: update list view
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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