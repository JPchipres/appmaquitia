package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.appmaquitia.adaptadores.AsociacionesAdapter;
import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.modelos.Asociacion;
import com.example.appmaquitia.modelos.alertas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class publicaciones extends AppCompatActivity implements Asociacioninterface, BottomNavigationView.OnNavigationItemSelectedListener {
    RecyclerView asociacionR;
    AsociacionesAdapter asociacionA;
    FirebaseFirestore asociacionF;
    BottomNavigationView navbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Intent i;
    BottomNavigationView bottomNavigationView;
    FirebaseUser usuarioActual = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_inicio);
        asociacionF = FirebaseFirestore.getInstance();
        asociacionR = findViewById(R.id.rv_asociaciones);
        asociacionR.setLayoutManager(new LinearLayoutManager(this));
        Query query = asociacionF.collection("organizaciones");
        FirestoreRecyclerOptions<Asociacion> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Asociacion>().setQuery(query,Asociacion.class).build();
        asociacionA = new AsociacionesAdapter(firestoreRecyclerOptions,this);
        asociacionA.notifyDataSetChanged();
        asociacionR.setAdapter(asociacionA);

        navbar = (BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        asociacionA.startListening();
        asociacionA.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        asociacionA.stopListening();
    }
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, Detalleorganizacion.class);
        i.putExtra("nombre",asociacionA.getItem(position).getNombre());
        i.putExtra("actividades",asociacionA.getItem(position).getActividades());
        i.putExtra("calle",asociacionA.getItem(position).getCalle());
        i.putExtra("cluni",asociacionA.getItem(position).getCluni());
        i.putExtra("colonia",asociacionA.getItem(position).getColonia());
        i.putExtra("correo",asociacionA.getItem(position).getCorreo());
        i.putExtra("cp",asociacionA.getItem(position).getCp());
        i.putExtra("descripcion",asociacionA.getItem(position).getDescripcion());
        i.putExtra("entidad",asociacionA.getItem(position).getEntidad());
        i.putExtra("municipio",asociacionA.getItem(position).getMunicipio());
        i.putExtra("numero_ext",asociacionA.getItem(position).getNum_ext());
        i.putExtra("representante",asociacionA.getItem(position).getRepresentantes());
        i.putExtra("numero",asociacionA.getItem(position).getTelefono());
        i.putExtra("topico",asociacionA.getItem(position).getTopic());
        i.putExtra("foto",asociacionA.getItem(position).getFoto());

        startActivity(i);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            return false;
        } else if (item.getItemId() == R.id.favs) {
            if(usuarioActual != null) {
                i = new Intent(publicaciones.this, publicaciones_favoritos.class);
                startActivity(i);
            }else {
                alertas.alertWarning(publicaciones.this, "Inicia sesión para ver tus favoritos",2000);
            }
        } else if (item.getItemId() == R.id.perfil) {
            if(usuarioActual != null) {
                i = new Intent(publicaciones.this, perfil_donador.class);
                startActivity(i);
            }else {
                alertas.alertWarning(publicaciones.this, "Inicia sesión para ver tu perfil",2000);
            }
        }
        return false;
    }
}