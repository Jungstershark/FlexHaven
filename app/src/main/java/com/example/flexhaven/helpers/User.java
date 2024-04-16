package com.example.flexhaven.helpers;

import java.io.Serializable;
import java.math.BigDecimal;

public class User implements Serializable {
    public int userPoints, sellerPoints, userWallet;
    public String userKey, username, email, phoneNumber, password, userTier, imageUrl, sellerTier;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email, String phoneNumber, String password, String imageUrl) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userPoints = 20;
        this.userTier = "";
        this.sellerPoints = 20;
        this.sellerTier = "";
        this.imageUrl = imageUrl;
        this.userWallet = 0;
        this.userKey = username + "_" + email;
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
    public void computeSellerTier(){
        if (this.sellerPoints<250){
            this.sellerTier = "Bronze";
        }
        else if ((this.sellerPoints>=250)&&(this.sellerPoints<750)){
            this.sellerTier = "Silver";
        }
        else if ((this.sellerPoints>=750)&&(this.sellerPoints<1500)){
            this.sellerTier = "Gold";
        }
        else{
            this.sellerTier = "Platinum";
        }
    }
}
