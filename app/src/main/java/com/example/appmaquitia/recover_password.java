package com.example.appmaquitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appmaquitia.modelos.alertas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jsoup.parser.Tag;

public class recover_password extends AppCompatActivity {
    String email = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        ImageButton regresar = (ImageButton) findViewById(R.id.btn_back);
        EditText etCorreo = (EditText) findViewById(R.id.etCorreo);
        Button btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        mAuth = FirebaseAuth.getInstance();

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etCorreo.getText().toString();

                if (!email.isEmpty()) {
                    restablecer(email);
                } else {
                    alertas.alertWarning(recover_password.this, "Rellene todos los campos", 2000);
                }

            }
        });
    }

    public void restablecer(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    alertas.alertSuccess(recover_password.this, "El correo fue enviado correctamente", 2000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2500);
                } else {
                    alertas.alertFalied(recover_password.this, "Error al enviar el correo", 2000);
                }

            }
        });
    }

}