package com.example.flexhaven;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.User;

public class ItemDescription extends AppCompatActivity {
    private Item item;
    private User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        if (item.name!= null){
            productTitle.setText(item.name);
        }

        TextView rentalPrice = findViewById(R.id.rentalPriceTextView);
        if (item.price!= null){
            rentalPrice.setText("Rental Price: $" + item.price + "/day");
        }

        TextView Condition = findViewById(R.id.ConditionTextView);
        if (item.condition!= null){
            Condition.setText("Condition: " + item.condition);
        }

        TextView CategoriesText = findViewById(R.id.CategoriesTextView);
        if (item.category!= null){
            String categoriesString = item.category.toString();
            CategoriesText.setText("Categories: " + categoriesString);
        }


        TextView LocationText = findViewById(R.id.LocationTextView);
        if (item.location!= null){
            LocationText.setText("Location: " + item.location);
        }

        TextView Description = findViewById(R.id.DescriptionTextView);
        if (item.itemDescription!= null){
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
        if (owner.username!= null){
            ownerNameTextView.setText(owner.username);
        }
        // Get Tier from user object
        TextView ownerTierTextView = findViewById(R.id.sellerTierTextView);
        if (owner.userTier!= null){
            ownerTierTextView.setText(owner.userTier);
        }

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
}
