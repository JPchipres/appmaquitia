package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class perfil_donador extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore firebasefirestore;
    FirebaseAuth mAuth;
    Button logout, resetpass, historial, terms;
    Intent i;
    BottomNavigationView bottomNavigationView;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_donador);
        CircleImageView ivPerfil = (CircleImageView) findViewById(R.id.imgperfil);
        Button btnEditar = (Button) findViewById(R.id.btnEditar);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        EditText etNombre = (EditText) findViewById(R.id.etNombre);
        EditText etCorreo = (EditText) findViewById(R.id.etCorreo);
        resetpass = (Button) findViewById(R.id.btnCambiarContrasena);
        logout = (Button) findViewById(R.id.btnCerrarSesion);
        historial = (Button) findViewById(R.id.btnHistorial);
        terms = (Button) findViewById(R.id.btnTerminos);
        btnGuardar.setEnabled(false);
        btnGuardar.setVisibility(View.GONE);
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            mFirestore.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        etNombre.setText(document.getString("name"));
                        etCorreo.setText(document.getString("email"));
                        id = document.getString("id");
                        Integer random = new Random().nextInt(3) + 1;
                        if(random == 1) {
                            ivPerfil.setImageResource(R.drawable.perfil1);
                        }else if (random == 2){
                            ivPerfil.setImageResource(R.drawable.perfil2);
                        } else {
                            ivPerfil.setImageResource(R.drawable.perfil3);
                        }

                    }
                }
            });
        }else{
            startActivity(new Intent(perfil_donador.this, MainActivity.class));
        }
        bottomNavigationView = findViewById(R.id.navbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombre.setEnabled(true);
                btnGuardar.setEnabled(true);
                btnGuardar.setVisibility(View.VISIBLE);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String regexname ="^([a-zA-ZáéíóúÁÉÍÓÚñÑ]+\\s){1,4}[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$";
                String nombre = etNombre.getText().toString().trim();
                if(!nombre.matches(regexname)) {
                    etNombre.setError("Ingresa un nombre válido.");
                    etNombre.requestFocus();
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> nuevosDatos = new HashMap<>();
                    nuevosDatos.put("name", nombre);
                    db.collection("user").document(id).update(nuevosDatos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            alertas.alertSuccess(perfil_donador.this,"Actualización exitosa",2000);
                            etNombre.setEnabled(false);
                            btnGuardar.setEnabled(false);
                            btnGuardar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(perfil_donador.this);
                View customLayout = inflater.inflate(R.layout.dialog_custom_terms, null);
                TextView messageTerms = customLayout.findViewById(R.id.txt_viewMessageTerms);
                AlertDialog.Builder builder = new AlertDialog.Builder(perfil_donador.this, R.style.AlertDialogTerms);
                builder.setTitle("Politicas y Términos");
                String text_terms = getResources().getString(R.string.txt_terms);
                messageTerms.setText(Html.fromHtml(text_terms));
                builder.setView(customLayout);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            alertas.alertSuccess(perfil_donador.this, "El correo fue enviado correctamente", 2000);
                        } else {
                            alertas.alertFalied(perfil_donador.this, "Error al enviar el correo", 2000);
                        }

                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                i = new Intent(perfil_donador.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            i = new Intent(perfil_donador.this, publicaciones.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.favs) {
            i = new Intent(perfil_donador.this, publicaciones_favoritos.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.perfil) {
            return false;
        }
        return false;
    }
}