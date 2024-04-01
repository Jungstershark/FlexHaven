package com.example.flexhaven;
import com.example.flexhaven.helpers.CommonData;
import com.example.flexhaven.helpers.User;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private TextView loginErrorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailTextInputEditText); // Add this to your layout
        passwordEditText = findViewById(R.id.passwordTextInputEditText);
        loginErrorTextView = findViewById(R.id.loginErrorTextView);

        //set buttons here
        Button loginNextButton = findViewById(R.id.loginNext);
        Button signUpNextButton = findViewById(R.id.signUpNext);

        //login button handling
        loginNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                loginUser(email, password);
            }
        });
        //sign up button handling
        signUpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }
    private void loginUser(String email, String password) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://infosys-37941-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        System.out.println(user.password);
                        if (user != null && user.password.equals(password)) {
                            CommonData.getInstance().setUsername(user.fullName);
                            CommonData.getInstance().setUserPoints(user.userPoints);
                            startActivity(new Intent(Login.this, FYP.class));
                            finish();
                        } else {
                            loginErrorTextView.setText("Password incorrect!");
                        }
                    }
                } else {
                    loginErrorTextView.setText("User does not exist!");
                }
            }
            //for database errors
            @Override
            public void onCancelled(DatabaseError databaseError) {
                loginErrorTextView.setText("Error logging in: " + databaseError.getMessage());
            }
        });
    }
}

