package com.example.appmaquitia;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class registro_de_asociacion extends AppCompatActivity {
    EditText nombre, cluni, email, pass, passconfirm;
    String snombre, scluni, semail, spass, spassconfirm;
    Button registro, busqueda;
    Button terms;
    ImageButton regresar;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    TextView titulo;
    CheckBox checkBoxTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_asociacion);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.contrasena);
        cluni = findViewById(R.id.cluni);
        passconfirm = findViewById(R.id.confirmacion);
        registro = (Button) findViewById(R.id.registroAsociacion);
        busqueda = (Button) findViewById(R.id.busquedaDatos);
        titulo = findViewById(R.id.titulo);
        regresar = (ImageButton) findViewById(R.id.btn_back);
        terms = (Button) findViewById(R.id.btn_terms);
        checkBoxTerms = findViewById(R.id.cb_terminos);

        nombre.setFocusable(false);
        nombre.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);
        registro.setEnabled(false);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(registro_de_asociacion.this);
                View customLayout = inflater.inflate(R.layout.dialog_custom_terms, null);
                TextView messageTerms = customLayout.findViewById(R.id.txt_viewMessageTerms);
                AlertDialog.Builder builder = new AlertDialog.Builder(registro_de_asociacion.this, R.style.AlertDialogTerms);
                builder.setTitle("Politicas y términos");
                String text_terms = getResources().getString(R.string.txt_terms2);
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
                scluni = cluni.getText().toString();
                if (!scluni.isEmpty()) {
                    new DATOS().execute();
                }else {
                    alertas.alertWarning(registro_de_asociacion.this,"Por favor ingrese un CLUNI",2000);
                }
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass.setError(null);
                passconfirm.setError(null);

                final String regexpass = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,63}$";
                String passs = pass.getText().toString().trim();
                String passsconfirm = passconfirm.getText().toString().trim();
                if (!passs.matches(regexpass)) {
                    pass.setError("La contraseña debe tener 8 dígitos, al menos un número, un caractere, letras mayúsculas y minúsculas.");
                    pass.requestFocus();
                } else if (!passs.equals(passsconfirm)) {

                    pass.setError("Las contraseñas coinciden.");
                    pass.requestFocus();
                    passconfirm.setError("Las contraseñas no coinciden.");
                    passconfirm.requestFocus();
                }else {
                    new LECTURA().execute();
                }

            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void estadoBoton() {
        scluni = cluni.getText().toString();
        snombre = nombre.getText().toString().trim();
        semail = email.getText().toString().trim();
        spass = pass.getText().toString().trim();
        spassconfirm = passconfirm.getText().toString().trim();

        if(!scluni.isEmpty() && !snombre.isEmpty() && !semail.isEmpty() && !spass.isEmpty() && !spassconfirm.isEmpty()){
            checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    registro.setEnabled(isChecked);
                    if(isChecked){
                        registro.setTextColor(getResources().getColor(R.color.white));
                        registro.setBackgroundResource(R.drawable.button_ini);
                    }else{
                        registro.setTextColor(getResources().getColor(R.color.white));
                        registro.setBackgroundResource(R.drawable.button_not_ini);
                    }
                }
            });
            //registro.setEnabled(true);
            busqueda.setEnabled(false);
            //registro.setBackgroundResource(R.drawable.button_ini);
            busqueda.setBackgroundResource(R.drawable.button3_style);
            busqueda.setTextColor(getResources().getColor(R.color.buttons));

        }else{
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

            try {
                Log.d("Registro asociacion ->", "try entry");
                doc = Jsoup.connect(url).get();
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
                                    nombre.setText("");
                                    email.setText("");
                                    nombre.setText(n_osc);
                                    email.setText(correo);
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nombre.setText("");
                                    email.setText("");
                                    alertas.alertWarning(registro_de_asociacion.this,"La ONG está inactiva", 2000);
                                }
                            });
                        }
                    }
                }
            }catch (Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertas.alertFalied(registro_de_asociacion.this,"Pruebe con un CLUNI válido",2000);
                    }
                });
            }
            return null;
        }
    }

    private class LECTURA extends AsyncTask<Void, Void, Void>{

        protected Void doInBackground(Void... voids){

            Document doc = null;
            String url = "http://www.sii.gob.mx/portal/?cluni=" + scluni + "&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";

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
                        Integer posicion = semail.indexOf(',');
                        String nEmail = (posicion != -1) ? semail.substring(0,posicion) : semail;
                        if(spass.equals(spassconfirm)) {
                                if (status.equals("Activa")) {
                                    String hashed = Hashing.sha256()
                                            .hashString(spass, StandardCharsets.UTF_8)
                                            .toString();
                                    String rol = "osc";
                                    OSC datos = new OSC(cluni, n_osc, figura, rfc, status, representantes, nEmail, telefono, entidad, municipio, colonia, calle, num_ext,
                                            num_int, cp, actividades, "", "", "", hashed, false, rol);

                                    DocumentReference documentReference = mFirestore.collection("organizaciones").document(cluni);
                                    documentReference.get().addOnCompleteListener(task ->  {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documento = task.getResult();
                                            if (!documento.exists()) {
                                                documentReference.set(datos)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                String documentId = documentReference.getId();
                                                                mAuth.createUserWithEmailAndPassword(nEmail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                alertas.alertSuccess(registro_de_asociacion.this,"¡Resgitro creado con éxito!",2000);
                                                                                new Handler().postDelayed(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        finish();
                                                                                        startActivity(new Intent(registro_de_asociacion.this, login_osc.class));

                                                                                    }
                                                                                }, 2500);
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
                                                                        alertas.alertWarning(registro_de_asociacion.this,"¡Ups Algo salió mal durante el registro!",2000);
                                                                    }
                                                                });
                                                            }
                                                        });
                                            }else {
                                                alertas.alertFalied(registro_de_asociacion.this,"La ONG se encuentra ya registrada",2000);
                                            }
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertas.alertFalied(registro_de_asociacion.this,"Intenta con una ONG activa",2000);
                                        }
                                    });
                                }
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