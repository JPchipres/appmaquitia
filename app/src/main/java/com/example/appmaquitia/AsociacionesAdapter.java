package com.example.appmaquitia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AsociacionesAdapter extends FirestoreRecyclerAdapter<Asociacion,AsociacionesAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AsociacionesAdapter(@NonNull FirestoreRecyclerOptions<Asociacion> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Asociacion Asociacion) {
        holder.nombre.setText(Asociacion.getNombre());
        holder.calle.setText(Asociacion.getCalle());
        holder.num_ext.setText(Asociacion.getNum_ext());
        holder.representantes.setText(Asociacion.getRepresentantes());
        holder.telefono.setText(Asociacion.getTelefono());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView img,nombre, calle,num_ext, representantes, telefono;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_as);
            calle = itemView.findViewById(R.id.calle_as);
            num_ext = itemView.findViewById(R.id.num_ext_as);
            representantes = itemView.findViewById(R.id.representante_as);
            telefono = itemView.findViewById(R.id.num_telefono_as);
        }
    }
}