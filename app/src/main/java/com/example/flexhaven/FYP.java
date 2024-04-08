package com.example.flexhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FYP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fyp);

        ImageButton featuredButton = findViewById(R.id.featuredButton);
        featuredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(),ItemDescription.class);
                startActivity(newActivity);
            }
        });

        ImageButton womanButton = findViewById(R.id.womenButton);
        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Woman";

                Intent newActivity = new Intent(getApplicationContext(),Listing.class);
                newActivity.putExtra("CATEGORY",category);
                startActivity(newActivity);
            }
        });

        ImageButton menButton = findViewById(R.id.menButton);
        menButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Shorts";

                Intent newActivity = new Intent(getApplicationContext(),Listing.class);
                newActivity.putExtra("CATEGORY",category);
                startActivity(newActivity);
            }
        });
        ImageButton accessoriesButton = findViewById(R.id.accessoriesButton);
        accessoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Jeans";

                Intent newActivity = new Intent(getApplicationContext(),Listing.class);
                newActivity.putExtra("CATEGORY",category);
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