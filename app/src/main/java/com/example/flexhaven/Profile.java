package com.example.flexhaven;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.ListingAdapter;
import com.example.flexhaven.helpers.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.Current;

import java.util.ArrayList;

public class  Profile extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    private Drawable tierIcon;
    private int amountToAdd;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //dynamically set username and user points!
        User currentUser = CommonData.getInstance().getCurrentUser();

        // Get profile picture using URL from currentUser user object
        String profileUrl = currentUser.imageUrl;
        ImageView profileImage = findViewById(R.id.userProfilePictureImageView);
        if (!profileUrl.isEmpty()) {
            // Load image using Glide if profileUrl is not empty
            Glide.with(this)
                    .load(profileUrl)
                    .centerCrop()
                    .placeholder(R.drawable.add_image_view)
                    .into(profileImage);
        } else {
            // If profileUrl is empty, set the ImageView to display a placeholder image directly
            profileImage.setImageResource(R.drawable.add_image_view);
        }


        // Get username from currentUser user object
        TextView usernameTextView = findViewById(R.id.userNameTextView);
        if (currentUser.username!= null){
            usernameTextView.setText(currentUser.username);
        }

        // Get user Tier and user Points from currentUser user object
        TextView userTierPoints = findViewById(R.id.userTierPointsTextView);
        if (currentUser != null) {
            currentUser.computeTier();
            switch (currentUser.userTier) {
                case "Bronze":
                    tierIcon = getResources().getDrawable(R.drawable.tier_bronze_icon);
                    break;
                case "Silver":
                    tierIcon = getResources().getDrawable(R.drawable.tier_silver_icon);
                    break;
                case "Gold":
                    tierIcon = getResources().getDrawable(R.drawable.tier_gold_icon);
                    break;
                case "Platinum":
                    tierIcon = getResources().getDrawable(R.drawable.tier_platinum_icon);
                    break;
                default:
                    tierIcon = getResources().getDrawable(R.drawable.tier_bronze_icon);
            }
            userTierPoints.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, tierIcon, null);
        }
        int currentUserPoints = currentUser.userPoints;
        if (currentUser!=null){
            userTierPoints.setText("Buyer: " + currentUserPoints + " Points" + "  |  " + currentUser.userTier);
        }

        // Get seller Tier and seller Points from currentUser user object
        TextView sellerTierPoints = findViewById(R.id.sellerTierPointsTextView);
        if (currentUser != null) {
            currentUser.computeSellerTier();
            switch (currentUser.sellerTier) {
                case "Bronze":
                    tierIcon = getResources().getDrawable(R.drawable.tier_bronze_icon);
                    break;
                case "Silver":
                    tierIcon = getResources().getDrawable(R.drawable.tier_silver_icon);
                    break;
                case "Gold":
                    tierIcon = getResources().getDrawable(R.drawable.tier_gold_icon);
                    break;
                case "Platinum":
                    tierIcon = getResources().getDrawable(R.drawable.tier_platinum_icon);
                    break;
                default:
                    tierIcon = getResources().getDrawable(R.drawable.tier_bronze_icon);
            }
            sellerTierPoints.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, tierIcon, null);
        }
        int sellerUserPoints = currentUser.sellerPoints;
        if (currentUser!=null){
            sellerTierPoints.setText("Seller: " + sellerUserPoints + " Points" + "  |  " + currentUser.sellerTier);
        }

        TextView walletValueTextView = findViewById(R.id.WalletValue);
        walletValueTextView.setText("$" + currentUser.userWallet);


        // Get user email from currentUser user object
        TextView userEmail = findViewById(R.id.userEmailTextView);
        if (currentUser!=null){
            userEmail.setText(currentUser.email);
        }

        Button addWalletValue = findViewById(R.id.AddWalletValue);
        addWalletValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText amountToAddEditText = findViewById(R.id.AmountToAddInput);
                String amountToAddString = amountToAddEditText.getText().toString().trim();
                try {
                    int amountToAdd = Integer.parseInt(amountToAddString);
                    TextView walletValueTextView = findViewById(R.id.WalletValue);
                    int totalWalletValue = amountToAdd + currentUser.userWallet;
                    currentUser.userWallet = totalWalletValue;
                    walletValueTextView.setText("$" + currentUser.userWallet);

                    // Update Database
                    updateUserWallet(totalWalletValue, currentUser);

                    // Show a toast message indicating success
                    Toast.makeText(Profile.this, "Wallet value updated successfully!", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    showErrorMessage("Type a valid Integer Value");
                }
            }
        });

        Button addItemButton = findViewById(R.id.AddItemProfile);

        recyclerView = findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        generateItems(0);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Add.class));
            }
        });

        Button userListings = findViewById(R.id.userListings);
        Button userRentedOut = findViewById(R.id.userRentedOut);
        Button userRentals = findViewById(R.id.userRentals);

        userListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListings.setBackgroundColor(getResources().getColor(R.color.clicked_button_color));
                userRentedOut.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                userRentals.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                generateItems(0);
            }
        });

        userRentedOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListings.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                userRentedOut.setBackgroundColor(getResources().getColor(R.color.clicked_button_color));
                userRentals.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                generateItems(1);
            }
        });

        userRentals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userListings.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                userRentedOut.setBackgroundColor(getResources().getColor(R.color.default_button_color));
                userRentals.setBackgroundColor(getResources().getColor(R.color.clicked_button_color));
                generateItems(2);
            }
        });

        ImageButton homeButton = findViewById(R.id.ProfileMenuFYPButton);
        ImageButton searchButton = findViewById(R.id.ProfileMenuSearchButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FYP.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Search.class));
            }
        });

        Button logOutButton = findViewById(R.id.LogoutProfile);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.resetInstance();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    private void generateItems(int status) {
        // Get the reference to the Firebase Realtime Database
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");

        // Add a listener to retrieve data from the "Items" node
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> userItems = new ArrayList<>();
                String currentUserEmail = CommonData.getInstance().getCurrentUser().email;
                String currentUserKey = CommonData.getInstance().getCurrentUser().userKey;

                // Iterate through each item in the dataSnapshot
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Get the item object
                    Item item = itemSnapshot.getValue(Item.class);
                    String itemUserkey = item.getOwner().username + "_" + item.getOwner().email;

                    // Check if the item owner's email matches the current user's email
                    if (item != null) {
                        switch (status) {
                            case 0:
                                // Add the item to the list if the user is the owner and rental status is 0
                                if (itemUserkey.equals(currentUserKey) && item.getRentalStatus().rentalStatus==0) {
                                    userItems.add(item);
                                }
                                break;
                            case 1:
                                // Add the item to the list if the user is the owner and rental status is 1
                                if (itemUserkey.equals(currentUserKey) && item.getRentalStatus().rentalStatus == 1) {
                                    userItems.add(item);
                                }
                                break;
                            case 2:
                                // Add the item to the list if the rental status is 1 and renter email is the current user's email
                                if (item.getRentalStatus().rentalStatus== 1 && item.getRentalStatus().itemRenterEmail.equals(currentUserEmail)) {
                                    userItems.add(item);
                                }
                                break;
                        }
                    }
                }

                // Update the RecyclerView with the filtered list of user items
                runOnUiThread(() -> refreshRecyclerViewWithNewData(userItems));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }

    public void refreshRecyclerViewWithNewData(ArrayList<Item> newData) {
        adapter.updateItems(newData);
    }
    private void updateUserWallet(int totalWalletValue, User currentUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        Query query = userRef.orderByChild("userKey").equalTo(currentUser.userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        userRef.child(userSnapshot.getKey()).child("userWallet").setValue(totalWalletValue);
                        currentUser.userWallet = totalWalletValue;
                        Log.d("UpdateUserWallet", "User wallet updated successfully");
                    }
                } else {
                    Log.d("UpdateUserWallet", "User data not found in the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UpdateUserWallet", "Database error: " + databaseError.getMessage());
                Toast.makeText(Profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showErrorMessage(String message) {
        Toast.makeText(Profile.this, message, Toast.LENGTH_SHORT).show();
    }
}
