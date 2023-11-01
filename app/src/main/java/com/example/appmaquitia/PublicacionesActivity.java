package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class PublicacionesActivity extends AppCompatActivity {
    private ActivityPublicacionesBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


    }
}