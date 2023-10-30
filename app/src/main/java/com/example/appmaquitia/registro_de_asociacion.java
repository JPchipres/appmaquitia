package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class registro_de_asociacion extends AppCompatActivity {
    EditText nombre, cluni, email, pass, passconfirm;
    String snombre, scluni, semail, spass, spassconfirm;
    Button registro, busqueda;
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
        registro = (Button) findViewById(R.id.registroAsociacion);
        busqueda = (Button) findViewById(R.id.busquedaDatos);
        titulo = findViewById(R.id.titulo);

        nombre.setFocusable(false);
        nombre.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);

        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DATOS().execute();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { new LECTURA().execute(); }
        });
    }

    private class DATOS extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void... voids){
            Document doc = null;
            scluni = cluni.getText().toString();
            try {
                scluni = URLEncoder.encode(scluni, "UTF-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();

            }
            String url = "http://www.sii.gob.mx/portal/?cluni=" + scluni + "&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";
            Context context = registro_de_asociacion.this;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(context, scluni, Toast.LENGTH_LONG).show();
                }
            });

            try {
                doc = Jsoup.connect(url).get();
                if(!scluni.isEmpty()) {
                    if (!doc.body().text().isEmpty()) {
                        Elements table = doc.select("tbody");
                        Elements rows = table.get(1).select("tr");
                        for (Element row : rows) {
                            String n_osc = row.select("td").get(2).text();
                            String correo = row.select("td").get(11).text();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nombre.setText(n_osc);
                                    email.setText(correo);
                                    //Toast.makeText(context, "Datos traidos", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "OSC no encontrada", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(context, "Coloque un CLUNI", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }
            return null;
        }
    }

    private class LECTURA extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void... voids){
            Document doc = null;
            String url = "http://www.sii.gob.mx/portal/?cluni=" + scluni + "&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";
            Context context = registro_de_asociacion.this;

            try {
                doc = Jsoup.connect(url).get();
                if(!doc.body().text().isEmpty()){
                    Elements table = doc.select("tbody");
                    Elements rows = table.get(1).select("tr");
                    for(Element row : rows){
                        String cluni = row.select("td").get(1).text();
                        String n_osc = row.select("td").get(2).text();
                        String figura = row.select("td").get(3).text();
                        String rfc = row.select("td").get(4).text();
                        String status = row.select("td").get(5).text();
                        String representantes = row.select("td").get(7).text();
                        String correo = row.select("td").get(11).text();
                        String telefono = row.select("td").get(12).text();
                        String entidad = row.select("td").get(13).text();
                        String municipio = row.select("td").get(14).text();
                        String colonia = row.select("td").get(15).text();
                        String calle = row.select("td").get(16).text();
                        String num_ext = row.select("td").get(17).text();
                        String num_int = row.select("td").get(18).text();
                        String cp = row.select("td").get(19).text();
                        String actividades = row.select("td").get(21).text();

                        snombre = nombre.getText().toString().trim();
                        semail = email.getText().toString().trim();
                        spass = pass.getText().toString().trim();
                        spassconfirm = passconfirm.getText().toString().trim();


                        if(!scluni.isEmpty() && !snombre.isEmpty() && !semail.isEmpty() && !spass.isEmpty() && !spassconfirm.isEmpty()){
                            if(status.equals("Activa")){
                                OSC datos = new OSC(cluni, n_osc, figura, rfc, status, representantes, correo, telefono, entidad, municipio, colonia, calle, num_ext,
                                        num_int, cp, actividades, "", "");
                                mFirestore.collection("organizaciones").add(datos)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                String documentId = documentReference.getId();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context, "Ocurrio un error en el registro", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });

                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "La OSC no está activa", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Todos los campos deben ser llenados", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(context, "OSC no encontrada", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (Exception e){
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }
}