package com.example.letsmeetapp;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationRepository {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> userLoggedInMutableLiveData;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedInMutableLiveData() {
        return userLoggedInMutableLiveData;
    }

    public AuthenticationRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData();
        userLoggedInMutableLiveData = new MutableLiveData();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    public void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                } else {
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(String email, String pass, String name) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

                    User user = new User(email, pass, name);
                    database.getReference().child("users").child(auth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                          /*  AlertDialog dialog = new AlertDialog.Builder(application.getBaseContext()).create();
                            dialog.setTitle("Account Created");
                            dialog.setMessage("You can login to your account now");
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show(); */
                            Toast.makeText(application, "Account Created", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signOut() {
        auth.signOut();
        userLoggedInMutableLiveData.postValue(true);
    }
}

