package edu.upc.dsa.models;

public class OlvContra {

    private String id;
    private String respuesta;

    public OlvContra() {}

    public OlvContra(String id, String respuesta) {
        this.id = id;
        this.respuesta = respuesta;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    public String getId() {
        return id;
    }
    public String getRespuesta() {
        return respuesta;
    }

}
