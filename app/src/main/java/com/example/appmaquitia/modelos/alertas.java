package com.example.appmaquitia.modelos;

import android.content.Context;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.appmaquitia.R;
import com.example.appmaquitia.registro;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import android.content.DialogInterface;
import android.widget.Toast;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class alertas {

    String texto;
    Integer duracion = 0;
    Context context;
    //Uri imagen;


    public static void alertSuccess(Context context, String texto, Integer duracion) {

       SpannableString textAlert = new SpannableString(texto);

       AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustomSuccess);

       int color =  ContextCompat.getColor(context, R.color.white);

       textAlert.setSpan(new ForegroundColorSpan(color), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       alert.setMessage(textAlert);
       AlertDialog dialog = alert.create();
       Window window = dialog.getWindow();
       window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
       dialog.show();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if (dialog.isShowing()) {
                   dialog.dismiss();
               }
           }
       }, duracion);
   }
    public static void alertFalied(Context context, String texto, Integer duracion) {
       SpannableString textAlert = new SpannableString(texto);

       AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustomFailed);

       int color =  ContextCompat.getColor(context, R.color.white);

       textAlert.setSpan(new ForegroundColorSpan(color), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       alert.setMessage(textAlert);
       AlertDialog dialog = alert.create();
       Window window = dialog.getWindow();
       window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
       dialog.show();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               if (dialog.isShowing()) {
                   dialog.dismiss();
               }
           }
       }, duracion);
   }
    public static void alertWarning(Context context, String texto, Integer duracion) {
        SpannableString textAlert = new SpannableString(texto);

        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustomWarning);

        int color =  ContextCompat.getColor(context, R.color.black);

        textAlert.setSpan(new ForegroundColorSpan(color), 0, textAlert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alert.setMessage(textAlert);
        AlertDialog dialog = alert.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, duracion);
    }



}
