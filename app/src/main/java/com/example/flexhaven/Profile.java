package com.example.flexhaven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flexhaven.helpers.CommonData;

public class Profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //dynamically set username and user points!
        TextView usernameTextView = findViewById(R.id.userNameTextView);
        usernameTextView.setText(CommonData.getInstance().getUsername());
        TextView userPointsTextView = findViewById(R.id.userPointsTextView);
        userPointsTextView.setText("User Points: " + String.valueOf(CommonData.getInstance().getUserPoints()));

        Button addItemButton = findViewById(R.id.AddItemProfile);
        //TODO implement dynamic display here!
        Button displayListingsButton = findViewById(R.id.DisplayListingsProfile);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Add.class));
            }
        });
    }
}
