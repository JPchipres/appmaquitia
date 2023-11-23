package com.example.appmaquitia.modelos;

import java.security.PrivateKey;

public class Transaccion {
    private String userID;
    private String oscID;
    private String nombre;
    private String userName;
    private String monto;
    private String fecha;
    public Transaccion(String userID, String oscID, String nombre, String monto, String fecha, String userName) {
        this.userID = userID;
        this.oscID = oscID;
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Transaccion(){

    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOscID() {
        return oscID;
    }

    public void setOscID(String oscID) {
        this.oscID = oscID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }





}
