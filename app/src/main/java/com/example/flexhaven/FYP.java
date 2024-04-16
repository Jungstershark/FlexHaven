package com.example.flexhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FYP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fyp);

        ImageButton featuredButton = findViewById(R.id.featuredButton);
        featuredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the top 10 items from the database
                DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");
                Query query = itemsRef.orderByKey().limitToFirst(10);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Item> itemList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Item item = snapshot.getValue(Item.class);
                            if (item != null && item.getRentalStatus().rentalStatus == 0) {
                                itemList.add(item);
                            }
                        }

                        if (!itemList.isEmpty()) {
                            // Get the first item from the filtered list
                            Item featuredItem = itemList.get(0);
                            // Pass the user object and the retrieved item to the ItemDescription activity
                            Intent intent = new Intent(getApplicationContext(), ItemDescription.class);
                            intent.putExtra("ITEM_OBJECT", featuredItem);
                            startActivity(intent);
                        } else {
                            Toast.makeText(FYP.this, "No featured items found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(FYP.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageButton womanButton = findViewById(R.id.womenButton);
        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> categories = new ArrayList<>();
                categories.add("Woman");

                Intent newActivity = new Intent(getApplicationContext(), Listing.class);
                newActivity.putStringArrayListExtra("CATEGORIES", categories);
                newActivity.putExtra("TITLE", "Woman");
                startActivity(newActivity);
            }
        });

        ImageButton menButton = findViewById(R.id.menButton);
        menButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> categories = new ArrayList<>();
                categories.add("Men");

                Intent newActivity = new Intent(getApplicationContext(), Listing.class);
                newActivity.putStringArrayListExtra("CATEGORIES", categories);
                newActivity.putExtra("TITLE", "Men");
                startActivity(newActivity);
            }
        });

        ImageButton accessoriesButton = findViewById(R.id.accessoriesButton);
        accessoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> categories = new ArrayList<>();
                categories.add("Jeans");
                categories.add("Shorts");
                categories.add("T-Shirt");
                categories.add("Electronics");

                Intent newActivity = new Intent(getApplicationContext(), Listing.class);
                newActivity.putStringArrayListExtra("CATEGORIES", categories);
                newActivity.putExtra("TITLE", "Accessories");
                startActivity(newActivity);
            }
        });




        ImageButton profileButton = findViewById(R.id.profile);
        ImageButton searchButton = findViewById(R.id.search);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(),Profile.class);
                startActivity(newActivity);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(),Search.class);
                startActivity(newActivity);
            }
        });
    }
}