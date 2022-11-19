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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                if (requirementsForInput(editMealName.getText().toString(), editMealType.getText().toString(), editCuisineType.getText().toString(), editAllergens.getText().toString(), editMealDescription.getText().toString(), editMealPrice.getText().toString(), ingredients) == true) {

                    Meal meal = new Meal(editMealName.getText().toString(), editMealType.getText().toString(), editCuisineType.getText().toString(), editAllergens.getText().toString(), editMealDescription.getText().toString(), editMealPrice.getText().toString(), ingredients);
                    addMealToMenu(meal);
                    dialog.dismiss();
                }
            }
        });
    }

    public void addMealToMenu(Meal meal){
        FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Menu").child(meal.mealName).setValue(meal)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CookProfileActivity.this, "You have successfully added meal to menu", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(CookProfileActivity.this, "Failed to add meal to menu!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        getMenuFromFirebase();
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

    public boolean requirementsForInput(String mealName, String mealType, String mealCuisine, String mealAllergens,
                                        String mealDescription,String mealPrice, ArrayList<String> ingredients )
    {

        if(mealName.isEmpty()){
            editMealName.setError("Meal name required");
            editMealName.requestFocus();
            return false;
        }
        else if(this.hasNumeric(mealName)){
            editMealName.setError("Only characters are allowed");
            editMealName.requestFocus();
            return false;
        }

        if(mealType.isEmpty()){
            editMealType.setError("Meal type is required");
            editMealType.requestFocus();
            return false;
        }
        else if(this.hasNumeric(mealType)){
            editMealType.setError("Only characters are allowed");
            editMealType.requestFocus();
            return false;
        }

        if(mealCuisine.isEmpty()){
            editCuisineType.setError("Cuisine type is required");
            editCuisineType.requestFocus();
            return false;

        }
        else if(this.hasNumeric(mealCuisine)){
            editCuisineType.setError("Only characters are allowed");
            editCuisineType.requestFocus();
            return false;
        }


        if(mealAllergens.isEmpty()){
            editAllergens.setError("Allergens required");
            editAllergens.requestFocus();
            return false;
        }
        else if(this.hasNumeric(mealAllergens)){
            editAllergens.setError("Only characters are allowed");
            editAllergens.requestFocus();
            return false;
        }

        if(mealDescription.isEmpty()){
            editMealDescription.setError("Description required");
            editMealDescription.requestFocus();
            return false;
        }
        else if(this.hasNumeric(mealDescription)){
            editMealDescription.setError("Only characters are allowed");
            editMealDescription.requestFocus();
            return false;
        }

        if(mealPrice.isEmpty()){
            editMealPrice.setError("Price is required");
            editMealPrice.requestFocus();
            return false;
        }
        if(this.isNumeric(mealPrice) == false){
            editMealPrice.setError("price is numbers only");
            editMealPrice.requestFocus();
            return false;
        }

        for(int i = 0; i < ingredients.size(); i++){
            if(this.hasNumeric(ingredients.get(i))){
                editIngredients.setError("Only characters are allowed");
                editIngredients.requestFocus();
                return false;
            }
        }

        return true;

    }

    public boolean isNumeric(String characters){
        if (characters == null) {
            return false;
        }
        try {
            Double.parseDouble(characters);
            return true;
        } catch (NumberFormatException nfe) {

            return false;
        }
    }
    public boolean hasNumeric(String characters){

        char[] chars = characters.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;

    }

}