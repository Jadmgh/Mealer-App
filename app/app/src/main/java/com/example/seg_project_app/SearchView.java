package com.example.seg_project_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchView extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, SearchRecyclerAdapter.OnMealListener {

    public Spinner spinnerMealType, spinnerMealCuisine;
    public String typeToSearch, cuisineToSearch, nameToSearch;
    public EditText editNameSearch;
    private ArrayList<Meal> mealList;
    private RecyclerView recyclerView;
    public Button btnSearch, btnRequest, btnBack;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public Client client;
    private String[] userValues;
    public TextView mealName, mealType, mealCuisine ,mealAllergens, mealPrice, mealDescription, mealIngredients, cookName, cookRating;
   public static ArrayList<Meal> requestedMeals;
   FirebaseDatabase rootnode;
   DatabaseReference  reference;
   DatabaseReference reference2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);


        Intent intent = getIntent();
        userValues = intent.getStringArrayExtra("userInfo");
        client = new Client(userValues[0],userValues[1], userValues[2],userValues[3],userValues[4],userValues[5],userValues[6],userValues[7]);

        spinnerMealType = findViewById(R.id.spinnerMealType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.meal_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);
        spinnerMealType.setOnItemSelectedListener(this);

        spinnerMealCuisine = findViewById(R.id.spinnerMealCuisine);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.meal_cuisine_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealCuisine.setAdapter(adapter2);
        spinnerMealCuisine.setOnItemSelectedListener(this);

        editNameSearch = (EditText) findViewById(R.id.editNameSearch);
        nameToSearch = editNameSearch.getText().toString().trim();

        mealList = new ArrayList<Meal>();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);



        recyclerView = findViewById(R.id.recyclerMealsResult);

        cuisineToSearch = "Any";
        typeToSearch = "Any";
        makeMealList();
    }

    private void setAdapter() {
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(mealList,this );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void makeMealList() {
        mealList.clear();
        nameToSearch = editNameSearch.getText().toString().trim().toLowerCase();

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot mealsSnapshot = snapshot.child("Offered Meals");
                DataSnapshot usersSnapshot = snapshot.child("Users");
                if (typeToSearch.equals("Any") && cuisineToSearch.equals("Any")){
                    for (DataSnapshot mealSnapshot : mealsSnapshot.getChildren()) {
                        if (mealSnapshot.child("mealName").getValue().toString().toLowerCase().contains(nameToSearch)) {
                            ArrayList<String> ingredients = new ArrayList<String>();
                            for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                                ingredients.add(ingredientSnapshot.getValue().toString());
                            }
                            Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                                    , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                                    mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(), mealSnapshot.child("cookRating").getValue().toString(), mealSnapshot.child("cookUID").getValue().toString());
                            if (usersSnapshot.child(meal.cookUID).child("permanentlyBanned").getValue().toString().equals("false") && usersSnapshot.child(meal.cookUID).child("tempBanned").getValue().toString().equals("false") ) {
                                mealList.add(meal);
                            }

                        }
                    }
                }
                else if (!typeToSearch.equals("Any")&& cuisineToSearch.equals("Any")){
                    for (DataSnapshot mealSnapshot : mealsSnapshot.getChildren()) {
                        if (mealSnapshot.child("mealName").getValue().toString().toLowerCase().contains(nameToSearch) && mealSnapshot.child("mealType").getValue().toString().equals(typeToSearch) ) {
                            ArrayList<String> ingredients = new ArrayList<String>();
                            for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                                ingredients.add(ingredientSnapshot.getValue().toString());
                            }
                            Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                                    , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                                    mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(), mealSnapshot.child("cookRating").getValue().toString(), mealSnapshot.child("cookUID").getValue().toString());
                            if (usersSnapshot.child(meal.cookUID).child("permanentlyBanned").getValue().toString().equals("false") && usersSnapshot.child(meal.cookUID).child("tempBanned").getValue().toString().equals("false") ) {
                                mealList.add(meal);
                            }
                        }
                    }
                }
                else if (typeToSearch.equals("Any")&& !cuisineToSearch.equals("Any")){
                    for (DataSnapshot mealSnapshot : mealsSnapshot.getChildren()) {
                        if (mealSnapshot.child("mealName").getValue().toString().toLowerCase().contains(nameToSearch) && mealSnapshot.child("mealCuisine").getValue().toString().equals(cuisineToSearch) ) {
                            ArrayList<String> ingredients = new ArrayList<String>();
                            for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                                ingredients.add(ingredientSnapshot.getValue().toString());
                            }
                            Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                                    , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                                    mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(), mealSnapshot.child("cookRating").getValue().toString(), mealSnapshot.child("cookUID").getValue().toString());
                            if (usersSnapshot.child(meal.cookUID).child("permanentlyBanned").getValue().toString().equals("false") && usersSnapshot.child(meal.cookUID).child("tempBanned").getValue().toString().equals("false")) {
                                mealList.add(meal);
                            }
                        }
                    }
                }
                else{
                    for (DataSnapshot mealSnapshot : mealsSnapshot.getChildren()) {
                        if (mealSnapshot.child("mealName").getValue().toString().contains(nameToSearch) && mealSnapshot.child("mealCuisine").getValue().toString().equals(cuisineToSearch) && mealSnapshot.child("mealType").getValue().toString().equals(typeToSearch) ) {
                            ArrayList<String> ingredients = new ArrayList<String>();
                            for (DataSnapshot ingredientSnapshot : mealSnapshot.child("ingredients").getChildren()) {
                                ingredients.add(ingredientSnapshot.getValue().toString());
                            }
                            Meal meal = new Meal(mealSnapshot.child("mealName").getValue().toString(), mealSnapshot.child("mealType").getValue().toString(), mealSnapshot.child("mealCuisine").getValue().toString()
                                    , mealSnapshot.child("mealAllergens").getValue().toString(), mealSnapshot.child("mealDescription").getValue().toString(), mealSnapshot.child("mealPrice").getValue().toString(), ingredients,
                                    mealSnapshot.child("cookFirstName").getValue().toString(), mealSnapshot.child("cookLastName").getValue().toString(), mealSnapshot.child("cookRating").getValue().toString(), mealSnapshot.child("cookUID").getValue().toString());
                            if (usersSnapshot.child(meal.cookUID).child("permanentlyBanned").getValue().toString().equals("false") && usersSnapshot.child(meal.cookUID).child("tempBanned").getValue().toString().equals("false")) {
                                mealList.add(meal);
                            }
                        }
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerMealType:
                spinnerMealType.setSelection(position);
                typeToSearch = (String) spinnerMealType.getSelectedItem();
                return;
            case R.id.spinnerMealCuisine:
                spinnerMealCuisine.setSelection(position);
                cuisineToSearch = (String) spinnerMealCuisine.getSelectedItem();
                return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                makeMealList();
                return;

        }
    }

    @Override
    public void onMealClick(int position) {
        Meal meal = mealList.get(position);

        dialogBuilder = new AlertDialog.Builder(this);
        final View mealPopupView = getLayoutInflater().inflate(R.layout.activity_meal_info, null);
        mealName = (TextView) mealPopupView.findViewById(R.id.txtMealName);
        mealType = (TextView) mealPopupView.findViewById(R.id.txtMealType);
        mealCuisine = (TextView) mealPopupView.findViewById(R.id.txtMealCuisine);
        mealAllergens = (TextView) mealPopupView.findViewById(R.id.txtMealAllergens);
        mealPrice = (TextView) mealPopupView.findViewById(R.id.txtMealPrice);
        mealDescription = (TextView) mealPopupView.findViewById(R.id.txtMealDescription);
        mealIngredients = (TextView) mealPopupView.findViewById(R.id.txtIngredients);

        cookName = (TextView) mealPopupView.findViewById(R.id.txtCookName);
        cookRating = (TextView) mealPopupView.findViewById(R.id.txtRequestStatus);

        btnRequest = (Button) mealPopupView.findViewById(R.id.btnRequestMeal);

        dialogBuilder.setView(mealPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mealName.setText(meal.mealName);
        mealType.setText(meal.mealType);
        mealCuisine.setText(meal.mealCuisine);
        mealAllergens.setText(meal.mealAllergens);
        mealPrice.setText(meal.mealPrice);
        mealDescription.setText(meal.mealDescription);
        Cook temp = new Cook();
        String ingredients = temp.ingredientsToString(meal.ingredients);

        cookName.setText(meal.getName());
        cookRating.setText(meal.cookRating + "/5");
        mealIngredients.setText(ingredients);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientUID = client.userID;
                String cookUID = meal.cookUID;
                Request request = new Request(meal.mealName, cookUID,clientUID, "pending", client.firstName + " "+client.lastName, meal.cookFirstName+" "+meal.cookLastName );
                String path=cookUID+" "+ client.userID+" "+meal.mealName;
                FirebaseDatabase.getInstance().getReference("Users").child(cookUID).child("Requests Received").child(path).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference("Users").child(clientUID).child("Requests Sent").child(path).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(SearchView.this, "Meal was successfully requested!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                 }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SearchView.this, "Meal already requested", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}