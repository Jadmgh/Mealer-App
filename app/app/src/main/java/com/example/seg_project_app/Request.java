package com.example.seg_project_app;

public class Request {

    public String cookUID;
    public String clientUID;
    public String cookName;
    public String clientName;
    public String status;
    public String mealName;
    public String ratingGiven;

    public Request(String mealName, String cookUID, String clientUID, String status,String clientName,String cookName){
        this.mealName = mealName;
        this.cookUID = cookUID;
        this.clientUID = clientUID;
        this.cookName= cookName;
        this.clientName = clientName;
        this.status = status;
        this.ratingGiven = "";
    }

    public void acceptRequest(){
        status = "accepted";
    }

    public void declineRequest(){
        status = "declined";
    }

    public boolean isRequestAccepted(){
        if(status == "accepted"){
            return true;

        }
        else{
            return false;
        }
    }

    public boolean isRequestDeclined(){
        if(status == "declined"){
            return true;
        }
        else{
            return false;
        }
    }

    public void rateCook(String ratingGiven){
        this.ratingGiven = ratingGiven;
    }


}
