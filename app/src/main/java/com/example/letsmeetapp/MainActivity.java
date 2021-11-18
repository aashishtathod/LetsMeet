package com.example.letsmeetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.letsmeetapp.databinding.ActivityMainBinding;


import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.Code.getText().toString().trim().isEmpty()) {
                    try {
                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setWelcomePageEnabled(false)
                                .build();

                        options = new JitsiMeetConferenceOptions.Builder().setRoom(binding.Code.getText().toString().trim()).build();
                        JitsiMeetActivity.launch(MainActivity.this, options);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                    dialog.setTitle("Required");
                    dialog.setMessage("Secret Code can't be Empty! \n Follow the Instructions Given Below");
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
        });


        binding.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT , binding.Code.getText().toString().trim());
                startActivity(Intent.createChooser(intent, "Share With" ));
            }
        });
    }
}


