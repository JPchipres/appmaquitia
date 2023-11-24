package com.example.appmaquitia;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appmaquitia.modelos.Asociacion;
import com.example.appmaquitia.modelos.alertas;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.FieldIndex;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class registro_de_asociacion_imagen extends AppCompatActivity {
    Uri ImageUri;
    String urlimg;
    Button seleccionarimg,subirImagen;
    ImageButton regresar;
    ImageView ivPreliminar;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    StorageReference mStoragref;
    String cluni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_asociacion_imagen);
        ivPreliminar = (ImageView) findViewById(R.id.ivFotoPreliminar);
        seleccionarimg = (Button) findViewById(R.id.btnSeleccionar);
        subirImagen = (Button) findViewById(R.id.btnSubir);
        regresar = (ImageButton) findViewById(R.id.btn_back);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mStoragref = storage.getReference();
        subirImagen.setEnabled(false);
        subirImagen.setVisibility(View.GONE);

        cluni = getIntent().getStringExtra("cluni");
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(registro_de_asociacion_imagen.this, perfil_osc.class));
            }
        });
        seleccionarimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seleccionarimg();

                subirImagen.setEnabled(true);
                subirImagen.setVisibility(View.VISIBLE);
            }
        });
        subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagenFStorage(ImageUri,cluni);
            }
        });

    }

    private void Seleccionarimg() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(abrirGaleria,1);
    }

    private void SubirImagenFStorage(Uri Image, String ID) {
        if (Image != null) {
            String imageName = ID+".jpg"; // Puedes generar un nombre único
            StorageReference imageRef = mStoragref.child(imageName);

            // Sube la imagen al Storage
            imageRef.putFile(Image)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Imagen subida con éxito, obtén la URL de descarga
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            urlimg = uri.toString();

                            // Ahora puedes guardar 'urlimg' en Firestore o realizar otras operaciones
                            actualizarURLFirestore(urlimg);

                        });
                    })
                    .addOnFailureListener(e -> {
                        // Maneja errores al subir la imagen
                        alertas.alertFalied(registro_de_asociacion_imagen.this, "¡Error al subir la imagen!",2000);
                    });
        } else {
            alertas.alertWarning(registro_de_asociacion_imagen.this,"Selecciona una imagen",2000);
        }
    }

    private void actualizarURLFirestore(String urlimg) {
        if (urlimg != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String cluni = getIntent().getStringExtra("cluni");
            // Consulta la colección "organizaciones" para encontrar el documento con "cluni" igual a "BAC99030825014"
            Query query = db.collection("organizaciones").whereEqualTo("cluni", cluni);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Obtiene la referencia al primer documento que coincide con la consulta
                        DocumentReference documentReference = querySnapshot.getDocuments().get(0).getReference();

                        // Actualiza el campo de la URL de la imagen
                        documentReference.update("foto", urlimg)
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito al actualizar el campo en Firestore
                                    alertas.alertSuccess(registro_de_asociacion_imagen.this, "La imagen fue actualizada con éxito",2000);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                })
                                .addOnFailureListener(e -> {
                                    // Maneja errores al actualizar el campo en Firestore
                                    alertas.alertFalied(registro_de_asociacion_imagen.this,"¡Error al actualizar la imagen!",2000);
                                });
                    } else {
                        alertas.alertWarning(registro_de_asociacion_imagen.this, "Documento no encontrado",2000);
                    }
                } else {
                    // Maneja errores al realizar la consulta en Firestore
                    alertas.alertFalied(registro_de_asociacion_imagen.this,"¡Error al consultar Firestore!",2000);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //DEL MÉTODO CARGAR IMAGEN
        if(resultCode == -1 && requestCode == 1 && data != null) {
            ImageUri = data.getData();
            Glide.with(this)
                    .load(ImageUri)
                    .into(ivPreliminar);

        }else {
            alertas.alertWarning(registro_de_asociacion_imagen.this, "Ninguna imagen fue seleccionada",2000);
        }
    }
}
