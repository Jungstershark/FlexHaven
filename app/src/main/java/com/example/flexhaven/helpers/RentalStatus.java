package com.example.flexhaven.helpers;

import java.io.Serializable;

public class RentalStatus implements Serializable {
    public int rentalStatus;
    public String itemRenterUsername, itemRenterEmail;
    // Default constructor is needed for Firebase
    public RentalStatus() {}

    // Constructor
    public RentalStatus(String itemRenterUsername, String itemRenterEmail, int rentalStatus){
        this.rentalStatus = rentalStatus;
        this.itemRenterUsername = itemRenterUsername;
        this.itemRenterEmail = itemRenterEmail;

    }
}
