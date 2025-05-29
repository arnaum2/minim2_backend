package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;

import java.util.List;

public interface GameManager {

    public void addUsuario(String id, String name, String ape,String contra, String mail, String q, String a) throws UsuarioYaRegistradoException;
    public UsuarioEnviar login(String mail_nombre, String pswd) throws CredencialesIncorrectasException;
    public ConsultaTienda findSkins();
    public ConsultaTienda findArmas();
    public DevolverCompra Comprar (Compra compra) throws UsuarioNoAutenticadoException, NoSuficientesTarrosException;
    public void addObjeto(Objeto objeto);
    public Objeto findObjeto(String idObjeto);
    public void initTestUsers() throws UsuarioYaRegistradoException;
    public String obtenerContra(String usuario) throws CredencialesIncorrectasException;
    public Usuario relogin(String id, String respuesta) throws CredencialesIncorrectasException;
    public void CambiarContra(String usuario, String contra) throws CredencialesIncorrectasException;
    public ConsultaTienda skinsUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public ConsultaTienda armasUsuario(String usuario) throws CredencialesIncorrectasException, NoHayObjetos;
    public void deleteUsuario(String id) throws UsuarioNoEncontradoException;
    public Intercambio intercambio(String usuario) throws CredencialesIncorrectasException, NoHayFlores;
    public InfoList rankingConPosicion(String userId);
    public void addIssue(String date, String titol, String informer, String message) throws MissingDataException;
    public List<Issue> llistaIssues();
}
