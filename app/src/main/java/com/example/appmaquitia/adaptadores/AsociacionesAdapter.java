package com.example.appmaquitia.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmaquitia.interfaces.Asociacioninterface;
import com.example.appmaquitia.R;
import com.example.appmaquitia.modelos.Asociacion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AsociacionesAdapter extends FirestoreRecyclerAdapter<Asociacion,AsociacionesAdapter.ViewHolder> implements Asociacioninterface {
    private final Asociacioninterface asociacioninterface;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AsociacionesAdapter(@NonNull FirestoreRecyclerOptions<Asociacion> options, Asociacioninterface asociacioninterface) {
        super(options);
        this.asociacioninterface = asociacioninterface;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Asociacion Asociacion) {
        holder.nombre.setText(Asociacion.getNombre());
        holder.municipio.setText(Asociacion.getMunicipio());
        holder.representantes.setText(Asociacion.getRepresentantes());
        holder.telefono.setText(Asociacion.getTelefono());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, municipio, representantes, telefono;
        public ViewHolder(@NonNull View itemView, Asociacioninterface asociacioninterface) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_as);
            municipio = itemView.findViewById(R.id.municipio_as);
            representantes = itemView.findViewById(R.id.representante_as);
            telefono = itemView.findViewById(R.id.num_telefono_as);

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