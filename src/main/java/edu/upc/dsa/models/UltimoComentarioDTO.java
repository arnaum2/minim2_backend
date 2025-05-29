package edu.upc.dsa.models;

public class UltimoComentarioDTO {
    private String tema;
    private String ultimoComentario;

    public UltimoComentarioDTO() {}

    public UltimoComentarioDTO(String tema, String ultimoComentario) {
        this.tema = tema;
        this.ultimoComentario = ultimoComentario;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getUltimoComentario() {
        return ultimoComentario;
    }

    public void setUltimoComentario(String ultimoComentario) {
        this.ultimoComentario = ultimoComentario;
    }
}