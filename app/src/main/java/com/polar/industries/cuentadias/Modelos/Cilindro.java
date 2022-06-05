package com.polar.industries.cuentadias.Modelos;

import java.util.Date;

public class Cilindro {
    private String id;
    private String dias_duracion;
    private String fecha_fin;
    private Date fecha_inicio;
    private String precio;
    private String size_cilindro;

    public Cilindro(Date fecha_inicio, String precio, String size_cilindro) {
        this.fecha_inicio = fecha_inicio;
        this.precio = precio;
        this.size_cilindro = size_cilindro;
    }


    public Cilindro(String id, String dias_duracion, String fecha_fin, Date fecha_inicio, String precio, String size_cilindro) {
        this.id = id;
        this.dias_duracion = dias_duracion;
        this.fecha_fin = fecha_fin;
        this.fecha_inicio = fecha_inicio;
        this.precio = precio;
        this.size_cilindro = size_cilindro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDias_duracion() {
        return dias_duracion;
    }

    public void setDias_duracion(String dias_duracion) {
        this.dias_duracion = dias_duracion;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getSize_cilindro() {
        return size_cilindro;
    }

    public void setSize_cilindro(String size_cilindro) {
        this.size_cilindro = size_cilindro;
    }
}
