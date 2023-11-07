package com.example.appmaquitia;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.appmaquitia.adaptadores.AnuncioAdapter;
import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.example.appmaquitia.modelos.Anuncio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    Uri imageuri;
    private static final int SELECT_PICTURE = 1;
    private boolean imagenCargada  = false;
    private String asociacionID = "4WZHbfJDD7QhbqBjUNop"; //dinamicamente se cambiará

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);

        cargarAnuncios(asociacionID);

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
                if(imagenCargada) {
                    subirImagen(imageuri);
                }else {
                    if(!b.etNuevoAnuncio.getText().toString().equals("")) {
                        crearAnuncio(asociacionID,"");
                    }
                }
            }});
        b.btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();
            }
        });
    }
    public void cargarAnuncios(String organizacionID) {
        //Esta función se encarga de traer todos los anuncios de cada organización y establece un escuchador para que el recyclerview de se actualicé automaticamente cada que
        //se agrega un nuevo anuncio, lo que ayuda a que el usuario no tenga que recargar la ventana
        //recibe como parametro el id de la organización para que el usuario decida cual chat verbu
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
    public void crearAnuncio(String organizacionID, String imagen) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date fecha_hora= new Date();
        Timestamp ts = new Timestamp(fecha_hora);
        Map<String, Object> anuncio = new HashMap<>();
        anuncio.put("cuerpo", b.etNuevoAnuncio.getText().toString());
        anuncio.put("fecha_hora", ts);
        anuncio.put("imagen", imagen);
        //esto es provicional debe cambiar
        db.collection("organizaciones")
                .document(organizacionID)
                .collection("anuncios")
                .document()
                .set(anuncio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PublicacionesActivity.this, "Se creo un nuevo anuncio", Toast.LENGTH_SHORT).show();
                        restablecerInput();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PublicacionesActivity.this, "No se pudo crear un nuevo anuncio", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void cargarImagen() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(abrirGaleria,SELECT_PICTURE);
    }
    public void subirImagen(Uri imagenUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("imagenes/*");
        imagesRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                if(uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadURI = uri.toString();
                            crearAnuncio(asociacionID, downloadURI);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PublicacionesActivity.this,"Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void restablecerInput() {
        imageuri = null;
        imageuri=Uri.parse("");
        b.etNuevoAnuncio.setText("");
        b.btnCargarImagen.setImageResource(R.drawable.imagen_no_adjuntada);
        b.btnCargarImagen.setEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //DEL MÉTODO CARGAR IMAGEN
        if(resultCode == RESULT_OK && requestCode == SELECT_PICTURE && data != null) {
            imageuri = data.getData();
            b.btnCargarImagen.setImageResource(R.drawable.imagen_adjuntada);
            imagenCargada = true;
            b.btnCargarImagen.setEnabled(false);
        }else {
            Toast.makeText(this, "No selected image", Toast.LENGTH_SHORT).show();
        }
    }
}