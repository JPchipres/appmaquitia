package com.example.appmaquitia;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.example.appmaquitia.adaptadores.AnuncioAdapter;
import com.example.appmaquitia.databinding.ActivityPublicacionesBinding;
import com.example.appmaquitia.modelos.Anuncio;
import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.view.LayoutInflater;
import java.util.Map;

import android.content.DialogInterface;
import java.util.Random;

//ESTA ES LA VISTA DE LAS ORGANIZACIONES PARA CREAR ANUNCIOS
public class PublicacionesActivity extends AppCompatActivity {
    private ActivityPublicacionesBinding b;
    Uri imageuri;
    private static final int SELECT_PICTURE = 1;
    private boolean imagenCargada  = false;
    private String asociacionID = "";
    private String topicoONG = "";
    private String numeroONG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPublicacionesBinding.inflate(getLayoutInflater());
        View v = b.getRoot();
        setContentView(v);


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
                finish();//mata este activity y regresa al activity que lo lanzó inicialmente
            }
        });
        b.btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirWhatsapp("3141438337");
            }
        });
        b.btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(this,perfilOrganizacion.class);
                //startActivity(i);
            }
        });
        b.btnEnviarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagenCargada) {
                    subirImagen(imageuri,asociacionID);
                }else {
                    if(!b.etNuevoAnuncio.getText().toString().equals("")) {
                        if(!b.etNuevoAnuncio.getText().toString().startsWith(" ") && !b.etNuevoAnuncio.getText().toString().startsWith("\n")) {
                            crearAnuncio(asociacionID, "");
                        }
                    }
                }
            }});
        b.btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagenCargada) {

                    eliminarImagen(imageuri);
                }else{
                    cargarImagen();
                }
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
                AnuncioAdapter adapter = new AnuncioAdapter(anuncios,PublicacionesActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
            }
        });
    }
    public void abrirWhatsapp(String numeroDeTelefono) {
        String url = "https://api.whatsapp.com/send?phone=" + numeroDeTelefono;
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    public void crearAnuncio(String organizacionID, String imagen) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date fecha_hora= new Date();
        Timestamp ts = new Timestamp(fecha_hora);
        Map<String, Object> anuncio = new HashMap<>();
        anuncio.put("cuerpo", b.etNuevoAnuncio.getText().toString().trim());
        anuncio.put("fecha_hora", ts);
        anuncio.put("imagen", imagen);
        //esto es provicional debe cambiar
        db.collection("organizaciones")
                .document(organizacionID)
                .collection("anuncios")
                .document()
                .set(anuncio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                b.scrollView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        restablecerInput();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertas.alertFalied(PublicacionesActivity.this,"No se pudo crear el anuncio", 2000);
                    }
                });
    }
    public void cargarImagen() {
        Intent abrirGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(abrirGaleria,SELECT_PICTURE);
    }
    public void subirImagen(Uri imagenUri, String asociacionID) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Random rand = new Random();
        Integer randomNum = rand.nextInt(1000);
        StorageReference imagesRef = storageRef.child("imagenes/" + asociacionID + "/" + randomNum);
        imagesRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                if(uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadURI = uri.toString();
                            crearAnuncio(asociacionID, downloadURI);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertas.alertFalied(PublicacionesActivity.this,"Error: " + e, 2000);
                        }
                    });
                }
            }
        });
    }
    public void restablecerInput() {
        imageuri = null;
        imageuri = Uri.parse("");
        imagenCargada = false;
        b.etNuevoAnuncio.setText("");
        b.btnCargarImagen.setImageResource(R.drawable.imagen_no_adjuntada);
        b.btnCargarImagen.setEnabled(true);
    }
    public void eliminarImagen(Uri image) {

        SpannableString textAlert = new SpannableString("¿Desea eliminar la imagen seleccionada?");
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        int color = ContextCompat.getColor(this, R.color.black);
        textAlert.setSpan(new ForegroundColorSpan(color), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alert.setMessage(textAlert);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null);
        alert.setView(view);
        ImageView imageView = view.findViewById(R.id.image);

        // Cargar la imagen desde la URI usando Glide
        Glide.with(this)
                .load(image)
                .into(imageView);
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                imageuri = null;
                imagenCargada=false;
                b.btnCargarImagen.setImageResource(R.drawable.imagen_no_adjuntada);

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.buttons));
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.buttons));
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //DEL MÉTODO CARGAR IMAGEN
        if(resultCode == RESULT_OK && requestCode == SELECT_PICTURE && data != null) {
            imageuri = data.getData();
            b.btnCargarImagen.setImageResource(R.drawable.imagen_adjuntada);
            imagenCargada = true;
        }else {
            alertas.alertWarning(PublicacionesActivity.this, "Ninguna imagen fue seleccionada",2000);
        }
    }
}