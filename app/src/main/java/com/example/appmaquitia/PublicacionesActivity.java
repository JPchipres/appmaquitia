package com.example.appmaquitia;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.AnuncioAdapter;
import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.example.appmaquitia.modelos.Anuncio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//ESTA ES LA VISTA DE LAS ORGANIZACIONES PARA CREAR ANUNCIOS
public class PublicacionesActivity extends AppCompatActivity {
    private ActivityPublicacionesBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);

        cargarAnuncios("4WZHbfJDD7QhbqBjUNop");


        b.btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//mata este activity y regresa al activity que lo lanzó inicialmente
            }
        });
        b.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirWhatsapp("3141438337");
            }
        });
        b.btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(this,perfilOrganizacion.class);
                //startActivity(i);
            }
        });
        b.btnEnviarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!b.etNuevoAnuncio.getText().toString().equals("")) {
                    crearAnuncio("4WZHbfJDD7QhbqBjUNop");
                }
            }});
    }
    //Esta función se encarga de traer todos los anuncios de cada organización y establece un escuchador para que el recyclerview de se actualicé automaticamente cada que
    //se agrega un nuevo anuncio, lo que ayuda a que el usuario no tenga que recargar la ventana
    //recibe como parametro el id de la organización para que el usuario decida cual chat verbu
    public void cargarAnuncios(String organizacionID) {
        CollectionReference anunciosRef = FirebaseFirestore.getInstance().collection("organizaciones/"+organizacionID+"/anuncios");

        anunciosRef.orderBy("fecha_hora", Query.Direction.ASCENDING).addSnapshotListener((queryDocumentSnapshot, e) -> {
            //Manejo de errores
            if(e != null) {
                Log.e(TAG, "Error al escuchar los cambios de la collección: " + e);
                return;
            }
            if(queryDocumentSnapshot != null) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshot) {
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
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
            }
        });
    }
    public void abrirWhatsapp(String numeroDeTelefono) {
        String url = "https://api.whatsapp.com/send?phone=" + numeroDeTelefono;
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void crearAnuncio(String organizacionID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SoundPool soundPool;
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        int sonidoConfirmacion = soundPool.load(this, R.raw.sonido_success, 1);
        int sonidoFallo = soundPool.load(this,R.raw.sonido_error,1);

        Date fecha_hora= new Date();
        Timestamp ts = new Timestamp(fecha_hora);
        Map<String, Object> anuncio = new HashMap<>();
        anuncio.put("cuerpo", b.etNuevoAnuncio.getText().toString());
        anuncio.put("fecha_hora", ts);
        anuncio.put("imagen", "");

        db.collection("organizaciones")
                .document(organizacionID)
                .collection("anuncios")
                .document()//esto es provicional debe cambiar
                .set(anuncio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PublicacionesActivity.this, "Se creo un nuevo anuncio", Toast.LENGTH_SHORT).show();
                        soundPool.play(sonidoConfirmacion, 1.0f, 1.0f, 0, 0, 1.0f);
                        b.etNuevoAnuncio.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PublicacionesActivity.this, "No se pudo crear un nuevo anuncio", Toast.LENGTH_SHORT).show();
                        soundPool.play(sonidoFallo, 1.0f, 1.0f, 0, 0, 1.0f);
                    }
                });
    }
}