package com.example.appmaquitia;

public class Asociacion {
    String nombre;
    String calle;
    String num_ext;
    String representantes;
    String telefono;
    public Asociacion(){}
    public Asociacion(String nombre, String calle, String num_ext, String representates, String telefono) {
        this.nombre = nombre;
        this.calle = calle;
        this.num_ext = num_ext;
        this.representantes = representates;
        this.telefono = telefono;
    }

    public String getNum_ext() {
        return num_ext;
    }

    public void setNum_ext(String num_ext) {
        this.num_ext = num_ext;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(String representantes) {
        this.representantes = representantes;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
