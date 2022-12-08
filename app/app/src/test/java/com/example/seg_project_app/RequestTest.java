package com.example.seg_project_app;

import junit.framework.TestCase;

import java.util.ArrayList;

public class RequestTest extends TestCase {
    public void testIsRequestAccepted() {
        Request request = new Request("Mac and Cheese", "cookUID", "Client UID", "Declined", "Kevon", "Bhavuk");

        request.acceptRequest();
        boolean test = request.isRequestAccepted();

        assertTrue(test);

    }

    public void testIsRequestDeclined() {
        Request request = new Request("Mac and Cheese", "cookUID", "Client UID", "Acceoted", "Kevon", "Bhavuk");
        request.declineRequest();
        boolean test = request.isRequestDeclined();
        assertTrue(test);






    }
    public  void testRequirements(){
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("ingr1");
        ingredients.add("ingrd2");
        Meal meal = new Meal("Mac and cheese", "Dinner", "Italian", "allergens","Description", "20.60", ingredients, "Kevon", "Nav", "6.0","cookUID");
        boolean bool = meal.requirementsForInput("Mac and cheese", "Dinner", "Italian", "allergens","Description", "20.60", ingredients);

        assertFalse(bool);
    }

    public  void testRequirements2(){
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("ingr");
        ingredients.add("ingrdtwo");
        Meal meal = new Meal("Mac and cheese", "Dinner", "Italian", "allergens","Description", "20.60", ingredients, "Kevon", "Nav", "6.0","cookUID");
        boolean bool = meal.requirementsForInput("Mac and cheese", "Dinner", "Italian", "allergens","Description", "20.60", ingredients);

        assertTrue(bool);
    }

}