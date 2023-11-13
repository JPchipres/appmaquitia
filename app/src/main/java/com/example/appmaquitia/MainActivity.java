package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView btnpublicaciones;
    Button registro, iniciar;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registro = (Button) findViewById(R.id.btnRegistro);
        iniciar = (Button) findViewById(R.id.btnInicioSesion);
        btnpublicaciones = findViewById(R.id.btn_publicaciones);
        Intent ii = new Intent(this, codigo_otp.class);
        startActivity(ii);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, login.class);
                startActivity(i);
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, registro.class);
                startActivity(i);

            }
        });
        btnpublicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, publicaciones.class);
                startActivity(i);
            }
        });

    }
}