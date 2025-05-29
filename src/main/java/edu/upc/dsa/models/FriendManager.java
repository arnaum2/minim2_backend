package edu.upc.dsa.models;

import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FriendManager {

    private static FriendManager instance;
    private List<SolicitudAmistad> solicitudes;
    private static final String FILE = "amistades.json";

    private FriendManager() {
        solicitudes = new ArrayList<>();
        cargarSolicitudes();
    }

    public static synchronized FriendManager getInstance() {
        if (instance == null) instance = new FriendManager();
        return instance;
    }

    public void enviarSolicitud(String de, String para) {
        if (de.equals(para)) return;
        if (yaExisteSolicitud(de, para)) return;

        solicitudes.add(new SolicitudAmistad(de, para));
        guardarSolicitudes();
    }

    public void aceptarSolicitud(String de, String para) {
        for (SolicitudAmistad s : solicitudes) {
            if (s.getDe().equals(de) && s.getPara().equals(para)) {
                s.setAceptada(true);
                break;
            }
        }
        guardarSolicitudes();
    }

    public void rechazarSolicitud(String de, String para) {
        solicitudes.removeIf(s -> s.getDe().equals(de) && s.getPara().equals(para) && !s.isAceptada());
        guardarSolicitudes();
    }

    public List<String> getAmigos(String usuario) {
        Set<String> amigos = new HashSet<>();
        for (SolicitudAmistad s : solicitudes) {
            if (s.isAceptada()) {
                if (s.getDe().equals(usuario)) amigos.add(s.getPara());
                if (s.getPara().equals(usuario)) amigos.add(s.getDe());
            }
        }
        return new ArrayList<>(amigos);
    }

    public boolean sonAmigos(String u1, String u2) {
        for (SolicitudAmistad s : solicitudes) {
            if (s.isAceptada() &&
                    ((s.getDe().equals(u1) && s.getPara().equals(u2)) ||
                            (s.getDe().equals(u2) && s.getPara().equals(u1)))) {
                return true;
            }
        }
        return false;
    }

    public List<SolicitudAmistad> getSolicitudesRecibidas(String usuario) {
        List<SolicitudAmistad> pendientes = new ArrayList<>();
        for (SolicitudAmistad s : solicitudes) {
            if (s.getPara().equals(usuario) && !s.isAceptada()) pendientes.add(s);
        }
        return pendientes;
    }

    public List<SolicitudAmistad> getSolicitudesEnviadas(String usuario) {
        List<SolicitudAmistad> enviadas = new ArrayList<>();
        for (SolicitudAmistad s : solicitudes) {
            if (s.getDe().equals(usuario) && !s.isAceptada()) enviadas.add(s);
        }
        return enviadas;
    }

    // NUEVO: Devuelve el conjunto de usuarios que han enviado solicitud pendiente a 'usuario'
    public Set<String> getSolicitudesPendientes(String usuario) {
        Set<String> pendientes = new HashSet<>();
        for (SolicitudAmistad s : solicitudes) {
            if (s.getPara().equals(usuario) && !s.isAceptada()) {
                pendientes.add(s.getDe());
            }
        }
        return pendientes;
    }

    private boolean yaExisteSolicitud(String de, String para) {
        for (SolicitudAmistad s : solicitudes) {
            if ((s.getDe().equals(de) && s.getPara().equals(para)) ||
                    (s.getDe().equals(para) && s.getPara().equals(de))) {
                return true;
            }
        }
        return false;
    }

    private void guardarSolicitudes() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FILE), this.solicitudes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarSolicitudes() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(FILE);
            if (file.exists()) {
                this.solicitudes = Arrays.asList(mapper.readValue(file, SolicitudAmistad[].class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTestAmistades() {
        enviarSolicitud("Pedro", "Juan");
        aceptarSolicitud("Pedro", "Juan");

        enviarSolicitud("Laura", "Pedro");
        aceptarSolicitud("Laura", "Pedro");

        enviarSolicitud("Laura", "Marc");

        enviarSolicitud("Admin", "Pedro");
        aceptarSolicitud("Admin", "Pedro");

        guardarSolicitudes();
    }
}