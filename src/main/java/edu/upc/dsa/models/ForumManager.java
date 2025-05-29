package edu.upc.dsa.models;

import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ForumManager {

    private static ForumManager instance;
    private Map<String, List<Comentario>> temas;
    private static final String FORUM_FILE = "forum.json";

    private ForumManager() {
        temas = new HashMap<>();
        cargarForo();
    }

    public static synchronized ForumManager getInstance() {
        if (instance == null) instance = new ForumManager();
        return instance;
    }

    public void crearTema(String nombre) {
        temas.putIfAbsent(nombre, new ArrayList<>());
        guardarForo();
    }

    public void publicarComentario(String tema, String autor, String contenido) {
        Comentario comentario = new Comentario(autor, contenido);
        temas.computeIfAbsent(tema, k -> new ArrayList<>()).add(comentario);
        guardarForo();
    }

    public List<Comentario> getComentarios(String tema) {
        return temas.getOrDefault(tema, new ArrayList<>());
    }

    public Set<String> getTemas() {
        return temas.keySet();
    }

    private void guardarForo() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FORUM_FILE), this.temas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarForo() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(FORUM_FILE);
            if (file.exists()) {
                com.fasterxml.jackson.databind.type.TypeFactory typeFactory = mapper.getTypeFactory();
                com.fasterxml.jackson.databind.type.MapType mapType = typeFactory.constructMapType(
                        HashMap.class,
                        typeFactory.constructType(String.class),
                        typeFactory.constructCollectionType(List.class, Comentario.class)
                );
                this.temas = mapper.readValue(file, mapType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTestForum() {
        crearTema("General");
        publicarComentario("General", "Admin", "¡Bienvenidos al foro!");
        crearTema("Dudas");
        publicarComentario("Dudas", "Admin", "¿Cómo se juega?");
        guardarForo();
    }

    public void eliminarComentario(String tema, int index) {
        List<Comentario> comentarios = temas.get(tema);
        if (comentarios != null && index >= 0 && index < comentarios.size()) {
            comentarios.remove(index);
            guardarForo();
        }
    }

    public void editarComentario(String tema, int index, String nuevoContenido) {
        List<Comentario> comentarios = temas.get(tema);
        if (comentarios != null && index >= 0 && index < comentarios.size()) {
            comentarios.get(index).setContenido(nuevoContenido);
            guardarForo();
        }
    }

    public void votarComentario(String tema, int index, boolean positivo) {
        List<Comentario> comentarios = temas.get(tema);
        if (comentarios != null && index >= 0 && index < comentarios.size()) {
            if (positivo) comentarios.get(index).upvote();
            else comentarios.get(index).downvote();
            guardarForo();
        }
    }
    public static void resetInstance() {
        instance = null;
    }
}