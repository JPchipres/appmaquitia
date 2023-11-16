package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class perfil_osc extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView nom_osc, email_osc, total_dinero, conteo;
    Button editar, tyc, signout;

    Intent i;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "perfil_osc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_osc);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        nom_osc = (TextView) findViewById(R.id.txtnombre);
        email_osc = (TextView) findViewById(R.id.txtemail);
        total_dinero = (TextView) findViewById(R.id.totaldinero);
        conteo = (TextView) findViewById(R.id.conteo);
        editar = (Button) findViewById(R.id.editar_perfil);
        tyc = (Button) findViewById(R.id.terminos);
        signout = (Button) findViewById(R.id.cerrar_sesion);
        bottomNavigationView
                = findViewById(R.id.navbar);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);

        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            email_osc.setText(email);
            mFirestore.collection("organizaciones").whereEqualTo("correo",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String nombreosc = document.getString("nombre");
                        nom_osc.setText(nombreosc);
                    }
                    } else {
                    Log.e(TAG, "Error obtentiendo documento: ", task.getException());
                }

                }
            });

        }
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(perfil_osc.this, editar_osc.class));
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(perfil_osc.this, MainActivity.class));
            }
        });




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            i = new Intent(perfil_osc.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.favs) {
            i = new Intent(perfil_osc.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {

            return false;
        }
        return false;
    }
}