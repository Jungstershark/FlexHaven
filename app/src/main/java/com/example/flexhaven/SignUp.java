package com.example.flexhaven;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexhaven.helpers.User;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUp extends AppCompatActivity {
    EditText fullNameEditText, emailEditText, phoneNumberEditText, passwordEditText;
    private Uri imageUri;
    private boolean uploadImageStatus = false;
    public String UriString;
    public ImageView addProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signUpFinishedButton = findViewById(R.id.signUpFinished);
        Button loginBackButton = findViewById(R.id.loginBack);
        CheckBox tncCheckBox = findViewById(R.id.signUpTermsAndConditions);
        addProfileImage = findViewById(R.id.addProfilePictureButton);

        //get EditText from layout
        TextInputLayout usernameInputLayout = findViewById(R.id.usernameInput);
        TextInputLayout emailInputLayout = findViewById(R.id.emailInput);
        TextInputLayout phoneInputLayout = findViewById(R.id.phoneInput);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInput);
        EditText fullNameEditText = usernameInputLayout.getEditText();
        EditText emailEditText = emailInputLayout.getEditText();
        EditText phoneNumberEditText = phoneInputLayout.getEditText();
        EditText passwordEditText = passwordInputLayout.getEditText();

        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        signUpFinishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //you must check the checkbox
                if (!tncCheckBox.isChecked()) {
                    Toast.makeText(SignUp.this, "You must accept the terms and conditions to register.", Toast.LENGTH_LONG).show();
                    return;
                }
                //when button clicked, get values
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                //use private method to place values into firebase
                saveUserDetailsToFirebase(fullName, email, phoneNumber, password);
            }
        });

        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), Login.class);
                startActivity(newActivity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            uploadImageStatus = true;

            imageUri = data.getData();
            addProfileImage.setImageURI(imageUri);
        }
    }

    //TODO low priority! use Firebase authentication instead to check for valid email account
    //there can be more than 1 username (e.g. Discord) but only can have 1 unique email.
    private void saveUserDetailsToFirebase(String fullName, String email, String phoneNumber, String password) {
        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            //some field empty!
            showErrorMessage("Please fill in all fields");
        } else {
            // get a reference to the Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Users");

            //check if email already exists! because email should be unique
            myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // email exists, display error message!
                        showErrorMessage("Email already in use!");
                    } else {
                        // If an image is uploaded, save it to Firebase Storage
                        if (uploadImageStatus && imageUri != null) {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    registerNewUser(fullName, email, phoneNumber, password, imageUrl, myRef);
                                });
                            }).addOnFailureListener(e -> {
                                // Handle error in uploading image
                                showErrorMessage("Error uploading image. Please try again.");
                            });
                        } else {
                            // If no image is uploaded, register the user without image URL
                            registerNewUser(fullName, email, phoneNumber, password, "", myRef);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    showErrorMessage("Error checking email. Please try again.");
                }
            });
        }
    }

    private void registerNewUser(String username, String email, String phoneNumber, String password, String imageUrl, DatabaseReference myRef) {
        String userId = myRef.push().getKey();
        User user = new User(username, email, phoneNumber, password, imageUrl);
        if (userId != null) {
            myRef.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent newActivity = new Intent(getApplicationContext(), Login.class);
                    startActivity(newActivity);
                } else {
                    // something happened? show error message
                    showErrorMessage("Registration failed. Please try again.");
                }
            });
        }
    }
    private void showErrorMessage(String message){
        Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
