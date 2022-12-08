//package com.example.seg_project_app;
//
//import android.graphics.drawable.Drawable;
//import android.media.Image;
//import android.net.Uri;
//import android.widget.ImageView;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class Cook extends User {
//
//
//    public String firstName, lastName,email, password, address, description;
//    public Uri chequeUri;
//    private Boolean banned, tempBanned;
//    private Date unbanDate;
//
//    public Cook(String firstName, String lastName, String email, String password, String address, String description) {
//        super(email, password, "cook");
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.password = password;
//        this.email = email;
//        this.description = description;
//        this.address = address;
//    }
//
////    public void setChequeUri(Uri uri) {
////        chequeUri = uri;
////    }
//
//
//    public Boolean isPermanentlyBanned(){
//        return banned;
//    }
//
//    public boolean isTempBanned(){
//        return tempBanned;
//    }
//
//    public String getUnbanDateAsString(){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        String date = dateFormat.format(unbanDate);
//        return date;
//    }
//
//    public void banCook(){
//        banned = true;
//    }
//
//    public void tempBanCook(int numberOfDays){
//        tempBanned=true;
//        Calendar c = Calendar.getInstance();
//        c.setTime(Calendar.getInstance().getTime());
//        c.add(Calendar.DATE,numberOfDays);
//        unbanDate = c.getTime();    //gets date after numberOfDays date //TODO: test calendar +
//    }
//
//    public void signIn(){
//        //TODO: move sign In to cook, when sign in, check if unbanned
////        CookProfileActivity newActivity = new CookProfileActivity(this);
//    }
//}

package com.example.seg_project_app;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Cook extends User {

    public String firstName, lastName, address, description, permanentlyBanned, tempBanned, unbanDate, rating, nbOfReviews;

    public Cook(){
    }
    public Cook(String firstName, String lastName, String email, String password, String address, String description, String userID, String permanentlyBanned, String tempBanned, String unbanDate){
        super(email,password,"cook", userID);

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.description = description;

        this.permanentlyBanned = permanentlyBanned;
        this.tempBanned = tempBanned;
        this.unbanDate = unbanDate;
        rating = "";
        nbOfReviews ="";
    }

    public Boolean isPermanentlyBanned(){
        if (permanentlyBanned.equals( "true")){
            return true;
        }
        return false;
    }

    public boolean isTempBanned(){
        if (tempBanned.equals("true")){
            return  true;
        }
        return false;
    }

    public String getUnbanDateAsString(){

        return unbanDate;
    }

    public Date getUnbanDateAsDate(){
        Date date = null;
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
            date = formatter2.parse(unbanDate);
        }
        catch (Exception e){
        }
        return date;
    }


    public boolean pastUnbanDate(){
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        if (getUnbanDateAsDate().after(currentDate)){
            return true;
        }
        return false;
    }

    public void tempBan(int numberOfDays){
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        c.add(Calendar.DATE,numberOfDays);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        unbanDate= dateFormat.format(c.getTime());
        tempBanned = "true";
    }
    public void unbanCook() {
        tempBanned= "false";
        permanentlyBanned = "false";
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

    public void permanentlyBanCook(){
        permanentlyBanned = "true";
    }

    public boolean isPermanentlyAndTemporarilyBanned(){
        if (permanentlyBanned == "true"&& tempBanned == "true"){
            return true;
        }
        return false;
    }

}
