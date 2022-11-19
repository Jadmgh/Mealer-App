package com.example.seg_project_app;

import java.util.ArrayList;

public class Meal {
    public String mealName, mealType,mealCuisine,mealAllergens,mealDescription,mealPrice;
    public ArrayList<String> ingredients;

    public Meal(String mealName, String mealType, String mealCuisine, String mealAllergens,
                String mealDescription,String mealPrice, ArrayList<String> ingredients){
        this.ingredients= ingredients;
        this.mealAllergens = mealAllergens;
        this.mealPrice = mealPrice;
        this.mealCuisine = mealCuisine;
        this.mealType = mealType;
        this.mealDescription = mealDescription;
        this.mealName = mealName;
    }
}
