package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class perfil_donador extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore firebasefirestore;
    FirebaseAuth mAuth;
    Button logout, editar;
    Intent i;
    BottomNavigationView bottomNavigationView;
    TextView txtnombre, txtemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_donador);
        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txtemail = (TextView) findViewById(R.id.txtemail);
        editar = (Button) findViewById(R.id.editar_perfil);
        logout = (Button) findViewById(R.id.cerrar_sesion);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            txtemail.setText(email);
            mFirestore.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        txtnombre.setText(document.getString("name"));
                        editar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(perfil_donador.this, editar_donador.class));
                            }
                        });
                    }
                }
            });

        }
        bottomNavigationView
                = findViewById(R.id.navbar);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                i = new Intent(perfil_donador.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            i = new Intent(perfil_donador.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.favs) {
            i = new Intent(perfil_donador.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {
            i = new Intent(perfil_donador.this, perfil_donador.class);
            startActivity(i);
        }
        return false;
    }
}