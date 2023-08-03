package com.rohitkumar.allgood3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import com.onesignal.OneSignal;
import com.rohitkumar.allgood3.Adapters.FragmentsAdapter;
import com.rohitkumar.allgood3.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {




    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);





        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());





        auth = FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.groupChat:

                Intent intentt = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intentt);

        }
        return true;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Exit")
                .setMessage("are you sure want to exit this app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}