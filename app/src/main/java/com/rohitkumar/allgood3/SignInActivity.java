package com.rohitkumar.allgood3;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.rohitkumar.allgood3.Models.Users;
import com.rohitkumar.allgood3.databinding.ActivitySignInBinding;


import java.util.Objects;


public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.btnSignIn.setOnClickListener(v -> {
            if (binding.etEmail.getText().toString().isEmpty()) {
                binding.etEmail.setError("Enter your email");
                return;
            }
            if (binding.etPassword.getText().toString().isEmpty()) {
                binding.etPassword.setError("Enter your Password");
                return;
            }
            progressDialog.show();
            auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),
                    binding.etPassword.getText().toString()).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(SignInActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.tvClickSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        binding.btnGoogle.setOnClickListener(v -> signIn());

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    int RC_SIGN_IN = 65;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);

            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();

                        Users users = new Users();
                        assert user != null;
                        users.setUserId(user.getUid());
                        users.setUserName(user.getDisplayName());
                        users.setProfilepic(Objects.requireNonNull(user.getPhotoUrl()).toString());
                        database.getReference().child("Users").child(user.getUid()).setValue(users);


                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignInActivity.this, "Sign in with Google", Toast.LENGTH_SHORT).show();
                        //updateUI(user);
                    } else {

                    }
                });

    }


}
