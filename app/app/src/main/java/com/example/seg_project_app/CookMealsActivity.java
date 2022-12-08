package com.example.seg_project_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CookMealsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtSignOut;
    private TextView text;
    public  Cook cook;
    private Button btnCreateMeal;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public TextView mealName, mealType, mealCuisine ,mealAllergens, mealPrice, mealDescription, mealIngredients;
    public EditText editMealName, editMealType,editCuisineType,editAllergens, editMealPrice, editMealDescription, editIngredients;
    public Button btnMakeMeal, btnAddMeal, btnDeleteMealFromOffered,btnDeleteMealFromMenu,btnBack,btnBackToProfile;
    public String[] userValues;
    public ListView menuListView, offeredMealsListView;
    public ArrayList<Meal> menuList, offeredMealsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_meals);

        btnCreateMeal = (Button) findViewById(R.id.btnCreateMeal);
        btnCreateMeal.setOnClickListener(this);

        menuListView = (ListView) findViewById(R.id.menuList);
        offeredMealsListView = (ListView) findViewById(R.id.offeredMealsList);

        menuList = new ArrayList<Meal>();
        offeredMealsList = new ArrayList<Meal>();

        Intent intent = getIntent();

        userValues = intent.getStringArrayExtra("userInfo");
        cook = new Cook(userValues[0], userValues[1], userValues[2], userValues[3], userValues[4],
                userValues[5], userValues[6], userValues[7], userValues[8], userValues[9]);

        cook.rating = userValues[10];
        getMenuFromFirebase();
        getOfferedMealsFromFirebase();

        txtSignOut = (TextView) findViewById(R.id.txtSignOut);
        txtSignOut.setOnClickListener(this);

        offeredMealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = offeredMealsListView.getItemAtPosition(position);
                String mealName = (String) o;
                openMealOfferedFromDatabase(mealName);
            }
        });

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = menuListView.getItemAtPosition(position);
                String mealName = (String) o;
                openMealNotOfferedFromDatabase(mealName);

            }
        });

        btnBackToProfile = (Button) findViewById(R.id.btnBackToProfile);
        btnBackToProfile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CookMealsActivity.this, MainActivity.class));
                return;
            case R.id.btnCreateMeal:
                createNewMealDialog();
                getMenuFromFirebase();
                return;
            case R.id.btnBackToProfile:
                Intent i = new Intent(CookMealsActivity.this, CookActivity.class);
                i.putExtra("userInfo", userValues);
                startActivity(i);
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
                //Requirements check


                ArrayList<String> ingredients = stringToArrayList(editIngredients.getText().toString());
                if (requirementsForInput(editMealName.getText().toString(), editMealType.getText().toString(), editCuisineType.getText().toString(), editAllergens.getText().toString(), editMealDescription.getText().toString(), editMealPrice.getText().toString(), ingredients) == true) {

                    Meal meal = new Meal(editMealName.getText().toString(), editMealType.getText().toString(), editCuisineType.getText().toString(), editAllergens.getText().toString(), editMealDescription.getText().toString(), editMealPrice.getText().toString(), ingredients, cook.firstName, cook.lastName,cook.rating,cook.userID);
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
                            Toast.makeText(CookMealsActivity.this, "You have successfully added meal to menu", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(CookMealsActivity.this, "Failed to add meal to menu!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        getMenuFromFirebase();
    }

    public boolean requirementsForInput(String mealName, String mealType, String mealCuisine, String mealAllergens,
                                        String mealDescription,String mealPrice, ArrayList<String> ingredients)
    {
        Meal meal = new Meal( mealName,  mealType,  mealCuisine, mealAllergens,
                mealDescription,mealPrice, ingredients, cook.firstName, cook.lastName,cook.rating,cook.userID);

        if(mealName.isEmpty()){
            editMealName.setError("Meal name required");
            editMealName.requestFocus();
            return false;
        }
        else if(meal.hasNumeric(mealName)){
            editMealName.setError("Only characters are allowed");
            editMealName.requestFocus();
            return false;
        }

        if(mealType.isEmpty()){
            editMealType.setError("Meal type is required");
            editMealType.requestFocus();
            return false;
        }
        else if(meal.hasNumeric(mealType)){
            editMealType.setError("Only characters are allowed");
            editMealType.requestFocus();
            return false;
        }

        if(mealCuisine.isEmpty()){
            editCuisineType.setError("Cuisine type is required");
            editCuisineType.requestFocus();
            return false;

        }
        else if(meal.hasNumeric(mealCuisine)){
            editCuisineType.setError("Only characters are allowed");
            editCuisineType.requestFocus();
            return false;
        }


        if(mealAllergens.isEmpty()){
            editAllergens.setError("Allergens required");
            editAllergens.requestFocus();
            return false;
        }
        else if(meal.hasNumeric(mealAllergens)){
            editAllergens.setError("Only characters are allowed");
            editAllergens.requestFocus();
            return false;
        }

        if(mealDescription.isEmpty()){
            editMealDescription.setError("Description required");
            editMealDescription.requestFocus();
            return false;
        }
        else if(meal.hasNumeric(mealDescription)){
            editMealDescription.setError("Only characters are allowed");
            editMealDescription.requestFocus();
            return false;
        }

        if(mealPrice.isEmpty()){
            editMealPrice.setError("Price is required");
            editMealPrice.requestFocus();
            return false;
        }
        if(meal.isNumeric(mealPrice) == false){
            editMealPrice.setError("price is numbers only");
            editMealPrice.requestFocus();
            return false;
        }

        if (ingredients.size()==0){
            editIngredients.setError("Input at least one ingredients");
            editIngredients.requestFocus();
            return false;
        }
        for(int i = 0; i < ingredients.size(); i++){
            if(meal.hasNumeric(ingredients.get(i))){
                editIngredients.setError("Only characters are allowed");
                editIngredients.requestFocus();
                return false;
            }
        }

        return true;

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
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                            mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(),mealSnapshot.child("cookRating").getValue().toString(),mealSnapshot.child("cookUID").getValue().toString());
                    menuList.add(meal);
                }
                menuListViewUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                            mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(),mealSnapshot.child("cookRating").getValue().toString(),mealSnapshot.child("cookUID").getValue().toString());
                    offeredMealsList.add(meal);
                }
                offeredMealsListViewUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                            mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(),mealSnapshot.child("cookRating").getValue().toString(),mealSnapshot.child("cookUID").getValue().toString());

                    openMealNotOfferedDialog(meal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void openMealOfferedFromDatabase(String mealName){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Offered meals").child(mealName);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot mealSnapshot) {
                if(mealSnapshot.child("mealName").getValue() !=null) {
                    ArrayList<String> ingredients = new ArrayList<String>();
                    for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                        ingredients.add(ingredientSnapshot.getValue().toString());
                    }
                    Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                            , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients
                    ,mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(),mealSnapshot.child("cookRating").getValue().toString(),mealSnapshot.child("cookUID").getValue().toString());

                    openMealOfferedDialog(meal);
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
        String ingredients = cook.ingredientsToString(meal.ingredients);

        mealIngredients.setText(ingredients);
        btnBack = (Button) mealPopupView.findViewById(R.id.btnBackToProfile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealToOfferedMeals(meal);
                dialog.dismiss();
            }
        });

        btnDeleteMealFromMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                deleteMealFromMenu(meal);
            }
        });
    }

    private void openMealOfferedDialog(Meal meal) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View mealPopupView = getLayoutInflater().inflate(R.layout.activity_meal_offered,null);
        mealName = (TextView) mealPopupView.findViewById(R.id.mealName);
        mealType = (TextView) mealPopupView.findViewById(R.id.mealType);
        mealCuisine = (TextView) mealPopupView.findViewById(R.id.mealCuisine);
        mealAllergens = (TextView) mealPopupView.findViewById(R.id.mealAllergens);
        mealPrice = (TextView) mealPopupView.findViewById(R.id.mealPrice);
        mealDescription = (TextView) mealPopupView.findViewById(R.id.mealDescription);
        mealIngredients = (TextView) mealPopupView.findViewById(R.id.mealIngeredients);

        mealName.setText(meal.mealName);
        mealType.setText(meal.mealType);
        mealCuisine.setText(meal.mealCuisine);
        mealAllergens.setText(meal.mealAllergens);
        mealPrice.setText(meal.mealPrice);
        mealDescription.setText(meal.mealDescription);
        String ingredients = ingredientsToString(meal.ingredients);
        btnBack = (Button) mealPopupView.findViewById(R.id.btnBackToProfile);

        mealIngredients.setText(ingredients);

        btnDeleteMealFromOffered = (Button) mealPopupView.findViewById(R.id.btnDeleteFromOffered);

        dialogBuilder.setView(mealPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDeleteMealFromOffered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                dialog.dismiss();
                deleteMealFromOfferedMeals(meal);       //TODO: check if delete works
            }
        });
    }

    public ArrayList<String> stringToArrayList(String ingredientsString){
        ArrayList<String> ingredients = new ArrayList<String>();
        String[] ingredientsArray = ingredientsString.split(",");
        for (int i = 0; i < ingredientsArray.length; i++) {
            ingredients.add(ingredientsArray[i]);
        }
        return ingredients;
    }

    public String ingredientsToString(ArrayList<String> newingredients){
        String ingredients = "";
        for (int i = 0; i < newingredients.size(); i++) {
            if (i+1 == newingredients.size()) {
                ingredients = ingredients + newingredients.get(i) ;
            }
            else {
                ingredients = ingredients + newingredients.get(i) + ",";
            }
        }
        return ingredients;
    }

    public void deleteMealFromMenu(Meal meal){
        FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Offered meals").child(meal.mealName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Users").child(cook.userID).child("Menu").child(meal.mealName).removeValue();
                    deleteMealFromOfferedMeals(meal);       //TODO: ask if deleted from offered too
                    getMenuFromFirebase();
                    dialog.dismiss();

                }
                else {
                    Toast.makeText(CookMealsActivity.this, "Cannot delete meal from menu since it is an offered meal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CookMealsActivity.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addMealToOfferedMeals(Meal meal){
        FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Menu").child(meal.mealName).child("offered").setValue("true");
        FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Offered meals").child(meal.mealName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference("Users").child(cook.userID).child("Offered meals").child(meal.mealName).setValue(meal)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Offered Meals").child(meal.mealName).setValue(meal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(CookMealsActivity.this, "You have successfully added meal to offered meals", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(CookMealsActivity.this, "Failed to add meal to offered meals!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    getOfferedMealsFromFirebase();
                }
                else {
                    Toast.makeText(CookMealsActivity.this, "Cannot offer meal from menu since it is already offered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CookMealsActivity.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteMealFromOfferedMeals(Meal meal){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(cook.userID).child("Offered meals").child(meal.mealName).removeValue();
        ref.child("Offered Meals").child(meal.mealName).removeValue();
        getOfferedMealsFromFirebase();
    }


    public void menuListViewUpdate(){
        List<String> menuNames = new ArrayList<String>();

        for (int i = 0; i < menuList.size(); i++) {
            menuNames.add(menuList.get(i).mealName);
        }


        ArrayAdapter<String> adapterMealNames = new ArrayAdapter<String>(CookMealsActivity.this, android.R.layout.simple_list_item_1,menuNames);

        menuListView.setAdapter(adapterMealNames);
    }

    public void offeredMealsListViewUpdate(){
        List<String> offeredMealsNames = new ArrayList<String>();
        for (int i = 0; i < offeredMealsList.size(); i++) {
            offeredMealsNames.add(offeredMealsList.get(i).mealName);
        }

        ArrayAdapter<String> adapterOfferedMeals = new ArrayAdapter<String>(CookMealsActivity.this, android.R.layout.simple_list_item_1,offeredMealsNames);
        offeredMealsListView.setAdapter(adapterOfferedMeals);
    }

}