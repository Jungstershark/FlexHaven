package com.example.flexhaven.helpers;

import java.io.Serializable;
import java.math.BigDecimal;

public class User implements Serializable {
    public int userPoints, sellerPoints;
    public BigDecimal userWallet;
    public String username, email, phoneNumber, password, userTier, imageUrl, sellerTier;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email, String phoneNumber, String password, String imageUrl) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userPoints = 0;
        this.userTier = " ";
        this.sellerPoints = 0;
        this.sellerTier = " ";
        this.imageUrl = imageUrl;
        this.userWallet = new BigDecimal(0);
    }

    public void computeTier(){
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
