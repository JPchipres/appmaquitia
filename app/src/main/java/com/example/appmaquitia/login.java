package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class login extends AppCompatActivity {
    EditText txtemail, txtpass;
    Button iniciar;
    ImageButton regresar;
    TextView btnRegistro;
    TextView restablecer;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
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
        btnRegistro = (TextView) findViewById(R.id.btnRegistro);
        mFirestore = FirebaseFirestore.getInstance();
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,registro.class);
                startActivity(i);
            }
        });
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString();
                String pass = txtpass.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty()) {

                    FirebaseUser cu = mAuth.getCurrentUser();
                    if(cu != null) {
                        mAuth.signOut();
                        autenticar(email, pass);
                    }else {
                        autenticar(email, pass);
                    }
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
                startActivity(new Intent(login.this, recover_password.class));
            }
        });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void autenticar(String email, String pass) {

        Log.d("LOGIN ->", "AUTENTICAR");
        mFirestore.collection("user").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("LOGIN ->", "ONCOMPLETE");
                for (QueryDocumentSnapshot doc : task.getResult()){
                    if (task.isSuccessful()) {
                        iniciarSesion(email, pass);
                    }
                    else{
                        alertas.alertFalied(login.this, "Credenciales Incorrectas", 2000);
                    }
                }
            }
        });
    }
    private void iniciarSesion(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        i = new Intent(getApplicationContext(), publicaciones_favoritos.class);
                        startActivity(i);
                        finish();
                    } else {
                        alertas.alertWarning(login.this, "Verifica tu correo para inciar sesión",2000);
                    }
                }else{
                    alertas.alertFalied(login.this,"Credenciales Incorrectas",2000);
                }
            }
        });
    }
}