package com.example.appmaquitia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;

import com.example.appmaquitia.R;
import com.example.appmaquitia.viewmodel.CheckoutViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.appmaquitia.databinding.ActivityCheckoutBinding;

/**
 * Checkout implementation for the app
 */
public class CheckoutActivity extends AppCompatActivity {

    TextView txtnombre, txtfecha;
    String nombre, userID, cluni, monto, fechaS;
    Integer dia, mes, y;
    Button price1, price2, price3, op1;
    ImageButton regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        nombre = getIntent().getStringExtra("nombre");
        userID = getIntent().getStringExtra("userID");
        cluni = getIntent().getStringExtra("ID");
        txtnombre = (TextView) findViewById(R.id.txtnom);
        txtfecha = (TextView) findViewById(R.id.txtfecha);
        price1 = (Button) findViewById(R.id.price1);
        price2 = (Button) findViewById(R.id.price2);
        price3 = (Button) findViewById(R.id.price3);
        op1 = (Button) findViewById(R.id.op1);
        regresar = (ImageButton) findViewById(R.id.btn_back);
        Calendar fecha = Calendar.getInstance();
            dia = fecha.get(Calendar.DAY_OF_MONTH);
            mes = fecha.get(Calendar.MONTH) + 1;
            y = fecha.get(Calendar.YEAR);
            fechaS = dia + "/" + mes + "/" + y;
        txtfecha.setText("Donante: "+nombre);
        txtnombre.setText("Fecha: "+fechaS);
        price1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setEnabled(true);
                price1.setSelected(true);
                price2.setSelected(false);
                price3.setSelected(false);
                monto = "10";

            }
        });
        price2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setEnabled(true);
                price1.setSelected(false);
                price2.setSelected(true);
                price3.setSelected(false);
                monto = "50";
            }
        });
        price3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setEnabled(true);
                price1.setSelected(false);
                price2.setSelected(false);
                price3.setSelected(true);
                monto = "100";
            }
        });
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckoutActivity.this, detallespago.class);
                i.putExtra("userID", userID);
                i.putExtra("ID",cluni);
                i.putExtra("nombre",nombre);
                i.putExtra("monto",monto);
                i.putExtra("fecha", fechaS);
                startActivity(i);
            }
        });


    }

}