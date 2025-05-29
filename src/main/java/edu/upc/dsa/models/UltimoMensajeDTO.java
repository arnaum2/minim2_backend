package edu.upc.dsa.models;

public class UltimoMensajeDTO {
    private String usuario;
    private String ultimoMensaje;

    public UltimoMensajeDTO() {}

    public UltimoMensajeDTO(String usuario, String ultimoMensaje) {
        this.usuario = usuario;
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}