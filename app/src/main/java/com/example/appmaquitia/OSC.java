package com.example.appmaquitia;

public class OSC {
    private String cluni;
    private String nombre;
    private String rfc;
    private String figura;
    private String status;
    private String representantes;
    private String correo;
    private String telefono;
    private String entidad;
    private String municipio;
    private String colonia;
    private String calle;
    private String num_ext;
    private String num_int;
    private String cp;
    private String actividades;
    private String descripcion;
    private String topic;

    public OSC(){

    }

    public String getCluni() {
        return cluni;
    }

    public void setCluni(String cluni) {
        this.cluni = cluni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        this.figura = figura;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(String representantes) {
        this.representantes = representantes;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNum_ext() {
        return num_ext;
    }

    public void setNum_ext(String num_ext) {
        this.num_ext = num_ext;
    }

    public String getNum_int() {
        return num_int;
    }

    public void setNum_int(String num_int) {
        this.num_int = num_int;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public OSC(String cluni, String nombre, String rfc, String figura, String status, String representantes, String correo, String telefono, String entidad, String municipio,
               String colonia, String calle, String num_ext, String num_int, String cp, String actividades, String descripcion, String topic){
        this.cluni = cluni;
        this.nombre = nombre;
        this.rfc = rfc;
        this.figura = figura;
        this.status = status;
        this.representantes = representantes;
        this.correo = correo;
        this.telefono = telefono;
        this.entidad = entidad;
        this.municipio = municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.num_ext = num_ext;
        this.num_int = num_int;
        this.cp = cp;
        this.actividades = actividades;
        this.descripcion = descripcion;
        this.topic = topic;
    }
}
