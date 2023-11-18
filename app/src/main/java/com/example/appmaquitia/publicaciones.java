package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.AsociacionesAdapter;
import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.modelos.Asociacion;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class publicaciones extends AppCompatActivity implements Asociacioninterface, BottomNavigationView.OnNavigationItemSelectedListener {
    RecyclerView asociacionR;
    AsociacionesAdapter asociacionA;
    FirebaseFirestore asociacionF;
    Intent i;
    BottomNavigationView bottomNavigationView;
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
        ImageButton regresar = (ImageButton) findViewById(R.id.btn_back);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        Intent i = new Intent(this, PublicacionesActivityUsuarios.class);
        i.putExtra("nombre",asociacionA.getItem(position).getNombre());
        i.putExtra("topic",asociacionA.getItem(position).getNombre());
        i.putExtra("numero",asociacionA.getItem(position).getTelefono());
        i.putExtra("ID",asociacionA.getItem(position).getCluni());
        startActivity(i);

        /*Intent i = new Intent(this, Detalleorganizacion.class);
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
        i.putExtra("telefono",asociacionA.getItem(position).getTelefono());
        i.putExtra("topico",asociacionA.getItem(position).getTopic());

        startActivity(i);
        */


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            i = new Intent(publicaciones.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.favs) {
            i = new Intent(publicaciones.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {
            i = new Intent(publicaciones.this, perfil_donador.class);
            startActivity(i);
        }
        return false;
    }
}