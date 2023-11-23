package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.example.appmaquitia.adaptadores.DonacionAdapter;
import com.example.appmaquitia.adaptadores.TransaccionAdapter;
import com.example.appmaquitia.modelos.Transaccion;
import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListaDonaciones extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    String cluni, nombreosc;
    BottomNavigationView bottomNavigationView;
    ListView List;
    ArrayList<Transaccion> dataModalArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_donaciones);

        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        List = (ListView) findViewById(R.id.lvDonaciones);
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        dataModalArrayList = new ArrayList<>();


        cluni = getIntent().getStringExtra("ID");
        nombreosc = getIntent().getStringExtra("nombre");

        mFirestore.collection("organizaciones").document(cluni).collection("donaciones").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    java.util.List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        // after getting this list we are passing
                        // that list to our object class.
                        Transaccion dataModal = d.toObject(Transaccion.class);

                        // after getting data from Firebase we are
                        // storing that data in our array list
                        dataModalArrayList.add(dataModal);
                    }
                    // after that we are passing our array list to our adapter class.
                    DonacionAdapter adapter = new DonacionAdapter(getApplicationContext(),dataModalArrayList);

                    // after passing this array list to our adapter
                    // class we are setting our adapter to our list view.
                    List.setAdapter(adapter);
                }else {
                    alertas.alertSuccess(ListaDonaciones.this, "Aún no haz recibido donaciones",1000);
                }
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.historialDonaciones){
            return false;
        } else if (item.getItemId() == R.id.chat) {
            Intent i = new Intent(ListaDonaciones.this, PublicacionesActivity.class);
            i.putExtra("ID",cluni);
            i.putExtra("nombre",nombreosc);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {
            Intent i = new Intent(ListaDonaciones.this,perfil_osc.class);
            startActivity(i);
        }
        return false;
    }
}