package com.example.seg_project_app;

import junit.framework.TestCase;

import java.util.ArrayList;

public class CookProfileActivityTest extends TestCase {




    public void testStringToArrayList() {

        String testString = "cookies";
        ArrayList<String> correct = new ArrayList<String>();
        correct.add("cookies");

        Cook cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "false", "false","");

        ArrayList<String> newArray = cook.stringToArrayList(testString);
        boolean test = newArray.equals(correct);

        assertTrue(test);
    }

    public void testIngredientsToString() {

       String correct = "cookies, milk";
       Cook cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "false", "false","");

        ArrayList<String> testArray = new ArrayList<String>();
        testArray.add("cookies");
        testArray.add("milk");
        testArray.add("eggs");
       String attempt = cook.ingredientsToString(testArray);
        System.out.println(attempt);
        System.out.println(correct);
       boolean test = attempt.equals(cook);

       assertFalse(test);

    }
    public void testRequirements(){
        ArrayList<String> testArray = new ArrayList<String>();
        testArray.add("cookies");
        testArray.add("milk");
        testArray.add("eggs");
        //Cook cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "false", "false","");
        Meal meal = new Meal("food1", "Type", "Random", "allergen1", "food is good", "twenty", testArray);
        boolean test = meal.requirementsForInput("food", "Type", "Random", "allergen", "food is good", "20", testArray);
        assertTrue(test);
    }

    public  void testHasNumeric(){
        ArrayList<String> testArray = new ArrayList<String>();
        testArray.add("cookies");
        testArray.add("milk");
        testArray.add("eggs");
        String intStr = "kev123";
        Meal meal = new Meal("food1", "Type", "Random", "allergen1", "food is good", "twenty", testArray);

        boolean test = meal.hasNumeric(intStr);
        assertTrue(test);
    }

}