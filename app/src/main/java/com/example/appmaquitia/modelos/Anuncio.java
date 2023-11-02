package com.example.appmaquitia.modelos;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Anuncio {
    String cuerpo;
    String fecha_hora;
    String imagen;

    public Anuncio(){}

    public Anuncio(String cuerpo, String fecha_hora, String imagen) {
        this.cuerpo = cuerpo;
        this.fecha_hora = fecha_hora;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }
}
