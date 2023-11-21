package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.HashMap;
import java.util.Map;

public class Detalleorganizacion extends AppCompatActivity {
    TextView tvNombre,tvDireccion,tvCluni,tvCorreo,tvRepresentantes,tvTelefonos,tvTopico;
    JustifiedTextView tvActividades, tvDescripcion;
    ImageButton btnRegresar,btnFavoritos,btnDonacion,btnChat;
    ImageView perfilFoto;
    String userId, documentId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference organizaciones = db.collection("organizaciones");
    String cluni,telefono,nombre,foto;
    FirebaseUser usuarioActual = mAuth.getCurrentUser();
    Map<String, Object> oscData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalleorganizacion);
        tvNombre = findViewById(R.id.tvNombre);
        tvActividades = (JustifiedTextView) findViewById(R.id.tvActividades);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvCluni = findViewById(R.id.tvCluni);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvDescripcion = (JustifiedTextView) findViewById(R.id.tvDescripcion);
        tvRepresentantes = findViewById(R.id.tvRepresentantes);
        tvTelefonos = findViewById(R.id.tvTelefonos);
        tvTopico = findViewById(R.id.tvTopico);
        btnRegresar = findViewById(R.id.btn_back);
        btnFavoritos = findViewById(R.id.btn_fav);
        perfilFoto = findViewById(R.id.ivPerfilImagen);
        btnChat = findViewById(R.id.btnChat);

        nombre = getIntent().getStringExtra("nombre");
        tvNombre.setText(nombre);
        String actividades = getIntent().getStringExtra("actividades");
        tvActividades.setText(actividades);
        String calle = getIntent().getStringExtra("calle");
        String colonia = getIntent().getStringExtra("colonia");
        String entidad = getIntent().getStringExtra("entidad");
        String municipio = getIntent().getStringExtra("municipio");
        String numero_ext = getIntent().getStringExtra("numero_ext");
        String cp = getIntent().getStringExtra("cp");
        tvDireccion.setText(calle + ", " + numero_ext + ", "+ colonia + ", " + cp + ", " + municipio + ", " + entidad + ".");
        cluni = getIntent().getStringExtra("cluni");
        tvCluni.setText(cluni);
        String correo = getIntent().getStringExtra("correo");
        tvCorreo.setText(correo);
        String descripcion = getIntent().getStringExtra("descripcion");
        if(descripcion.equals("")) {
            tvDescripcion.setText("Sin descripción...");
        }else {
            tvDescripcion.setText(descripcion);
        }
        String representante = getIntent().getStringExtra("representante");
        tvRepresentantes.setText(representante);
        telefono = getIntent().getStringExtra("numero");
        tvTelefonos.setText(telefono);
        String topico = getIntent().getStringExtra("topico");
        if(topico.equals("")){
            tvTopico.setText("Sin tópico...");
        }else {
            tvTopico.setText(topico);
        }
        foto = getIntent().getStringExtra("foto");
        if(!foto.equals("")) {

            Glide.with(Detalleorganizacion.this)
                    .load(foto)
                    .override(400,300)
                    .transform(new RoundedCorners(30))
                    .into(perfilFoto);
        }else {
            Glide.with(Detalleorganizacion.this)
                    .load(R.drawable.base_perfil_foto_ong)
                    .override(400,300)
                    .transform(new RoundedCorners(30))
                    .into(perfilFoto);
        }

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Detalleorganizacion.this, PublicacionesActivityUsuarios.class);
                i.putExtra("ID",cluni);
                i.putExtra("nombre",nombre);
                i.putExtra("telefonos",telefono);
                i.putExtra("foto",foto);
                startActivity(i);

            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

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
                                   btnFavoritos.setImageResource(R.drawable.favorito);
                               }else{
                                   btnFavoritos.setImageResource(R.drawable.nofavorito);
                               }
                           }
                        });
                    }
                }
            });
        }

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuarioActual != null) {
                    Boolean isFavSelected = sharedPreferences.getBoolean("favorito_" + cluni, false);
                    userId = usuarioActual.getUid();
                    DocumentReference userDocRef = db.collection("user").document(userId);
                    isFavSelected = !isFavSelected;
                    if (isFavSelected) {
                        btnFavoritos.setImageResource(R.drawable.favorito);
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
                                                    alertas.alertSuccess(Detalleorganizacion.this,document.getString("nombre") + " se agregó de favoritos",2000);

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
                        btnFavoritos.setImageResource(R.drawable.nofavorito);
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
                                                        alertas.alertSuccess(Detalleorganizacion.this,document.getString("nombre") + " se eliminó de favoritos",2000);
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
                        btnFavoritos.setImageResource(R.drawable.favorito);
                    } else {
                        btnFavoritos.setImageResource(R.drawable.nofavorito);
                    }

                }else{
                    alertas.alertWarning(Detalleorganizacion.this, "Debes registrarte para añadir a favoritos",2000);
                }
            }
        });

    }
}