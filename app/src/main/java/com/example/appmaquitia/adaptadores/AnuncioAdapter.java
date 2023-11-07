package com.example.appmaquitia.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmaquitia.PublicacionesActivity;
import com.example.appmaquitia.R;
import com.example.appmaquitia.modelos.Anuncio;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.ViewHolder> {
    private List<Anuncio> anuncios;
    private Context context;

    public AnuncioAdapter(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @NonNull
    @Override
    public AnuncioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publicaciones_template,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioAdapter.ViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);
        String cuerpo = anuncio.getCuerpo();
        String fecha_hora = anuncio.getFecha_hora();
        Glide.with(context)
                .load(anuncio.getImagen())
                .override(700,400)
                .into(holder.ivImagen);
        holder.bind(cuerpo, fecha_hora);
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCuerpo, tvFechaHora;
        ImageView ivImagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCuerpo = itemView.findViewById(R.id.tvCuerpo);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            ivImagen = itemView.findViewById(R.id.imgvImagen);

        }

        public void bind(String cuerpo, String fecha_hora) {
            tvCuerpo.setText(cuerpo);
            tvFechaHora.setText(fecha_hora.toString());
            //imagen.
        }
    }
}
