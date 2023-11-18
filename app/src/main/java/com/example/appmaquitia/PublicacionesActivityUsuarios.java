package com.example.appmaquitia;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appmaquitia.adaptadores.AnuncioAdapter;
import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.example.appmaquitia.databinding.ActivityPublicacionesUsuariosBinding;
import com.example.appmaquitia.modelos.Anuncio;
import com.example.appmaquitia.modelos.alertas;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicacionesActivityUsuarios extends AppCompatActivity {
    private ActivityPublicacionesUsuariosBinding b;
    String asociacionNombre, numeroONG, topicoONG,asociacionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesUsuariosBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);

        asociacionNombre = getIntent().getStringExtra("nombre");
        topicoONG = getIntent().getStringExtra("topic");
        numeroONG = getIntent().getStringExtra("numero");
        asociacionID = getIntent().getStringExtra("ID");
        b.tvNombre.setMaxLines(2);
        b.tvNombre.setEllipsize(TextUtils.TruncateAt.END);
        b.tvNombre.setText(asociacionNombre);

        cargarAnuncios(asociacionID);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 1000);
        b.btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumbersList(numeroONG);
            }
        });
        b.btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(this,perfilOrganizacion.class);
                //startActivity(i);
            }
        });
    }

    public void cargarAnuncios(String organizacionID) {
        //Esta función se encarga de traer todos los anuncios de cada organización y establece un escuchador para que el recyclerview de se actualicé automaticamente cada que
        //se agrega un nuevo anuncio, lo que ayuda a que el usuario no tenga que recargar la ventana
        //recibe como parametro el id de la organización para que el usuario decida cual chat verbu
        CollectionReference anunciosRef = FirebaseFirestore.getInstance().collection("organizaciones/"+organizacionID+"/anuncios");

        anunciosRef.orderBy("fecha_hora", Query.Direction.ASCENDING).addSnapshotListener((queryDocumentSnapshot, e) -> {
            //Manejo de errores
            if(e != null) {
                Log.e(TAG, "Error al escuchar los cambios de la collección: " + e);
                return;
            }
            if(queryDocumentSnapshot != null) {
                List<Anuncio> anuncios = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshot) {
                    String cuerpo = document.getString("cuerpo");
                    Timestamp fecha_hora = document.getTimestamp("fecha_hora");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String url_imagen = document.getString("imagen");
                    Date date = fecha_hora.toDate();
                    String fechaFormateada = dateFormat.format(date);
                    Anuncio anuncio = new Anuncio(cuerpo,fechaFormateada,url_imagen);
                    anuncios.add(anuncio);
                }
                RecyclerView recyclerView = b.rvPublicaciones;
                AnuncioAdapter adapter = new AnuncioAdapter(anuncios,PublicacionesActivityUsuarios.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
            }
        });
    }
    public void getNumbersList(String numeros) {
        Log.d("cadena base", "" + numeros);

        // Quitar caracteres indebidos dejando solo números y comas
        String pure = numeros.replaceAll("[^0-9,]", "");

        // Separar en arrays por la coma
        String[] array = pure.split(",");

        // Verificar si hay al menos un elemento en el array
        if (array.length > 0) {
            // Crear un LayoutInflater para inflar la vista personalizada
            LayoutInflater inflater = LayoutInflater.from(this);

            // Crear un contenedor para los elementos del array
            LinearLayout customDialogView = new LinearLayout(this);
            customDialogView.setPadding(50,50,50,50);
            customDialogView.setOrientation(LinearLayout.VERTICAL);

            // Agregar cada elemento del array como un TextView cliclable
            for (final String elemento : array) {
                TextView textView = new TextView(this);
                textView.setText(elemento);
                textView.setPadding(20,0,20,20);
                textView.setTextSize(20);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirWhatsapp(elemento);
                    }
                });

                // Agregar el TextView al contenedor
                customDialogView.addView(textView);
            }

            // Crear un AlertDialog utilizando la vista personalizada
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(customDialogView);
            alertDialogBuilder.setTitle("Selecciona un número");

            // Mostrar el AlertDialog
            alertDialogBuilder.show();
        } else {
            // Manejar el caso en el que no hay números después de la limpieza
            Log.d("cadena limpia", "No se encontraron números en la cadena proporcionada");
        }
    }



    public void abrirWhatsapp(String numeroDeTelefono) {
        String url = "https://api.whatsapp.com/send?phone=" + numeroDeTelefono;
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}