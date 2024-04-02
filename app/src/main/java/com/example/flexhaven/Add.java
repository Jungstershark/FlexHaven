package com.example.flexhaven;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.Item;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Add extends AppCompatActivity {
    private ImageView imageView;
    private Uri imageUri;
    private boolean uploadImageStatus = false;

    public String UriString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        Button addItemNextButton = findViewById(R.id.addItemFinished);
        imageView = findViewById(R.id.addImageButton);

        //all the preset categories
        String[] categories = CommonData.getInstance().getCategories();

        //get EditText from layout
        TextInputLayout itemNameInputLayout = findViewById(R.id.ItemNameInput);
        TextInputLayout conditionLayout = findViewById(R.id.ConditionInput);
        TextInputLayout priceLayout = findViewById(R.id.PriceInput);
        TextInputLayout locationLayout = findViewById(R.id.LocationInput);
        TextInputLayout itemDescriptionLayout = findViewById(R.id.ItemDescriptionInput);

        EditText itemNameEditText = itemNameInputLayout.getEditText();
        EditText conditionEditText = conditionLayout.getEditText();
        EditText priceEditText = priceLayout.getEditText();
        EditText locationEditText = locationLayout.getEditText();
        EditText itemDescriptionEditText = itemDescriptionLayout.getEditText();

        //to keep track of what categories has been selected (no double selecting >:(( )
        ArrayList<String> selectedCategoriesList = new ArrayList<>();
        //using a chip group for categories
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.categoryAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteTextView.setAdapter(adapter);
        ChipGroup chipGroup = findViewById(R.id.categoryChipGroup);
        autoCompleteTextView.setFocusable(false);
        autoCompleteTextView.setFocusableInTouchMode(false);
        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = adapter.getItem(position);
            if (!selectedCategoriesList.contains(selectedCategory)) {
                selectedCategoriesList.add(selectedCategory);
                Chip chip = new Chip(Add.this);
                chip.setText(selectedCategory);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
                    chipGroup.addView(chip);
            }
            autoCompleteTextView.setText("");
            autoCompleteTextView.clearFocus();




        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
          }
        });


        addItemNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when button clicked, get values
                String email = CommonData.getInstance().getEmail();
                String itemName = itemNameEditText.getText().toString().trim();
                String condition = conditionEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String location = locationEditText.getText().toString();
                String itemDescription = itemDescriptionEditText.getText().toString();
                //use private method to place values into firebase
                saveItemDetailsToFirebase(email, itemName, selectedCategoriesList, condition, price, location, itemDescription, imageUri);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            uploadImageStatus = true;

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }


    //method to handle new user input
    private void saveItemDetailsToFirebase(String email, String itemName, ArrayList<String> category, String condition, String price, String location, String itemDescription, Uri imageUri){


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

        StorageReference storage = FirebaseStorage.getInstance().getReference();

        // create a unique ID for each user
        String userId = myRef.push().getKey();

        if (uploadImageStatus && imageUri!=null){
            final StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UriString = uri.toString();
                            Item item = new Item(email, itemName, category, condition, price, location, itemDescription, UriString);

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
                    });
                }
            });
        }
        else {
            Item item = new Item(email, itemName, category, condition, price, location, itemDescription, "");

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

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

        }
