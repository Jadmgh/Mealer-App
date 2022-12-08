package com.example.seg_project_app;

import junit.framework.TestCase;

public class CookTest extends TestCase {


    public Cook cook;

    public void testTypeOfNewCook(){
        cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "false", "tempBanned","");
        boolean test = cook.type.equals("cook");
        assertTrue(test);

    }

    public void testPermanentlyBanCook(){
        cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "false", "false","");

        cook.permanentlyBanCook();
        assertTrue(cook.isPermanentlyBanned().equals(true));
    }

    public void testUnbanCook() {
        cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "true", "false","");
        cook.unbanCook();
        assertTrue(cook.isPermanentlyBanned() == false);
    }

    public void testIsPermanentlyAndTemporarilyBanned(){
        cook  = new Cook("kevin", "naveen", "kevinnaveen@gmail.com", "123123", "123 avenue", "My name is kevin", "userID", "true", "true","");
        assertTrue(cook.isPermanentlyAndTemporarilyBanned());
    }



}