package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.AsociacionesAdapter;
import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.modelos.Asociacion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class publicaciones extends AppCompatActivity implements Asociacioninterface {
    RecyclerView asociacionR;
    AsociacionesAdapter asociacionA;
    FirebaseFirestore asociacionF;
    BottomNavigationView navbar;
    Intent i;
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

        navbar.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.favs){
                i = new Intent(this, publicaciones_favoritos.class);
                startActivity(i);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        asociacionA.startListening();
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
        i.putExtra("telefono",asociacionA.getItem(position).getTelefono());
        i.putExtra("topico",asociacionA.getItem(position).getTopic());

        startActivity(i);
    }
}