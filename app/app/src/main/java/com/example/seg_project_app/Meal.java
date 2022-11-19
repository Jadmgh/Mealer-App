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
    public boolean requirementsForInput(String mealName, String mealType, String mealCuisine, String mealAllergens,
                                        String mealDescription,String mealPrice, ArrayList<String> ingredients )
    {

        if(mealName.isEmpty()){

            return false;
        }
        else if(this.hasNumeric(mealName)){

            return false;
        }

        if(mealType.isEmpty()){

            return false;
        }
        else if(this.hasNumeric(mealType)){

            return false;
        }

        if(mealCuisine.isEmpty()){

            return false;

        }
        else if(this.hasNumeric(mealCuisine)){

            return false;
        }


        if(mealAllergens.isEmpty()){

            return false;
        }
        else if(this.hasNumeric(mealAllergens)){

            return false;
        }

        if(mealDescription.isEmpty()){

            return false;
        }
        else if(this.hasNumeric(mealDescription)){

            return false;
        }

        if(mealPrice.isEmpty()){

            return false;
        }
        if(this.isNumeric(mealPrice) == false){

            return false;
        }

        if (ingredients.size()==0){

            return false;
        }
        for(int i = 0; i < ingredients.size(); i++){
            if(this.hasNumeric(ingredients.get(i))){

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
