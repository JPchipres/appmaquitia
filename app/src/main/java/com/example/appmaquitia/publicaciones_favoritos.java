package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.appmaquitia.adaptadores.AsociacionesAdapter;
import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.modelos.Asociacion;
import com.example.appmaquitia.modelos.alertas;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class publicaciones_favoritos extends AppCompatActivity implements Asociacioninterface, BottomNavigationView.OnNavigationItemSelectedListener{
    RecyclerView asociacionR;
    AsociacionesAdapter asociacionA;
    FirebaseFirestore asociacionF;
    BottomNavigationView navbar;
    BottomNavigationView bottomNavigationView;
    Intent i;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText etBuscador;
    FirebaseUser usuarioActual = mAuth.getCurrentUser();
    String userId;
    DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_favoritos);
        asociacionF = FirebaseFirestore.getInstance();
        asociacionR = findViewById(R.id.rv_asociaciones);
        asociacionR.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.navbar);
        etBuscador = (EditText) findViewById(R.id.etBuscador);

        if(usuarioActual != null){
            userId = usuarioActual.getUid();
        }

        userRef = asociacionF.collection("user").document(userId);
        Query query = userRef.collection("favoritos");
        FirestoreRecyclerOptions<Asociacion> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Asociacion>().setQuery(query,Asociacion.class).build();
        asociacionA = new AsociacionesAdapter(firestoreRecyclerOptions,this,this);
        asociacionA.notifyDataSetChanged();

        asociacionR.setAdapter(asociacionA);
        navbar = (BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void onStart(){
        super.onStart();
        asociacionA.startListening();
        asociacionA.notifyDataSetChanged();
    }

    protected void onStop(){
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
    public void buscar(String filtro) {
        Query query = asociacionF.collection("user").document(userId).collection("favoritos")
                .orderBy("nombre") // Ordena por el campo que desees
                .startAt(filtro)
                .endAt(filtro + "\uf8ff");

        FirestoreRecyclerOptions<Asociacion> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Asociacion>().setQuery(query, Asociacion.class).build();

        asociacionA.updateOptions(firestoreRecyclerOptions); // Actualiza el adaptador con los nuevos resultados
        asociacionA.notifyDataSetChanged();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            i = new Intent(publicaciones_favoritos.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.favs) {
            return false;
        } else if (item.getItemId() == R.id.perfil) {
            i = new Intent(publicaciones_favoritos.this, perfil_donador.class);
            startActivity(i);
        }
        return false;
    }
}