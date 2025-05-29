package edu.upc.dsa.models;

public class Mensaje {
    private String autor;
    private String contenido;
    private long timestamp;

    public Mensaje() {}

    public Mensaje(String autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
        this.timestamp = System.currentTimeMillis();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}