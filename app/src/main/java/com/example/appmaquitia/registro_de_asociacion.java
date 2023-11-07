package com.example.appmaquitia;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import com.google.common.hash.Hashing;
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
import java.nio.charset.StandardCharsets;
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
    AlertDialog.Builder alert;

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

        alert = new AlertDialog.Builder(registro_de_asociacion.this, R.style.AlertDialogStyle);
        registro.setEnabled(false);

        cluni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estadoBoton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estadoBoton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estadoBoton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estadoBoton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estadoBoton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    private void estadoBoton() {
        scluni = cluni.getText().toString();
        snombre = nombre.getText().toString().trim();
        semail = email.getText().toString().trim();
        spass = pass.getText().toString().trim();
        spassconfirm = passconfirm.getText().toString().trim();

        if(!scluni.isEmpty() && !snombre.isEmpty() && !semail.isEmpty() && !spass.isEmpty() && !spassconfirm.isEmpty()){
            registro.setBackgroundResource(R.drawable.btn_osc_data);
            registro.setEnabled(true);
        }else{
            registro.setBackgroundResource(R.drawable.btn_osc_register);
            registro.setEnabled(false);
        }
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

            try {
                doc = Jsoup.connect(url).get();
                if(!scluni.isEmpty()) {
                    if (!doc.body().text().isEmpty()) {
                        Elements table = doc.select("tbody");
                        Elements rows = table.get(1).select("tr");
                        for (Element row : rows) {
                            String n_osc = row.select("td").get(2).text();
                            String status = row.select("td").get(5).text();
                            String correo = row.select("td").get(11).text();
                            if(status.equals("Activa")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nombre.setText(n_osc);
                                        email.setText(correo);
                                        SpannableString textAlert = new SpannableString("La OSC con el cluni " + scluni + " fue encontrada!");
                                        int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                        textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        alert.setMessage(textAlert);
                                        AlertDialog dialog = alert.create();
                                        Window window = dialog.getWindow();
                                        window.setGravity(Gravity.TOP | Gravity.START);
                                        dialog.show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nombre.setText("");
                                        email.setText("");
                                        SpannableString textAlert = new SpannableString("La OSC no fue encontrada o esta inactiva");
                                        int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                        textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        alert.setMessage(textAlert);
                                        AlertDialog dialog = alert.create();
                                        Window window = dialog.getWindow();
                                        window.setGravity(Gravity.TOP | Gravity.START);
                                        dialog.show();
                                    }
                                });
                            }
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.setMessage("OSC no encontrada");
                                alert.show();
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpannableString textAlert = new SpannableString("Por favor, coloque un CLUNI");
                            int colorBlanco = ContextCompat.getColor(context, R.color.white);
                            textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            alert.setMessage(textAlert);
                            AlertDialog dialog = alert.create();
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.TOP | Gravity.START);
                            dialog.show();
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
                            if(spass.equals(spassconfirm)) {
                                if (status.equals("Activa")) {
                                    String hashed = Hashing.sha256()
                                            .hashString(spass, StandardCharsets.UTF_8)
                                            .toString();
                                    OSC datos = new OSC(cluni, n_osc, figura, rfc, status, representantes, correo, telefono, entidad, municipio, colonia, calle, num_ext,
                                            num_int, cp, actividades, "", "", "", hashed);
                                    DocumentReference documentReference = mFirestore.collection("organizaciones").document(cluni);
                                    documentReference.set(datos)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    String documentId = documentReference.getId();
                                                    mAuth.createUserWithEmailAndPassword(correo, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    SpannableString textAlert = new SpannableString("Registro exitoso");
                                                                    int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                                                    textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                                    alert.setMessage(textAlert);
                                                                    AlertDialog dialog = alert.create();
                                                                    Window window = dialog.getWindow();
                                                                    window.setGravity(Gravity.TOP | Gravity.START);
                                                                    dialog.show();
                                                                    i = new Intent(registro_de_asociacion.this, MainActivity.class);
                                                                    startActivity(i);
                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    SpannableString textAlert = new SpannableString("Ups! Algo ocurrio durante el registro");
                                                                    int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                                                    textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                                    alert.setMessage(textAlert);
                                                                    AlertDialog dialog = alert.create();
                                                                    Window window = dialog.getWindow();
                                                                    window.setGravity(Gravity.TOP | Gravity.START);
                                                                    dialog.show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            SpannableString textAlert = new SpannableString("Ups! Algo ocurrio durante el registro");
                                                            int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                                            textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            alert.setMessage(textAlert);
                                                            AlertDialog dialog = alert.create();
                                                            Window window = dialog.getWindow();
                                                            window.setGravity(Gravity.TOP | Gravity.START);
                                                            dialog.show();
                                                        }
                                                    });
                                                }
                                            });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alert.setMessage("La OSC no está activa");
                                            alert.show();
                                        }
                                    });
                                }
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SpannableString textAlert = new SpannableString("Las contraseñas deben ser iguales");
                                        int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                        textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        alert.setMessage(textAlert);
                                        AlertDialog dialog = alert.create();
                                        Window window = dialog.getWindow();
                                        window.setGravity(Gravity.TOP | Gravity.START);
                                        dialog.show();
                                    }
                                });
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SpannableString textAlert = new SpannableString("Todos los campos deben ser llenados");
                                    int colorBlanco = ContextCompat.getColor(context, R.color.white);
                                    textAlert.setSpan(new ForegroundColorSpan(colorBlanco), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    alert.setMessage(textAlert);
                                    AlertDialog dialog = alert.create();
                                    Window window = dialog.getWindow();
                                    window.setGravity(Gravity.TOP | Gravity.START);
                                    dialog.show();
                                }
                            });
                        }
                    }
                }
            }catch (Exception e){
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }
}