package com.example.flexhaven;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.ListingAdapter;
import com.example.flexhaven.helpers.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherProfile extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    private Drawable tierIcon;
    private User currentUser;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile);

        //dynamically set username and user points!
        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("OWNER_OBJECT");

        // Get profile picture using URL from currentUser user object
        String profileUrl = currentUser.imageUrl;
        ImageView profileImage = findViewById(R.id.otherProfilePictureImageView);
        if (!profileUrl.isEmpty()) { // Check if profileUrl is not empty
            Glide.with(this)
                    .load(profileUrl)
                    .centerCrop()
                    .placeholder(R.drawable.add_image_view)
                    .into(profileImage);
        } else {
            // Handle the case where profileUrl is empty
            // For example, load a default image or hide the ImageView
            profileImage.setImageResource(R.drawable.add_image_view);
        }
        // Get username from currentUser user object
        TextView usernameTextView = findViewById(R.id.otherNameTextView);
        if (currentUser.username!= null){
            usernameTextView.setText(currentUser.username);
        }

        // Get user Tier and user Points from currentUser user object
        TextView userTierPoints = findViewById(R.id.otherTierPointsTextView);
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
        TextView sellerTierPoints = findViewById(R.id.othersellerTierPointsTextView);
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

        // Get user email from currentUser user object
        TextView userEmail = findViewById(R.id.otherEmailTextView);
        if (currentUser!=null){
            userEmail.setText(currentUser.email);
        }


        recyclerView = findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        generateItems();


        ImageButton homeButton = findViewById(R.id.ProfileMenuFYPButton);
        ImageButton searchButton = findViewById(R.id.ProfileMenuSearchButton);
        ImageButton profileButton = findViewById(R.id.ProfileMenuProfileButton);

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
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
    }

    private void generateItems() {
        // Get the reference to the Firebase Realtime Database
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");

        // Add a listener to retrieve data from the "Items" node
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> userItems = new ArrayList<>();
                String currentUserEmail = currentUser.email;

                // Iterate through each item in the dataSnapshot
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Get the item object
                    Item item = itemSnapshot.getValue(Item.class);

                    // Check if the item owner's email matches the current user's email
                    // and if the rental status is not equal to 1 (1 represents rented)
                    if (item != null && item.getOwner().email.equals(currentUserEmail) && item.getRentalStatus().rentalStatus != 1) {
                        // Add the item to the list of user items
                        userItems.add(item);
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
}
