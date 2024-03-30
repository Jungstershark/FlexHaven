package com.example.flexhaven;

import androidx.appcompat.app.AppCompatActivity;
import com.example.flexhaven.helpers.Item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.flexhaven.helpers.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button addItemNextButton = findViewById(R.id.addItemFinished);

        //get EditText from layout
        TextInputLayout itemNameInputLayout = findViewById(R.id.ItemNameInput);
        TextInputLayout categoryInputLayout = findViewById(R.id.CategoryInput);
        TextInputLayout conditionLayout = findViewById(R.id.ConditionInput);
        TextInputLayout priceLayout = findViewById(R.id.PriceInput);
        TextInputLayout locationLayout = findViewById(R.id.LocationInput);
        TextInputLayout itemDescriptionLayout = findViewById(R.id.ItemDescriptionInput);

        EditText itemNameEditText = itemNameInputLayout.getEditText();
        EditText categoryEditText = categoryInputLayout.getEditText();
        EditText conditionEditText = conditionLayout.getEditText();
        EditText priceEditText = priceLayout.getEditText();
        EditText locationEditText = locationLayout.getEditText();
        EditText itemDescriptionEditText = itemDescriptionLayout.getEditText();

        addItemNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //when button clicked, get values
                String email = "jy";
                String itemName = itemNameEditText.getText().toString().trim();
                String category = categoryEditText.getText().toString().trim();
                String condition = conditionEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String location = locationEditText.getText().toString();
                String itemDescription = itemDescriptionEditText.getText().toString();

                //TODO 1 - To add Saved Instance of user's email, use it as the key to save it to the database
                //use private method to place values into firebase
                saveItemDetailsToFirebase(email, itemName, category, condition, price, location, itemDescription);
            }
        });
    }



    //method to handle new user input
    private void saveItemDetailsToFirebase(String email, String itemName, String category, String condition, String price, String location, String itemDescription){
        // TODO 2 - We could have a page if item description is empty
//        if (category.isEmpty() || condition.isEmpty() || price.isEmpty() || location.isEmpty() || itemDescription.isEmpty()) {
//            // if any field is empty, go to the signup error page
//            Intent newActivity = new Intent(getApplicationContext(), SignUpError.class);
//            startActivity(newActivity);
//            return;
//        }
        // get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Items");

        // create a unique ID for each user
        String userId = myRef.push().getKey();

        // create a user object or use a HashMap to organize user details
        Item item = new Item(email, itemName, category, condition, price, location, itemDescription);

        // save the user details under their unique ID
        if (userId != null) {
            myRef.child(userId).setValue(item).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //just put the user into the FYP, no need to login again!
                    Intent newActivity = new Intent(getApplicationContext(), Profile.class);
                    startActivity(newActivity);
                }
//                else {
//                    //go to sign up error page, because firebase isn't working.
//                    Intent newActivity = new Intent(getApplicationContext(), SignUpError.class);
//                    startActivity(newActivity);
//                }
            });
        }
    }
}