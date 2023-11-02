package com.example.appmaquitia;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.AnuncioAdapter;
import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.example.appmaquitia.modelos.Anuncio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicacionesActivity extends AppCompatActivity {
    private ActivityPublicacionesBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        b.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplaza "phoneNumber" con el número de teléfono al que deseas abrir el chat en WhatsApp.
                //String phoneNumber = "3141438337";
                //String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
                //Intent intent  = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(url));
                //startActivity(intent);

            }
        });
        db.collection("organizaciones")
                .document("4WZHbfJDD7QhbqBjUNop") // Reemplaza "ID_de_Organizacion" con el ID de la organización específica que deseas acceder
                .collection("anuncios")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Anuncio> anuncios = new ArrayList<>();
                        for (QueryDocumentSnapshot document :queryDocumentSnapshots) {
                            String cuerpo = document.getString("cuerpo");
                            Timestamp fecha_hora = document.getTimestamp("fecha_hora");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String url_imagen = document.getString("imagen");
                            Date date = fecha_hora.toDate();
                            String fechaFormateada = dateFormat.format(date);
                            Anuncio anuncio = new Anuncio(cuerpo,fechaFormateada,url_imagen);
                            anuncios.add(anuncio);
                        }
                        //Toast.makeText(PublicacionesActivity.this, "hay: " + queryDocumentSnapshots.size(), Toast.LENGTH_SHORT).show();
                        RecyclerView recyclerView = b.rvPublicaciones;
                        AnuncioAdapter adapter = new AnuncioAdapter(anuncios,PublicacionesActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG , "QUE HABRÁ PASADO NO SE: " +e);
                    }
                });
    }
}