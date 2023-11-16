package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class editar_osc extends AppCompatActivity {

    ImageButton regresar;
    Button guardar;
    TextView nombre_osc;
    EditText emailtxt, pass, tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_osc);
        regresar = (ImageButton) findViewById(R.id.btn_back);
        guardar = (Button) findViewById(R.id.btnguardar);
        nombre_osc = (TextView) findViewById(R.id.txtnombre);
        emailtxt = (EditText) findViewById(R.id.txtemail);
        tel = (EditText) findViewById(R.id.txttel);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore mFirestore =  FirebaseFirestore.getInstance();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            emailtxt.setText(email);

            mFirestore.collection("organizaciones").whereEqualTo("correo",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("editar_osc ->", document.getId() + " => " + document.getData());
                            String nombreosc = document.getString("nombre");
                            String telosc = document.getString("telefono");
                            nombre_osc.setText(nombreosc);
                            tel.setText(telosc);
                            if(emailtxt.getText().toString() == user.getEmail().toString()){
                                Log.d("perfil_osc->", "Email no modificado");
                            }else{
                                user.updateEmail(emailtxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("perfil_osc->", "Update email success.");

                                        startActivity(new Intent(editar_osc.this, perfil_osc.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("perfil_osc->", "Update email not success.");
                                    }
                                });
                            }
                            if(telosc == tel.getText().toString()){
                                Log.d("perfil_osc->", "Telefono no modificado");
                            }else {
                                mFirestore.collection("organizaciones").document(document.getId()).update("telefono", tel.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.e("perfil_osc->", "Update telefono success.");
                                            startActivity(new Intent(editar_osc.this, perfil_osc.class));
                                        }
                                    }
                                })
                               .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("perfil_osc->", ""+e);
                                    }
                                });
                            }

                        }
                    } else {
                        Log.e("editar_osc ->", "Error obtentiendo documento: ", task.getException());
                    }

                }
            });

        }
    }
}