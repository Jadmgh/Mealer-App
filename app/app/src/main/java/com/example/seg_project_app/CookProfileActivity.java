package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private Button btnCreateMeal;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public TextView mealName, mealType, mealCuisine ,mealAllergens, mealPrice, mealDescription, mealIngredients;
    public EditText editMealName, editMealType,editCuisineType,editAllergens, editMealPrice, editMealDescription, editIngredients;
    public Button btnMakeMeal, btnAddMeal,btnDeleteMealFromMenu, btnDeleteMealFromAllMenus, btnDeleteMealFromOffered;

    public ListView menuListView, offeredMealsListView;
    public ArrayList<Meal> menuList, offeredMealsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile_temp);
        text = (TextView) findViewById(R.id.textView);

        btnCreateMeal = (Button) findViewById(R.id.btnCreateMeal);
        btnCreateMeal.setOnClickListener(this);

        menuListView = (ListView) findViewById(R.id.menuList);
        offeredMealsListView = (ListView) findViewById(R.id.offeredMealsList);

        menuList = new ArrayList<Meal>();
        offeredMealsList = new ArrayList<Meal>();

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
        switch (view.getId()) {
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CookProfileActivity.this, MainActivity.class));
                return;
            case R.id.btnCreateMeal:
                createNewMealDialog();
                getMenuFromFirebase();
                return;
        }
    }

    public void createNewMealDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View mealPopupView = getLayoutInflater().inflate(R.layout.activity_create_meal,null);
        editMealName = (EditText) mealPopupView.findViewById(R.id.editMealName);
        editMealType = (EditText) mealPopupView.findViewById(R.id.editMealType);
        editCuisineType = (EditText) mealPopupView.findViewById(R.id.editCuisineType);
        editAllergens = (EditText) mealPopupView.findViewById(R.id.editAllergens);
        editMealPrice = (EditText) mealPopupView.findViewById(R.id.editMealPrice);
        editMealDescription = (EditText) mealPopupView.findViewById(R.id.editMealDescription);
        editIngredients = (EditText) mealPopupView.findViewById(R.id.editIngredients);

        btnMakeMeal = (Button) mealPopupView.findViewById(R.id.btnMakeMeal);

        dialogBuilder.setView(mealPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnMakeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> ingredients = new ArrayList<String>();
                String[] ingredientsArray = editIngredients.getText().toString().split(",");
                for (int i = 0; i < ingredientsArray.length; i++) {
                    ingredients.add(ingredientsArray[i]);
                }
                //TODO: check if all inputs are right
                //TODO: add meal to menu
                dialog.dismiss();
            }
        });
    }

    public void openMealNotOfferedFromDatabase(String mealName){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Menu").child(mealName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot mealSnapshot) {
                if(mealSnapshot.child("mealName").getValue() !=null) {
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                        ingredients.add(ingredientSnapshot.getValue().toString());
                    }
                    Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients);

                    openMealNotOfferedDialog(meal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openMealNotOfferedDialog(Meal meal) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View mealPopupView = getLayoutInflater().inflate(R.layout.activity_meal_not_offered,null);
        mealName = (TextView) mealPopupView.findViewById(R.id.mealName);
        mealType = (TextView) mealPopupView.findViewById(R.id.mealType);
        mealCuisine = (TextView) mealPopupView.findViewById(R.id.mealCuisine);
        mealAllergens = (TextView) mealPopupView.findViewById(R.id.mealAllergens);
        mealPrice = (TextView) mealPopupView.findViewById(R.id.mealPrice);
        mealDescription = (TextView) mealPopupView.findViewById(R.id.mealDescription);
        mealIngredients = (TextView) mealPopupView.findViewById(R.id.mealIngeredients);

        btnAddMeal = (Button) mealPopupView.findViewById(R.id.btnDeleteFromOffered);
        btnDeleteMealFromMenu = (Button) mealPopupView.findViewById(R.id.btnDeleteMealFromAllMenus);

        dialogBuilder.setView(mealPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mealName.setText(meal.mealName);
        mealType.setText(meal.mealType);
        mealCuisine.setText(meal.mealCuisine);
        mealAllergens.setText(meal.mealAllergens);
        mealPrice.setText(meal.mealPrice);
        mealDescription.setText(meal.mealDescription);
        String ingredients = "";

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add meal to offered
                dialog.dismiss();
            }
        });

        btnDeleteMealFromMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //TODO: delete meal from menu
            }
        });
    }
}