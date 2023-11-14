package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Detalleorganizacion extends AppCompatActivity {
    TextView nombre_as,actividad_as,calle_as,cluni_as,colonia_as,correo_as,cp_as,descripcion_as,entidad_as,municipio_as,num_ext, representante_as,telefono_as,topico_as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalleorganizacion);
        nombre_as = findViewById(R.id.nombre_as);
        actividad_as = findViewById(R.id.actividad_as);
        calle_as = findViewById(R.id.calle_as);
        cluni_as = findViewById(R.id.cluni_as);
        colonia_as = findViewById(R.id.colonia_as);
        correo_as = findViewById(R.id.correo_as);
        cp_as = findViewById(R.id.cp_as);
        descripcion_as = findViewById(R.id.descripcion_as);
        entidad_as = findViewById(R.id.entidad_as);
        municipio_as = findViewById(R.id.municipio_as);
        num_ext = findViewById(R.id.num_ext);
        representante_as = findViewById(R.id.representante_as);
        telefono_as = findViewById(R.id.telefono_as);
        topico_as = findViewById(R.id.topico_as);

        String nombre = getIntent().getStringExtra("nombre");
        nombre_as.setText(nombre);
        String actividades = getIntent().getStringExtra("actividades");
        actividad_as.setText(actividades);
        String calle = getIntent().getStringExtra("calle");
        calle_as.setText(calle);
        String cluni = getIntent().getStringExtra("cluni");
        cluni_as.setText(cluni);
        String colonia = getIntent().getStringExtra("colonia");
        colonia_as.setText(colonia);
        String correo = getIntent().getStringExtra("correo");
        correo_as.setText(correo);
        String cp = getIntent().getStringExtra("cp");
        cp_as.setText(cp);
        String descripcion = getIntent().getStringExtra("descripcion");
        descripcion_as.setText(descripcion);
        String entidad = getIntent().getStringExtra("entidad");
        entidad_as.setText(entidad);
        String municipio = getIntent().getStringExtra("municipio");
        municipio_as.setText(municipio);
        String numero_ext = getIntent().getStringExtra("numero_ext");
        num_ext.setText(numero_ext);
        String representante = getIntent().getStringExtra("representante");
        representante_as.setText(representante);
        String telefono = getIntent().getStringExtra("telefono");
        telefono_as.setText(telefono);
        String topico = getIntent().getStringExtra("topico");
        topico_as.setText(topico);




    }
}