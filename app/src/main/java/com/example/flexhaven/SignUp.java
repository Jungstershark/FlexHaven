package com.example.flexhaven;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexhaven.helpers.User;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText fullNameEditText, emailEditText, phoneNumberEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signUpFinishedButton = findViewById(R.id.signUpFinished);
        Button loginBackButton = findViewById(R.id.loginBack);

        //get EditText from layout
        TextInputLayout fullNameInputLayout = findViewById(R.id.nameInput);
        TextInputLayout emailInputLayout = findViewById(R.id.emailInput);
        TextInputLayout phoneInputLayout = findViewById(R.id.phoneInput);
        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInput);
        EditText fullNameEditText = fullNameInputLayout.getEditText();
        EditText emailEditText = emailInputLayout.getEditText();
        EditText phoneNumberEditText = phoneInputLayout.getEditText();
        EditText passwordEditText = passwordInputLayout.getEditText();

        signUpFinishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    //method to handle new user input
    private void saveUserDetailsToFirebase(String fullName, String email, String phoneNumber, String password){
        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            // if any field is empty, go to the signup error page
            Intent newActivity = new Intent(getApplicationContext(), SignUpError.class);
            startActivity(newActivity);
            return;
        }
        // get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Users");

        // create a unique ID for each user
        String userId = myRef.push().getKey();

        // create a user object or use a HashMap to organize user details
        User user = new User(fullName, email, phoneNumber, password);

        // save the user details under their unique ID
        if (userId != null) {
            myRef.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //just put the user into the FYP, no need to login again!
                    Intent newActivity = new Intent(getApplicationContext(), FYP.class);
                    startActivity(newActivity);
                } else {
                    //go to sign up error page, because firebase isn't working.
                    Intent newActivity = new Intent(getApplicationContext(), SignUpError.class);
                    startActivity(newActivity);
                }
            });
        }
    }
}
