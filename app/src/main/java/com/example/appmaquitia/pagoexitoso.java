package com.example.appmaquitia;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class pagoexitoso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagoexitoso);
        Intent i = new Intent(pagoexitoso.this, publicaciones.class);
        startActivity(i);
    }
}