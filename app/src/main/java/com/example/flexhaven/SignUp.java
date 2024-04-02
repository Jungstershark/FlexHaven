package com.example.flexhaven;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexhaven.helpers.User;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText fullNameEditText, emailEditText, phoneNumberEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signUpFinishedButton = findViewById(R.id.signUpFinished);
        Button loginBackButton = findViewById(R.id.loginBack);
        CheckBox tncCheckBox = findViewById(R.id.signUpTermsAndConditions);

        //get EditText from layout
        TextInputLayout usernameInputLayout = findViewById(R.id.usernameInput);
        TextInputLayout emailInputLayout = findViewById(R.id.emailInput);
        TextInputLayout phoneInputLayout = findViewById(R.id.phoneInput);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInput);
        EditText fullNameEditText = usernameInputLayout.getEditText();
        EditText emailEditText = emailInputLayout.getEditText();
        EditText phoneNumberEditText = phoneInputLayout.getEditText();
        EditText passwordEditText = passwordInputLayout.getEditText();

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

    //TODO low priority! use Firebase authentication instead to check for valid email account
    //there can be more than 1 username (e.g. Discord) but only can have 1 unique email.
    private void saveUserDetailsToFirebase(String fullName, String email, String phoneNumber, String password){
        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            //some field empty!
            showErrorMessage("Please fill in all fields");
        }
        else{
            // get a reference to the Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Users");
            //check if email already exists! because email should be uniquex
            myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // email exists, display error message!
                        showErrorMessage("Email already in use!");
                    } else {
                        registerNewUser(fullName, email, phoneNumber, password, myRef);
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
    private void registerNewUser(String username, String email, String phoneNumber, String password, DatabaseReference myRef){
        String userId = myRef.push().getKey();
        User user = new User(username, email, phoneNumber, password);
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
}
