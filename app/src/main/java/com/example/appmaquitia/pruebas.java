package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class pruebas extends AppCompatActivity {

    TextView detalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);
        detalles = findViewById(R.id.detalles);
        String url = "http://www.sii.gob.mx/portal/?cluni=CCH15020720GQG&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";
        new validate();
    }
        private class validate extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                Document doc = null;
                String url = "http://www.sii.gob.mx/portal/?cluni=CCH15020720GQG&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";
                try {
                    doc = Jsoup.connect(url).get();
                    Elements table1 = doc.select("tbody");
                    Elements rows = table1.get(1).select("tr");

                    for (Element row : rows){
                        String n_osc = row.select("td").get(2).text();
                        String estatus = row.select("td").get(5).text();
                        String r_legales = row.select("td").get(7).text();
                        String correos = row.select("td").get(11).text();
                        String telefonos = row.select("td").get(12).text();
                        detalles.setText(n_osc + estatus + r_legales);

                    }
                } catch (Exception e) {
                    Log.e("no encontrado", String.valueOf(e));
                }
                return null;
            }
    }
}