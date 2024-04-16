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
import java.util.Comparator;
import java.util.PriorityQueue;

//this generates this listings!
class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        return Integer.compare(item2.getOwner().userPoints, item1.getOwner().userPoints);
    }
}
public class Listing extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        Intent intent = getIntent();
        ArrayList<String> categories = intent.getStringArrayListExtra("CATEGORIES");
        String title = intent.getStringExtra("TITLE");

        TextView pageTypeTitle = findViewById(R.id.PageType);
        if (categories != null) {
            pageTypeTitle.setText(title);
        }

        Button BackFYPButton = findViewById(R.id.BackFYPButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListingAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        generateItems(categories);

        BackFYPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FYP.class));
            }
        });
    }

    private void generateItems(ArrayList<String> categories) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");

        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Item> allItems = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null && item.category != null && !item.category.isEmpty() && containsAny(item.category, categories) && item.getRentalStatus().rentalStatus != 1) {
                        allItems.add(item);
                    }
                }

                PriorityQueue<Item> priorityQueue = new PriorityQueue<>(new ItemComparator());
                priorityQueue.addAll(allItems);

                ArrayList<Item> sortedItems = new ArrayList<>(priorityQueue);
                runOnUiThread(() -> refreshRecyclerViewWithNewData(sortedItems));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean containsAny(ArrayList<String> list1, ArrayList<String> list2) {
        for (String item : list1) {
            if (list2.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public void refreshRecyclerViewWithNewData(ArrayList<Item> newData) {
        adapter.updateItems(newData);
    }
}