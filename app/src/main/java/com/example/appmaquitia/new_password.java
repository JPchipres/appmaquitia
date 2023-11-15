package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class new_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        Button restablecer = (Button) findViewById(R.id.btnRestablecer);
        ImageButton regresar = (ImageButton) findViewById(R.id.btnRegresar);
        EditText pass1 = (EditText) findViewById(R.id.etPass1);
        EditText pass2 = (EditText) findViewById(R.id.etPass2);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}