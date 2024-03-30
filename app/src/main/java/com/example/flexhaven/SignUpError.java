package com.example.flexhaven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpError extends AppCompatActivity {

    //this here happens when the app first opens-layout and stuff created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_error);
        //button created here
        //find button id loginNext
        Button loginNextButton = findViewById(R.id.loginNext);

        //creating a new OnClickListener interface that REQUIRES a onClick method
        //View is a superclass of Button
        loginNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent -> object that can help start another activity, needs to be imported.
                //An Activity is just another screen with a user Interface.
                //old activites are not automatically destroyed, just added to a navigation Stack lol
                //u need method finish() i think if u r sure u are not navigating back
                Intent newActivity = new Intent(getApplicationContext(),SignUp.class);
                startActivity(newActivity);
            }
        });


    }
}