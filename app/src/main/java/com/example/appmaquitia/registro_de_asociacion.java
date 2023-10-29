package com.example.appmaquitia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class registro_de_asociacion extends AppCompatActivity {
    EditText nombre, cluni, email, pass, passconfirm;
    Button registro;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Intent i;
    TextView titulo, tt;

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
        tt = findViewById(R.id.tt);
        String url = "http://www.sii.gob.mx/portal/?cluni=CCH15020720GQG&nombre=&acronimo=&rfc=&status_osc=&status_sancion=&figura_juridica=&estado=&municipio=&asentamiento=&cp=&rep_nombre=&rep_apaterno=&rep_amaterno=&num_notaria=&objeto_social=&red=&advanced=\n";
        new CLUNI();
    }

    private class CLUNI extends AsyncTask<Void, Void, Void> {
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
                    tt.setText(n_osc + estatus + r_legales);

                }
            } catch (Exception e) {
                Log.e("no encontrado", String.valueOf(e));
            }
            return null;
        }
    }
}