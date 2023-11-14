package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    EditText txtemail, txtpass;
    Button iniciar;
    ImageButton regresar;
    TextView restablecer;

    FirebaseAuth mAuth;
    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        txtemail = (EditText) findViewById(R.id.etmail);
        txtpass = (EditText) findViewById(R.id.pass);
        iniciar = (Button) findViewById(R.id.entrar);
        regresar = (ImageButton) findViewById(R.id.btnRegresar);
        restablecer = (TextView) findViewById(R.id.btnRestablecer);


        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString();
                String pass = txtpass.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty()) {
                    autenticar(email, pass);
                }else {
                    alertas.alertWarning(login.this,"Rellene todos los campos",2000);
                }

            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, new_password.class));
            }
        });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void autenticar(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    i = new Intent(getApplicationContext(), publicaciones.class);
                    startActivity(i);
                    finish();
                }else{
                    alertas.alertFalied(login.this,"Credenciales Incorrectas",2000);
                }
            }
        });
    }
}