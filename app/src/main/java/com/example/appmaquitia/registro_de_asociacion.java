package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registro_de_asociacion extends AppCompatActivity {
    EditText nombre, cluni, email, pass, passconfirm;
    Button registro;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Intent i;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_asociacion);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.contraseña);
        cluni = findViewById(R.id.cluni);
        passconfirm = findViewById(R.id.confirmacion);
        registro = findViewById(R.id.registroAsociacion);
        titulo = findViewById(R.id.titulo);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest postRequest = new StringRequest(Request.Method.GET, "http://www.sii.gob.mx/portal/?cluni=CCH15020720GQG&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        titulo.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(registro_de_asociacion.this).add(postRequest);
            }
        });
    }
}