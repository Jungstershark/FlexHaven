package com.example.flexhaven.helpers;

public class User {
    public int userPoints;
    public String fullName, email, phoneNumber, password, userTier;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String fullName, String email, String phoneNumber, String password) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userPoints = 0;
        this.userTier = "";
    }

    private void computeTier(){
        if (this.userPoints<250){
            this.userTier = "Bronze";
        }
        else if ((this.userPoints>=250)&&(this.userPoints<750)){
            this.userTier = "Silver";
        }
        else if ((this.userPoints>=750)&&(this.userPoints<1500)){
            this.userTier = "Gold";
        }
        else{
            this.userTier = "Platinum";
        }
    }
}
