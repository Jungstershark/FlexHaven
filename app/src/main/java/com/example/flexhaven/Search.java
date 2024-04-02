package com.example.flexhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.User;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String[] categories = CommonData.getInstance().getCategories();

        Button searchButton = findViewById(R.id.searchItems);

        // Keep track of what categories have been selected (no duplicates :(( )
        ArrayList<String> selectedCategoriesList = new ArrayList<>();
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.searchCategoryAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(adapter);

        ChipGroup chipGroup = findViewById(R.id.searchCategoryChipGroup);
        autoCompleteTextView.setFocusable(false);
        autoCompleteTextView.setFocusableInTouchMode(false);
        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = adapter.getItem(position);
            if (!selectedCategoriesList.contains(selectedCategory)) {
                selectedCategoriesList.add(selectedCategory);
                Chip chip = new Chip(Search.this);
                chip.setText(selectedCategory);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    selectedCategoriesList.remove(selectedCategory);
                    chipGroup.removeView(chip);
                });
                chipGroup.addView(chip);
            }
            autoCompleteTextView.setText("");
            autoCompleteTextView.clearFocus();
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.getInstance().CategoriesToSearch(selectedCategoriesList);
                Intent newActivity = new Intent(getApplicationContext(),Listing.class);
                startActivity(newActivity);
            }
        });
    }
}
