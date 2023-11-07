package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button registro, iniciar;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registro = (Button) findViewById(R.id.btnRegistro);
        i = new Intent(MainActivity.this, PublicacionesActivity.class); //Intent que dirije a lo que estoy trabajando actualmente
        startActivity(i);

        iniciar = (Button) findViewById(R.id.btnInicioSesion);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i = new Intent(MainActivity.this, publicaciones.class);

                i = new Intent(MainActivity.this, publicaciones.class);
                startActivity(i);

            }
        });
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, login.class);
                startActivity(i);
            }
        });
    }
}