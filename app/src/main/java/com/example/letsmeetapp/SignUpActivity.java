package com.example.letsmeetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.letsmeetapp.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private AuthViewModel authViewModel;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dialog.setCancelable(false);

        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(SignUpActivity.this.getApplication())).get(AuthViewModel.class);

        authViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    dialog.dismiss();
                }
            }
        });

        binding.tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.Name.getText().toString().trim();
                String email = binding.Email.getText().toString().trim();
                String pass = binding.Password.getText().toString().trim();

                dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage("We are creating your Account");
                dialog.setTitle("Creating Account");

                if (!email.isEmpty() && !pass.isEmpty() && !name.isEmpty()) {
                    dialog.show();
                    authViewModel.register(email, pass, name);
                }
            }
        });


    }
}