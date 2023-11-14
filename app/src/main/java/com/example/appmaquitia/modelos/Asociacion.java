package com.example.appmaquitia.modelos;

public class Asociacion{
    String actividades;
    String calle;
    String cluni;
    String colonia;
    String correo;
    String cp;
    String descripcion;
    String entidad;
    String foto;
    String municipio;
    String nombre;
    String num_ext;
    String representantes;
    String telefono;
    String topic;

    public Asociacion() {
    }

    public Asociacion(String actividades, String calle, String cluni, String colonia, String correo, String cp, String descripcion, String entidad, String foto, String municipio, String nombre, String num_ext, String representantes, String telefono, String topic) {
        this.actividades = actividades;
        this.calle = calle;
        this.cluni = cluni;
        this.colonia = colonia;
        this.correo = correo;
        this.cp = cp;
        this.descripcion = descripcion;
        this.entidad = entidad;
        this.foto = foto;
        this.municipio = municipio;
        this.nombre = nombre;
        this.num_ext = num_ext;
        this.representantes = representantes;
        this.telefono = telefono;
        this.topic = topic;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCluni() {
        return cluni;
    }

    public void setCluni(String cluni) {
        this.cluni = cluni;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNum_ext() {
        return num_ext;
    }

    public void setNum_ext(String num_ext) {
        this.num_ext = num_ext;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
