package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button registro;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registro = (Button) findViewById(R.id.btnRegistro);
        i = new Intent(MainActivity.this, registro_de_asociacion.class); //Intent que dirije a lo que estoy trabajando actualmente
        startActivity(i);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, registro.class);
                startActivity(i);

            }
        });
    }
}