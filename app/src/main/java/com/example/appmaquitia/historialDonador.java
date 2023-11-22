package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.TransaccionAdapter;
import com.example.appmaquitia.modelos.Transaccion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class historialDonador extends AppCompatActivity {
    ListView List;
    ArrayList<Transaccion> dataModalArrayList;
    ImageButton regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_donador);
        List = (ListView) findViewById(R.id.List);
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dataModalArrayList = new ArrayList<>();
        regresar = (ImageButton) findViewById(R.id.btn_back);

        // here we are calling a method
        // to load data in our list view.
        // below line is use to get data from Firebase
        // firestore using collection in android.
        mFirestore.collection("transacciones").whereEqualTo("userID", user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are hiding
                            // our progress bar and adding our data in a list.
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
                            TransaccionAdapter adapter = new TransaccionAdapter(historialDonador.this, dataModalArrayList);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            List.setAdapter(adapter);
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(historialDonador.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(historialDonador.this, perfil_donador.class);
                startActivity(i);
            }
        });
    }
}