package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class detallespago extends AppCompatActivity {
    ImageButton btnregresar;
    String nombre, userID, cluni, monto, fecha, tarjeta, fexpiracion, ccvv;
    Button pagar, cancelar;
    EditText ntarjeta, expiracion, cvv;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallespago);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        btnregresar = (ImageButton) findViewById(R.id.btn_back);
        pagar = (Button) findViewById(R.id.pagar);
        cancelar = (Button) findViewById(R.id.cancelar);
        ntarjeta = (EditText) findViewById(R.id.card);
        expiracion = (EditText) findViewById(R.id.date);
        cvv = (EditText) findViewById(R.id.cvv);
        nombre = getIntent().getStringExtra("nombre");
        userID = getIntent().getStringExtra("userID");
        cluni = getIntent().getStringExtra("ID");
        monto = getIntent().getStringExtra("monto");
        fecha = getIntent().getStringExtra("fecha");

        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarjeta = ntarjeta.getText().toString();
                fexpiracion = expiracion.getText().toString();
                ccvv = cvv.getText().toString();
                if(!tarjeta.isEmpty() && !fexpiracion.isEmpty()&& !ccvv.isEmpty()) {
                    Log.d("pagar->", "onclick");
                    pagarorden(nombre, userID, cluni, monto, fecha);
                }else {
                    alertas.alertWarning(detallespago.this,"Rellene todos los campos",2000);
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(detallespago.this, publicaciones.class);
            }
        });
    }

    private void pagarorden(String nombre, String userID, String cluni, String monto, String fecha) {
        Log.d("detallespago->", "pagar orden");
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("userID", userID);
        data.put("oscID", cluni);
        data.put("monto", monto);
        data.put("fecha", fecha);
        mFirestore.collection("transacciones").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("pagarorden->", "onsuccess");
                Intent i = new Intent(detallespago.this, pagoexitoso.class);
                startActivity(i);
            }
        });
    }
}