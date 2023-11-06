package com.example.appmaquitia;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<OSC> publicaciones;

    public CustomAdapter(Context context, ArrayList<OSC> publicaciones) {
        this.context = context;
        this.publicaciones = publicaciones;
    }

    @Override
    public int getCount() {
        return publicaciones.size();

    }

    @Override
    public Object getItem(int position) {
        return publicaciones.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }else{
            holderView = (HolderView) convertView.getTag();

        }
        OSC list = publicaciones.get(position);
        //holderView.imagen_osc.setImageResource(list.getActividades());
        holderView.nombre_osc.setText(list.getNombre());

        return convertView;

    }

    private class HolderView{
        private final ImageView imagen_osc;
        private final TextView nombre_osc;

        public HolderView(View view){
            imagen_osc = view.findViewById(R.id.img_post);
            nombre_osc = view.findViewById(R.id.nombre_osc);

        }
    }
}