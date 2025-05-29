package edu.upc.dsa.models;

public class UsuReg {

    private String id;
    private String name;
    private String apellidos;
    private String pswd;
    private String mail;
    private String pregunta;
    private String respuesta;

    public UsuReg() {}
    public UsuReg(String id, String name, String apellidos,String pswd, String mail, String q, String a) {
        this.id = id;
        this.name = name;
        this.apellidos = apellidos;
        this.pswd = pswd;
        this.mail = mail;
        this.pregunta = q;
        this.respuesta = a;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getApellidos() {
        return apellidos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPswd() {
        return pswd;
    }

    public String getMail() {
        return mail;
    }
}
