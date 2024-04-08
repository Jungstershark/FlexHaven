package com.example.flexhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;
import com.example.flexhaven.helpers.ListingAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//this generates this listings!
public class Listing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        // Retrieve the category argument from the previous activity
        Intent intent = getIntent();
        String category = intent.getStringExtra("CATEGORY");

        TextView pageTypeTitle = findViewById(R.id.PageType);
        if (category!=null){
            pageTypeTitle.setText(category);
        }

        Button BackFYPButton = findViewById(R.id.BackFYPButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        generateItems(category);

        BackFYPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FYP.class));
            }
        });
    }

    private void generateItems(String category) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> matchingItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null && item.category != null && !item.category.isEmpty() && item.category.contains(category)) {
                        matchingItems.add(item);
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
