package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {
    EditText etnombre, etmail, etpass, etpassconfirm;
    Button btnregistrar, btnasociacion, btnexit;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        etnombre = findViewById(R.id.etnombre);
        etmail = findViewById(R.id.etmail);
        etpass = findViewById(R.id.etpass);
        etpassconfirm = findViewById(R.id.etpassconfirm);
        btnregistrar = findViewById(R.id.btnregistrar);
        btnasociacion = findViewById(R.id.btnregistrarAso);
        btnexit = findViewById(R.id.btn_back);

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(registro.this, pruebas.class);
                startActivity(i);
            }
        });

        btnasociacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(registro.this, registro_de_asociacion.class);
                startActivity(i);
            }
        });

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etnombre.setError(null);
                etpass.setError(null);
                etpassconfirm.setError(null);
                etmail.setError(null);
                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
                final String regexpass = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,63}$";
                String nombre = etnombre.getText().toString().trim();
                String mail = etmail.getText().toString().trim();
                String pass = etpass.getText().toString().trim();
                String passconfirm = etpassconfirm.getText().toString().trim();
                if(nombre.isEmpty() && mail.isEmpty() && pass.isEmpty() && passconfirm.isEmpty())
                {
                    Toast.makeText(registro.this, "Debes acompletar todos los campos.", Toast.LENGTH_SHORT).show();
                }else if (nombre.length() <= 8){
                    etnombre.setError("Ingresa tu nombre completo.");
                    etnombre.requestFocus();
                    return;
                } else if (!mail.matches(regex)) {
                    etmail.setError("Formato del correo invalido.");
                    etmail.requestFocus();
                    return;
                }
                else if (!pass.matches(regexpass)) {
                    etpass.setError("Contraseña debe contener 8 caracteres y debe contener números, letras minúsculas y mayúsculas..");
                    etpass.requestFocus();
                    return;
                } else if (!pass.equals(passconfirm)) {
                    etpass.setError("Las contraseñas no son iguales.");
                    etpass.requestFocus();
                    etpassconfirm.setError("Las contraseñas no son iguales.");
                    etpassconfirm.requestFocus();
                    return;
                }else{
                    registrarUsuario(nombre, mail, pass);
                }
            }
        });
    }
    private void registrarUsuario(String nombre, String mail, String pass){
        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String hashed = Hashing.sha256()
                        .hashString(pass, StandardCharsets.UTF_8)
                        .toString();
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nombre);
                map.put("email", mail);
                map.put("password", hashed);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(registro.this, pruebas.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registro.this, "Error al añadir coleccion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registro.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}