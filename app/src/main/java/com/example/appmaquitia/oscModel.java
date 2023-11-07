package com.example.appmaquitia;

import java.io.Serializable;

public class oscModel implements Serializable {


    Integer img_osc;
    String nombre_osc;
    public oscModel(Integer img_osc, String nombre_osc) {
        this.img_osc = img_osc;
        this.nombre_osc = nombre_osc;
    }

    public oscModel(){

    }
    public Integer getImg_osc() {
        return img_osc;
    }

    public void setImg_osc(Integer img_osc) {
        this.img_osc = img_osc;
    }

    public String getNombre_osc() {
        return nombre_osc;
    }

    public void setNombre_osc(String nombre_osc) {
        this.nombre_osc = nombre_osc;
    }


}
