package com.example.seg_project_app;

public class Client extends User {

    public String firstName, lastName, address;
    public String creditCard, CCV;

    public Client(String firstName, String lastName, String email, String password, String address, String creditCard, String CCV){
        super(email,password,"client");

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.creditCard = creditCard;
        this.CCV = CCV;

    }
}
