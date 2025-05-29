package edu.upc.dsa;

import edu.upc.dsa.models.FriendManager;
import edu.upc.dsa.models.ChatManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.models.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FriendServiceTest {

    @BeforeEach
    void setUp() {
        // Limpia archivos y reinicia singletons
        new File("amistades.json").delete();
        new File("chat.json").delete();
        FriendManager.getInstance().initTestAmistades();
        ChatManager.getInstance().initTestChat();

        // Simula usuarios en GameManagerImpl
        GameManagerImpl gm = (GameManagerImpl) GameManagerImpl.getInstance();
        gm.getUsuarios().clear();
        gm.getUsuarios().put("Pedro", new Usuario());
        gm.getUsuarios().put("Juan", new Usuario());
        gm.getUsuarios().put("Laura", new Usuario());
        gm.getUsuarios().put("Marc", new Usuario());
        gm.getUsuarios().put("Admin", new Usuario());
    }

    @Test
    void testGetUsuarios() {
        GameManagerImpl gm = (GameManagerImpl) GameManagerImpl.getInstance();
        Set<String> usuarios = gm.getUsuarios().keySet();
        assertTrue(usuarios.contains("Pedro"));
        assertTrue(usuarios.contains("Juan"));
        assertTrue(usuarios.contains("Laura"));
        assertTrue(usuarios.contains("Marc"));
        assertTrue(usuarios.contains("Admin"));
    }

    @Test
    void testGetRecientes() {
        ChatManager cm = ChatManager.getInstance();
        // Simula conversación entre Pedro y Juan en "Lobby"
        cm.enviarMensaje("Lobby", "Pedro", "Hola Juan");
        cm.enviarMensaje("Lobby", "Juan", "Hola Pedro");

        // Lógica del endpoint
        Set<String> recientesPedro = new HashSet<>();
        for (String chat : cm.getChats()) {
            List<edu.upc.dsa.models.Mensaje> mensajes = cm.getMensajes(chat);
            boolean usuarioHaHablado = mensajes.stream().anyMatch(m -> m.getAutor().equals("Pedro"));
            if (usuarioHaHablado) {
                for (edu.upc.dsa.models.Mensaje m : mensajes) {
                    if (!m.getAutor().equals("Pedro")) {
                        recientesPedro.add(m.getAutor());
                    }
                }
            }
        }
        assertTrue(recientesPedro.contains("Juan"));
    }
}