package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class pruebas extends AppCompatActivity {

    TextView detalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);
        detalles = findViewById(R.id.detalles);
        try {
            Document doc = Jsoup.connect("http://www.sii.gob.mx/portal/?cluni=CCH15020720GQG&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=").get();
            for (Element td : doc.select("td")){
                detalles.setText(td.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}