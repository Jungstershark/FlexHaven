package com.example.flexhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.ListingAdapter;

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

    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String[] categories = CommonData.getInstance().getCategories();

        Button searchButton = findViewById(R.id.searchItems);


        // RECYCLER VIEW FOR GENERATING SEARCH
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);


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
//                Intent newActivity = new Intent(getApplicationContext(),Listing.class);
//                startActivity(newActivity);
                generateItems();
            }
        });

        ImageButton homeButton = findViewById(R.id.SearchMenuFYPButton);
        ImageButton profileButton = findViewById(R.id.SearchMenuProfileButton);

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
    }

    private void generateItems() {
        ArrayList<String> categoriesToSearch = CommonData.getInstance().getCategoriesToSearch();
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> matchingItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null && item.category != null && !item.category.isEmpty()) {
                        for (String category : item.category) {
                            if (categoriesToSearch.contains(category)) {
                                matchingItems.add(item);
                                break;
                            }
                        }
                    }
                }
                runOnUiThread(() -> refreshRecyclerViewWithNewData(matchingItems));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void refreshRecyclerViewWithNewData(ArrayList<Item> newData) {
        adapter.updateItems(newData);
    }
}
