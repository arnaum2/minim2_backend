package edu.upc.dsa;

import edu.upc.dsa.models.ChatManager;
import edu.upc.dsa.models.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChatManagerTest {

    private ChatManager chatManager;

    @BeforeEach
    void setUp() {
        File f = new File("chat.json");
        if (f.exists()) f.delete();
        chatManager = ChatManager.getInstance();
        chatManager.initTestChat(); // Inicializa datos de prueba
    }

    @Test
    void testCrearChat() {
        chatManager.crearChat("NuevoChat");
        Set<String> chats = chatManager.getChats();
        assertTrue(chats.contains("NuevoChat"));
    }

    @Test
    void testEnviarMensaje() {
        chatManager.enviarMensaje("Lobby", "User", "Nuevo mensaje");
        List<Mensaje> mensajes = chatManager.getMensajes("Lobby");
        assertEquals(2, mensajes.size());
        assertEquals("Nuevo mensaje", mensajes.get(1).getContenido());
    }

    @Test
    void testEditarMensaje() {
        chatManager.editarMensaje("Lobby", 0, "Mensaje editado");
        List<Mensaje> mensajes = chatManager.getMensajes("Lobby");
        assertEquals("Mensaje editado", mensajes.get(0).getContenido());
    }

    @Test
    void testEliminarMensaje() {
        chatManager.eliminarMensaje("Lobby", 0);
        List<Mensaje> mensajes = chatManager.getMensajes("Lobby");
        assertEquals(0, mensajes.size());
    }

    @Test
    void testGetMensajesDeChatInexistente() {
        List<Mensaje> mensajes = chatManager.getMensajes("ChatInexistente");
        assertTrue(mensajes.isEmpty());
    }

    @Test
    void testCrearChatDuplicado() {
        chatManager.crearChat("Lobby");
        Set<String> chats = chatManager.getChats();
        assertEquals(2, chats.size()); // No debe duplicar el chat
    }

    @Test
    void testGetUltimosMensajes() {
        // Simula chat privado entre Pedro y Juan
        chatManager.crearChat("Pedro_Juan");
        chatManager.enviarMensaje("Pedro_Juan", "Pedro", "Hola Juan");
        chatManager.enviarMensaje("Pedro_Juan", "Juan", "¡Hola Pedro!");

        edu.upc.dsa.services.ChatService chatService = new edu.upc.dsa.services.ChatService();
        List<edu.upc.dsa.models.UltimoMensajeDTO> ultimos = chatService.getUltimosMensajes("Pedro");

        // Debe haber un resultado con Juan y el último mensaje de Juan
        assertEquals(1, ultimos.size());
        assertEquals("Juan", ultimos.get(0).getUsuario());
        assertEquals("¡Hola Pedro!", ultimos.get(0).getUltimoMensaje());
    }
}