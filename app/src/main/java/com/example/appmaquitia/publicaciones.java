package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;




public class publicaciones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<OSC> publicaciones;
    ListView Lista;
    EditText nombreosc;
    ImageView imgosc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_inicio);
        Lista = findViewById(R.id.cardviewlist);
        publicaciones = setNombreYImagen();
        CustomAdapter adapter = new CustomAdapter(this, publicaciones);
        Lista.setAdapter(adapter);
        Lista.setOnItemClickListener(this);
    }
    private ArrayList<OSC> setNombreYImagen(){
        publicaciones = new ArrayList<>();
        //publicaciones.add(new OSC(R.drawable.promo_asociacion, "Casa hogar"));

        return publicaciones;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}