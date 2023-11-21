package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaquitia.modelos.Asociacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import java.util.HashMap;
import java.util.Map;

public class Detalleorganizacion extends AppCompatActivity {

    private static final int styleFav = R.drawable.bookmarked;
    private static final int styleUnfav = R.drawable.favorito;
    TextView nombre_as,actividad_as,calle_as,cluni_as,colonia_as,correo_as,cp_as,descripcion_as,entidad_as,municipio_as,num_ext, representante_as,telefono_as,topico_as;
    Button fav, donar;
    String userId, documentId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference organizaciones = db.collection("organizaciones");
    Map<String, Object> oscData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalleorganizacion);
        nombre_as = findViewById(R.id.nombre_as);
        actividad_as = findViewById(R.id.actividad_as);
        calle_as = findViewById(R.id.calle_as);
        cluni_as = findViewById(R.id.cluni_as);
        colonia_as = findViewById(R.id.colonia_as);
        correo_as = findViewById(R.id.correo_as);
        cp_as = findViewById(R.id.cp_as);
        descripcion_as = findViewById(R.id.descripcion_as);
        entidad_as = findViewById(R.id.entidad_as);
        municipio_as = findViewById(R.id.municipio_as);
        num_ext = findViewById(R.id.num_ext);
        representante_as = findViewById(R.id.representante_as);
        telefono_as = findViewById(R.id.telefono_as);
        topico_as = findViewById(R.id.topico_as);
        fav = findViewById(R.id.btn_fav);
        donar = findViewById(R.id.donar);

        String nombre = getIntent().getStringExtra("nombre");
        nombre_as.setText(nombre);
        String actividades = getIntent().getStringExtra("actividades");
        actividad_as.setText(actividades);
        String calle = getIntent().getStringExtra("calle");
        calle_as.setText(calle);
        String cluni = getIntent().getStringExtra("cluni");
        cluni_as.setText(cluni);
        String colonia = getIntent().getStringExtra("colonia");
        colonia_as.setText(colonia);
        String correo = getIntent().getStringExtra("correo");
        correo_as.setText(correo);
        String cp = getIntent().getStringExtra("cp");
        cp_as.setText(cp);
        String descripcion = getIntent().getStringExtra("descripcion");
        descripcion_as.setText(descripcion);
        String entidad = getIntent().getStringExtra("entidad");
        entidad_as.setText(entidad);
        String municipio = getIntent().getStringExtra("municipio");
        municipio_as.setText(municipio);
        String numero_ext = getIntent().getStringExtra("numero_ext");
        num_ext.setText(numero_ext);
        String representante = getIntent().getStringExtra("representante");
        representante_as.setText(representante);
        String telefono = getIntent().getStringExtra("telefono");
        telefono_as.setText(telefono);
        String topico = getIntent().getStringExtra("topico");
        topico_as.setText(topico);

        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Boolean isFavSelected = sharedPreferences.getBoolean("favorito_" + cluni, false);

        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if(usuarioActual != null){
            userId = usuarioActual.getUid();
            DocumentReference userDocRef = db.collection("user").document(userId);

            organizaciones.whereEqualTo("cluni", cluni).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        documentId = document.getId();

                        userDocRef.collection("favoritos").document(documentId).get().addOnCompleteListener(favTask -> {
                           if(favTask.isSuccessful()){
                               boolean isFav = favTask.getResult().exists();

                               if(isFav){
                                   fav.setBackgroundResource(styleFav);
                               }else{
                                   fav.setBackgroundResource(styleUnfav);
                               }
                           }
                        });
                    }
                }
            });
        }
        donar.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                     }
                                 });

                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (usuarioActual != null) {
                            Boolean isFavSelected = sharedPreferences.getBoolean("favorito_" + cluni, false);
                            userId = usuarioActual.getUid();
                            DocumentReference userDocRef = db.collection("user").document(userId);
                            isFavSelected = !isFavSelected;
                            if (isFavSelected) {
                                fav.setBackgroundResource(styleFav);
                                editor.putBoolean("favorito_" + cluni, isFavSelected);
                                editor.apply();

                                oscData.put("nombre", nombre);
                                oscData.put("actividades", actividades);
                                oscData.put("calle", calle);
                                oscData.put("cluni", cluni);
                                oscData.put("colonia", colonia);
                                oscData.put("correo", correo);
                                oscData.put("cp", cp);
                                oscData.put("descripcion", descripcion);
                                oscData.put("entidad", entidad);
                                oscData.put("municipio", municipio);
                                oscData.put("num_ext", numero_ext);
                                oscData.put("representantes", representante);
                                oscData.put("telefono", telefono);
                                oscData.put("topico", topico);

                                organizaciones.whereEqualTo("cluni", cluni).get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            documentId = document.getId();
                                            //Toast.makeText(this, userId, Toast.LENGTH_LONG).show();
                                            userDocRef.collection("favoritos").document(documentId).set(oscData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    }
                                });
                            } else {
                                fav.setBackgroundResource(styleUnfav);
                                editor.putBoolean("favorito_" + cluni, isFavSelected);
                                editor.apply();

                                organizaciones.whereEqualTo("cluni", cluni).get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            documentId = document.getId();
                                            if (documentId != null) {
                                                db.collection("user").document(userId).collection("favoritos").document(documentId)
                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                //Eliminado exitosamente
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //Error en la eliminación
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                            }

                            if (isFavSelected) {
                                fav.setBackgroundResource(styleFav);
                            } else {
                                fav.setBackgroundResource(styleUnfav);
                            }

                        } else {
                            //usuarios no validado
                        }
                    }
                });

    }
}