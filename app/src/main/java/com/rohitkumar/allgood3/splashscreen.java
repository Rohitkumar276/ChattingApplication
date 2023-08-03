package com.rohitkumar.allgood3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;



public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();

        Thread thread = new Thread(() -> {
            try{
                Thread.sleep(3000);
            }
            catch(Exception e){
                e.printStackTrace();

            }
            finally {

                Intent intent = new Intent(splashscreen.this , SignInActivity.class);
                startActivity(intent);

            }
        });
        thread.start();


    }

}