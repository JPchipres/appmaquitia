package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class perfil_osc extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    EditText nom_osc, email_osc,actividades,telefonos, descripcion, topico;
    Button editar, tyc, signout,guardar, resetpass;
    CircleImageView cambiarFoto;
    Intent i;
    BottomNavigationView bottomNavigationView;
    String  cluni,nombreosc, actividadess, telefonoss, topicc, descripcionn, foto;
    String name, description, topic, activities, phones;
    private static final String TAG = "perfil_osc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_osc);

        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        cambiarFoto = (CircleImageView) findViewById(R.id.btnCambiarFoto);
        nom_osc = (EditText) findViewById(R.id.etNombre);
        descripcion = (EditText) findViewById(R.id.etDescripcion);
        topico = (EditText) findViewById(R.id.etTopico);
        email_osc = (EditText) findViewById(R.id.etCorreo);
        actividades = (EditText) findViewById(R.id.etActividades);
        telefonos = (EditText) findViewById(R.id.etTelefono);
        guardar = (Button) findViewById(R.id.btnGuardarCambios);
        editar = (Button) findViewById(R.id.btnEditar);
        signout = (Button) findViewById(R.id.btnCerrarSesion);
        resetpass = (Button) findViewById(R.id.btnCambiarContraseña);
        bottomNavigationView = findViewById(R.id.navbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        guardar.setVisibility(View.GONE);
        cambiarFoto.setEnabled(false);

        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            email_osc.setText(email);
            mFirestore.collection("organizaciones").whereEqualTo("correo",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            nombreosc = document.getString("nombre");
                            cluni = document.getString("cluni");
                            actividadess = document.getString("actividades");
                            telefonoss = document.getString("telefono");
                            descripcionn = document.getString("descripcion");
                            topicc = document.getString("topic");
                            foto = document.getString("foto");
                            Glide.with(perfil_osc.this)
                                    .load(foto)
                                    .override(700,400)
                                    .transform(new RoundedCorners(30))
                                    .into(cambiarFoto);
                            topico.setText(topicc);
                            descripcion.setText(descripcionn);
                            nom_osc.setText(nombreosc);
                            actividades.setText(actividadess);
                            telefonos.setText(telefonoss);

                        }
                    } else {
                    Log.e(TAG, "Error al obtener el documento: ", task.getException());
                }

                }
            });

        }
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =  nom_osc.getText().toString().trim();
                description = descripcion.getText().toString().trim();
                topic = topico.getText().toString().trim();
                activities = actividades.getText().toString().trim();
                phones = telefonos.getText().toString().trim();
                if(!(name.length() > 10)){
                    alertas.alertWarning(perfil_osc.this,"Ingrese un nombre válido",1000);
                } else if (!(description.length() > 20)) {
                    alertas.alertWarning(perfil_osc.this, "Ingresa una descripcion de al menos 20 caracteres",1000);
                } else if (!(topic.length() > 10)) {
                    alertas.alertWarning(perfil_osc.this, "Ingresa un topico de al menos 10 caracteres",1000);
                } else if (!(activities.length() > 10)) {
                    alertas.alertWarning(perfil_osc.this, "Ingresa actividades",1000);
                } else if (!(phones.length() > 10)) {
                    alertas.alertWarning(perfil_osc.this, "Ingresa al menos un numero de telefono",1000);
                } else {
                    actualizar(cluni, name, description, activities, topic, phones);
                    nom_osc.setEnabled(false);
                    descripcion.setEnabled(false);
                    telefonos.setEnabled(false);
                    topico.setEnabled(false);
                    actividades.setEnabled(false);
                    guardar.setVisibility(View.GONE);
                }
            }
        });
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar.setVisibility(View.VISIBLE);
                nom_osc.setEnabled(true);
                descripcion.setEnabled(true);
                telefonos.setEnabled(true);
                topico.setEnabled(true);
                actividades.setEnabled(true);
                cambiarFoto.setEnabled(true);

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(perfil_osc.this, MainActivity.class));
            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            alertas.alertSuccess(perfil_osc.this, "El correo fue enviado correctamente", 2000);
                        } else {
                            alertas.alertFalied(perfil_osc.this, "Error al enviar el correo", 2000);
                        }
                    }
                });
            }
        });
        cambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(perfil_osc.this, registro_de_asociacion_imagen.class));
            }
        });
    }

    public void actualizar(String Documento, String Nombre,String Descripcion, String Actividades, String Topic, String Telefonos) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> nuevosDatos = new HashMap<>();
        nuevosDatos.put("nombre", Nombre);
        nuevosDatos.put("descripcion", Descripcion);
        nuevosDatos.put("actividades", Actividades);
        nuevosDatos.put("topic",Topic);
        nuevosDatos.put("telefono",Telefonos);

        db.collection("organizaciones").document(Documento).update(nuevosDatos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    alertas.alertSuccess(perfil_osc.this, "Los datos fueron actualizados con éxito",2000);
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            return false;
        } else if (item.getItemId() == R.id.chat) {
            Intent i = new Intent(perfil_osc.this, PublicacionesActivity.class);
            i.putExtra("ID",cluni);
            i.putExtra("nombre",nombreosc);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {

            return false;
        }
        return false;
    }
}