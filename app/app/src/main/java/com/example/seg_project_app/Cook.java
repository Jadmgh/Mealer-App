package com.example.seg_project_app;

public class Cook extends User {
    public String firstName, lastName,email, password, address, description;

    public Cook(String firstName, String lastName, String email, String password, String address, String description) {
        super(email, password, "cook");
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.description = description;



    }




}
//TODO: class cook
//TODO: figure out image for cheque