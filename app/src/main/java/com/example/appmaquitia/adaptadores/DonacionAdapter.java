package com.example.appmaquitia.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appmaquitia.R;
import com.example.appmaquitia.modelos.Transaccion;

import java.util.ArrayList;

public class DonacionAdapter extends ArrayAdapter<Transaccion> {
    public DonacionAdapter(@NonNull Context context, ArrayList<Transaccion> dataModalArrayList){
        super(context, 0, dataModalArrayList);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_donaciones, parent, false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Transaccion dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView oscTV = listitemView.findViewById(R.id.correo);
        TextView montoTV = listitemView.findViewById(R.id.monto);
        TextView fechaTV = listitemView.findViewById(R.id.fecha);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        oscTV.setText("Correo: "+dataModal.getUserName());
        montoTV.setText("Monto donado: $"+dataModal.getMonto());
        fechaTV.setText("Fecha: "+dataModal.getFecha());
        // in below line we are using Picasso to
        // below line is use to add item click listener
        // for our item of list view.
        /*listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Item clicked is : " + dataModal.getName(), Toast.LENGTH_SHORT).show();
            }
        });*/
        return listitemView;
    }
}
