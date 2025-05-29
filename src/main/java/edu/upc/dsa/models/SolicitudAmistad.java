package edu.upc.dsa.models;

public class SolicitudAmistad {
    private String de;
    private String para;
    private boolean aceptada;

    public SolicitudAmistad() {}

    public SolicitudAmistad(String de, String para) {
        this.de = de;
        this.para = para;
        this.aceptada = false;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public void setAceptada(boolean aceptada) {
        this.aceptada = aceptada;
    }
}