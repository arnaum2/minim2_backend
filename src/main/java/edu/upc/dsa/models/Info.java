package edu.upc.dsa.models;

public class Info {

    private String usuario;
    private int mejorPuntuacion;
    private int numPartidas;

    public Info(){}

    public Info(String usuario, int mejorPuntuacion, int numPartidas) {
        this.usuario = usuario;
        this.mejorPuntuacion = mejorPuntuacion;
        this.numPartidas = numPartidas;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getNumPartidas() {
        return numPartidas;
    }

    public int getMejorPuntuacion() {
        return mejorPuntuacion;
    }
}
