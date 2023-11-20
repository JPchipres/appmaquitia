package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class editar_donador extends AppCompatActivity {

    TextView txtnom, txtemail;
    EditText etnom, etemail;
    Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_donador);
    }
}