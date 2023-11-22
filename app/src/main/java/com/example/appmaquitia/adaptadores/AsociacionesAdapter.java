package com.example.appmaquitia.adaptadores;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.R;
import com.example.appmaquitia.modelos.Asociacion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.checkerframework.checker.units.qual.A;

public class AsociacionesAdapter extends FirestoreRecyclerAdapter<Asociacion,AsociacionesAdapter.ViewHolder> implements Asociacioninterface {
    private final Asociacioninterface asociacioninterface;
    private Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AsociacionesAdapter(@NonNull FirestoreRecyclerOptions<Asociacion> options, Asociacioninterface asociacioninterface, Context context) {
        super(options);
        this.asociacioninterface = asociacioninterface;
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Asociacion Asociacion) {
        holder.nombre.setText(Asociacion.getNombre());
        holder.municipio.setText(Asociacion.getMunicipio());
        if(Asociacion.getDescripcion().equals("")) {
            holder.descripcion.setText(Asociacion.getActividades());
        }else {
            holder.descripcion.setText(Asociacion.getDescripcion());
        }
        if((Asociacion.getFoto()).equals("")) {
            Glide.with(context)
                    .load(R.drawable.base_perfil_foto_ong)
                    .override(250,250)
                    .transform(new RoundedCorners(50))
                    .into(holder.foto);
        }else {
            Glide.with(context)
                    .load(Asociacion.getFoto())
                    .override(250,250)
                    .transform(new RoundedCorners(50))
                    .into(holder.foto);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview,parent,false);
        return new ViewHolder(v, asociacioninterface);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, municipio, descripcion;
        ImageView foto;
        public ViewHolder(@NonNull View itemView, Asociacioninterface asociacioninterface) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_as);
            municipio = itemView.findViewById(R.id.municipio_as);
            descripcion = itemView.findViewById(R.id.descripcion);
            foto = itemView.findViewById(R.id.asociacion_logo);
            nombre.setMaxLines(2);
            nombre.setEllipsize(TextUtils.TruncateAt.END);
            descripcion.setMaxLines(3);
            descripcion.setEllipsize(TextUtils.TruncateAt.END);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (asociacioninterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            asociacioninterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}