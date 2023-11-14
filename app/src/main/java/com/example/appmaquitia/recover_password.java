package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.appmaquitia.modelos.alertas;

public class recover_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        ImageButton regresar = (ImageButton) findViewById(R.id.btn_back);
        EditText etCorreo = (EditText) findViewById(R.id.etCorreo);
        Button btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}