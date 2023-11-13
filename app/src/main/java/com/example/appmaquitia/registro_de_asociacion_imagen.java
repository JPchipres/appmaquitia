package com.example.appmaquitia;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
    Button seleccionarimg;
    ImageButton regresar;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    StorageReference mStoragref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_asociacion_imagen);
        seleccionarimg = (Button) findViewById(R.id.btnSeleccion);
        regresar = (ImageButton) findViewById(R.id.btn_back);
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mStoragref = storage.getReference();


        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Seleccionarimg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }
    ActivityResultLauncher<Intent> launcher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result->{
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null && data.getData()!=null){
                        ImageUri = data.getData();
                        SubirImagenFStorage();
                    }else{
                        Toast.makeText(this, "Imagen no seleccionada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void SubirImagenFStorage() {
        if (ImageUri != null) {
            String cluni = getIntent().getStringExtra("cluni");
            String imageName = cluni+".jpg"; // Puedes generar un nombre único
            StorageReference imageRef = mStoragref.child(imageName);

            // Sube la imagen al Storage
            imageRef.putFile(ImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Imagen subida con éxito, obtén la URL de descarga
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            urlimg = uri.toString();

                            // Ahora puedes guardar 'urlimg' en Firestore o realizar otras operaciones
                            actualizarURLFirestore(urlimg);

                            // con la URL de la imagen

                            Toast.makeText(this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Maneja errores al subir la imagen
                        Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
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
                        documentReference.update("url_imagen", urlimg)
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito al actualizar el campo en Firestore
                                    Toast.makeText(this, "URL de imagen actualizada en Firestore", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(this, publicaciones.class);
                                    startActivity(i);
                                })
                                .addOnFailureListener(e -> {
                                    // Maneja errores al actualizar el campo en Firestore
                                    Toast.makeText(this, "Error al actualizar la URL de la imagen en Firestore", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Maneja errores al realizar la consulta en Firestore
                    Toast.makeText(this, "Error al consultar Firestore", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}