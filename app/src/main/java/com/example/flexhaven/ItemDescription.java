package com.example.flexhaven;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.RentalStatus;
import com.example.flexhaven.helpers.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;

public class ItemDescription extends AppCompatActivity {
    private Item item;
    private User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ItemDescription", "ItemDescription Page just initialised");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdescription);

        // Retrieve the Item object from the Intent
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("ITEM_OBJECT");

        // Access the properties of the Item object and display them in your layout
        String productImageUrl = item.imageUrl;
        ImageView productImage = findViewById(R.id.productImageView);
        if (!TextUtils.isEmpty(productImageUrl)) {
            // Load image using Glide if productImageUrl is not empty
            Glide.with(this)
                    .load(productImageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.add_image_view)
                    .into(productImage);
        } else {
            // If productImageUrl is empty, set the ImageView to display a placeholder image directly
            productImage.setImageResource(R.drawable.add_image_view);
        }

        TextView productTitle = findViewById(R.id.productTitleTextView);
        if (item.name != null) {
            productTitle.setText(item.name);
        }

        TextView rentalPrice = findViewById(R.id.rentalPriceTextView);
        if (item.price != null) {
            rentalPrice.setText("Rental Price: $" + item.price + "/day");
        }

        TextView Condition = findViewById(R.id.ConditionTextView);
        if (item.condition != null) {
            Condition.setText("Condition: " + item.condition);
        }

        TextView CategoriesText = findViewById(R.id.CategoriesTextView);
        if (item.category != null) {
            String categoriesString = item.category.toString();
            CategoriesText.setText("Categories: " + categoriesString);
        }


        TextView LocationText = findViewById(R.id.LocationTextView);
        if (item.location != null) {
            LocationText.setText("Location: " + item.location);
        }

        TextView Description = findViewById(R.id.DescriptionTextView);
        if (item.itemDescription != null) {
            Description.setText("Description: " + item.itemDescription);
        }

        // Generate Owner Details
        owner = item.getOwner();
        // Get profile picture using URL from currentUser user object
        String ownerImageUrl = owner.imageUrl;
        ImageView ownerImage = findViewById(R.id.sellerImageView);
        if (!ownerImageUrl.isEmpty()) {
            // Load image using Glide if ownerImageUrl is not empty
            Glide.with(this)
                    .load(ownerImageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.add_image_view)
                    .into(ownerImage);
        } else {
            // If ownerImageUrl is empty, set the ImageView to display a placeholder image directly
            ownerImage.setImageResource(R.drawable.add_image_view);
        }

        // Get username from user object
        TextView ownerNameTextView = findViewById(R.id.sellerNameTextView);
        if (owner.username != null) {
            ownerNameTextView.setText(owner.username);
        }
        // Get Tier from user object
        TextView ownerTierTextView = findViewById(R.id.sellerTierTextView);
        if (owner.userTier != null) {
            ownerTierTextView.setText("Seller: " + owner.userTier);
        }

        if (owner != null) {
            owner.computeSellerTier();
            Drawable tierIcon;
            switch (owner.sellerTier) {
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
            ownerTierTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(tierIcon, null, null, null);
        }


        Button RentNowButton = findViewById(R.id.rentNowButton);
        RentNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ItemDescription", "Rent button clicked");
                // Check if the current user is not the owner of the item
                User currentUser = CommonData.getInstance().getCurrentUser();
                if (currentUser != null && !currentUser.email.equals(item.userEmail)) {
                    Log.d("ItemDescription", "Current user is not the owner of the item");

                    if (currentUser.userWallet >= Integer.parseInt(item.price)) {

                        if (item.getRentalStatus().rentalStatus == 0) {
                            // Sufficient balance, proceed with renting
                            DatabaseReference itemRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");
                            Query query = itemRef.orderByChild("itemKey").equalTo(item.itemKey);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Log.d("ItemDescription", "Item found in the database");

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String itemId = snapshot.getKey();
                                            RentalStatus rentalStatus = new RentalStatus(currentUser.username, currentUser.email, 1);
                                            updateRentalStatus(itemId, rentalStatus, currentUser);
                                        }
                                    } else {
                                        Log.d("ItemDescription", "Item not found in the database");
                                        Toast.makeText(ItemDescription.this, "Item not found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("ItemDescription", "Database error: " + databaseError.getMessage());
                                    Toast.makeText(ItemDescription.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Item already rented out
                            showErrorMessage("Item has already been rented out");
                        }
                    } else {
                        // Insufficient balance, show error message
                        showErrorMessage("Insufficient balance in the wallet");
                    }
                } else {
                    Log.d("ItemDescription", "Current user is the owner of the item");
                    Toast.makeText(ItemDescription.this, "You cannot rent your own item", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Button ViewProfileButton = findViewById(R.id.ViewProfileButton);
        ViewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User ownerObject = item.getOwner();
                User currentUser = CommonData.getInstance().getCurrentUser();

                if (ownerObject != null && currentUser != null && ownerObject.email.equals(currentUser.email) && ownerObject.username.equals(currentUser.username)) {
                    // If ownerObject is the same as the currently logged-in user, go to the Profile.class
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                } else {
                    // Otherwise, go to the OtherProfile.class
                    Intent intent = new Intent(getApplicationContext(), OtherProfile.class);
                    intent.putExtra("OWNER_OBJECT", ownerObject);
                    startActivity(intent);
                }
            }
        });


        ImageButton homeButton = findViewById(R.id.ItemDescriptionFYPButton);
        Button backButton = findViewById(R.id.ItemDescriptionToListings);
        ImageButton searchButton = findViewById(R.id.ItemDescriptionSearchButton);
        ImageButton profileButton = findViewById(R.id.ItemDescriptionProfileButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FYP.class));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Search.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void updateRentalStatus(String itemId, RentalStatus rentalStatus, User currentUser) {
        DatabaseReference itemRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");
        DatabaseReference rentRef = itemRef.child(itemId).child("rentalStatus");

        rentRef.setValue(rentalStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("ItemDescription", "Item rented successfully");
                    Toast.makeText(ItemDescription.this, "Item rented successfully", Toast.LENGTH_SHORT).show();
                    updateUser(currentUser);
                } else {
                    Log.e("ItemDescription", "Error renting item: " + task.getException().getMessage());
                    Toast.makeText(ItemDescription.this, "Error renting item. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser(User currentUser) {
        Log.d("ItemDescription", "Updating user");
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        Query query = userRef.orderByChild("userKey").equalTo(currentUser.userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("ItemDescription", "User data found in the database");
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            int userPoints = user.userPoints + Integer.parseInt(item.price);
                            int userWallet = user.userWallet - Integer.parseInt(item.price);
                            if (userWallet < 0){
                                showErrorMessage("Insufficient balance in the wallet");
                            }
                            else {
                                currentUser.userPoints = userPoints;
                                currentUser.userWallet = userWallet;
                                userRef.child(userSnapshot.getKey()).child("userPoints").setValue(userPoints);
                                userRef.child(userSnapshot.getKey()).child("userWallet").setValue(userWallet);
                                Log.d("ItemDescription", "User points and wallet updated successfully");

                                // Update tier as well
                                user.userPoints = userPoints;
                                user.computeTier();
                                userRef.child(userSnapshot.getKey()).child("userTier").setValue(user.userTier);
                                updateSeller(item);

                            }
                        }
                    }
                } else{Log.d("ItemDescription", "User data not found in the database");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDescription", "Database error: " + databaseError.getMessage());
                Toast.makeText(ItemDescription.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSeller(Item item) {
        Log.d("ItemDescription", "Updating seller");
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        Query query = userRef.orderByChild("userKey").equalTo(item.userUsername + "_" + item.userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("ItemDescription", "Seller data found in the database");
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User seller = userSnapshot.getValue(User.class);
                        if (seller != null) {
                            int sellerPoints = seller.sellerPoints + Integer.parseInt(item.price);
                            int sellerWallet = seller.userWallet + Integer.parseInt(item.price);
                            userRef.child(userSnapshot.getKey()).child("sellerPoints").setValue(sellerPoints);
                            userRef.child(userSnapshot.getKey()).child("userWallet").setValue(sellerWallet);
                            Log.d("ItemDescription", "Seller points and wallet updated successfully");

                            // Update tier as well
                            seller.sellerPoints = sellerPoints;
                            seller.computeSellerTier();
                            userRef.child(userSnapshot.getKey()).child("sellerTier").setValue(seller.sellerTier);
                        }
                    }
                } else {Log.d("ItemDescription", "Seller data not found in the database");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDescription", "Database error: " + databaseError.getMessage());
                Toast.makeText(ItemDescription.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showErrorMessage(String message) {
        Toast.makeText(ItemDescription.this, message, Toast.LENGTH_SHORT).show();
    }
}