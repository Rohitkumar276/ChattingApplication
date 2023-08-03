package com.rohitkumar.allgood3;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitkumar.allgood3.Models.Users;
import com.rohitkumar.allgood3.databinding.ActivitySignUpBinding;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    private FirebaseAuth auth;

    FirebaseDatabase database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");


        binding.btnSignUp.setOnClickListener(v -> {
            if(binding.etUsername.getText().toString().isEmpty()){
                binding.etUsername.setError("Enter your Username");
                return;
            }
            if(binding.etEmail.getText().toString().isEmpty()){
                binding.etEmail.setError("Enter your email");
                return;
            } if(binding.etPassword.getText().toString().isEmpty()){
                binding.etPassword.setError("Enter your Password");
                return;
            }

            progressDialog.show();

            auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),
                            binding.etPassword.getText().toString()).
                    addOnCompleteListener(task -> {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Users user =new Users(binding.etUsername.getText().toString(),
                                    binding.etEmail.getText().toString(),
                                    binding.etPassword.getText().toString());

                            String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        binding.tvAlreadyAccount.setOnClickListener(v -> {
            Intent intent= new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
        });






    }
}