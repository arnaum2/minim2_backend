package edu.upc.dsa.models;

import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatManager {

    private static ChatManager instance;
    private Map<String, List<Mensaje>> chats;
    private static final String CHAT_FILE = "chat.json";

    private ChatManager() {
        chats = new HashMap<>();
        cargarChat();
    }

    public static synchronized ChatManager getInstance() {
        if (instance == null) instance = new ChatManager();
        return instance;
    }

    public void crearChat(String nombre) {
        chats.putIfAbsent(nombre, new ArrayList<>());
        guardarChat();
    }

    public void enviarMensaje(String chat, String autor, String contenido) {
        Mensaje mensaje = new Mensaje(autor, contenido);
        chats.computeIfAbsent(chat, k -> new ArrayList<>()).add(mensaje);
        guardarChat();
    }

    public List<Mensaje> getMensajes(String chat) {
        return chats.getOrDefault(chat, new ArrayList<>());
    }

    public Set<String> getChats() {
        return chats.keySet();
    }

    private void guardarChat() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(CHAT_FILE), this.chats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarChat() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(CHAT_FILE);
            if (file.exists()) {
                // Crear JavaType para la clave y el valor
                com.fasterxml.jackson.databind.type.TypeFactory typeFactory = mapper.getTypeFactory();
                com.fasterxml.jackson.databind.type.MapType mapType = typeFactory.constructMapType(
                        HashMap.class,
                        typeFactory.constructType(String.class),
                        typeFactory.constructCollectionType(List.class, Mensaje.class)
                );
                this.chats = mapper.readValue(file, mapType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTestChat() {
        chats.clear(); // Limpia todos los chats y mensajes previos
        crearChat("Lobby");
        enviarMensaje("Lobby", "Admin", "¡Hola a todos!");
        crearChat("Soporte");
        enviarMensaje("Soporte", "Admin", "¿Alguien necesita ayuda?");
        guardarChat();
    }

    public void eliminarMensaje(String chat, int index) {
        List<Mensaje> mensajes = chats.get(chat);
        if (mensajes != null && index >= 0 && index < mensajes.size()) {
            mensajes.remove(index);
            guardarChat();
        }
    }

    public void editarMensaje(String chat, int index, String nuevoContenido) {
        List<Mensaje> mensajes = chats.get(chat);
        if (mensajes != null && index >= 0 && index < mensajes.size()) {
            mensajes.get(index).setContenido(nuevoContenido);
            guardarChat();
        }
    }
}