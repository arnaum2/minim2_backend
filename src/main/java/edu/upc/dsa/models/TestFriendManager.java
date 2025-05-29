package edu.upc.dsa.models;

import java.util.List;

public class TestFriendManager {
    public static void main(String[] args) {

        // 💬 1. TEST AMISTADES
        FriendManager fm = FriendManager.getInstance();
        System.out.println("📤 Enviando solicitud de Pedro a Juan...");
        fm.enviarSolicitud("Pedro", "Juan");

        System.out.println("📥 Juan acepta solicitud...");
        fm.aceptarSolicitud("Pedro", "Juan");

        System.out.println("🤝 ¿Son amigos Pedro y Juan? " + fm.sonAmigos("Pedro", "Juan"));
        System.out.println("👥 Amigos de Pedro: " + fm.getAmigos("Pedro"));

        // 💬 2. TEST CHAT
        ChatManager cm = ChatManager.getInstance();
        System.out.println("\n💬 Creando chat 'Lobby'...");
        cm.crearChat("Lobby");

        System.out.println("📨 Pedro envía mensaje a Lobby...");
        cm.enviarMensaje("Lobby", "Pedro", "¡Hola a todos!");

        System.out.println("📨 Juan responde...");
        cm.enviarMensaje("Lobby", "Juan", "Hola Pedro!");

        System.out.println("📜 Mensajes en 'Lobby':");
        List<Mensaje> mensajes = cm.getMensajes("Lobby");
        for (Mensaje m : mensajes) {
            System.out.println("[" + m.getAutor() + "] " + m.getContenido());
        }

        // 💬 3. TEST FORO
        ForumManager forum = ForumManager.getInstance();
        System.out.println("\n📄 Creando tema 'Dudas'...");
        forum.crearTema("Dudas");

        System.out.println("📝 Pedro publica una duda...");
        forum.publicarComentario("Dudas", "Pedro", "¿Cómo se pasa de nivel 1?");

        System.out.println("📝 Juan responde...");
        forum.publicarComentario("Dudas", "Juan", "Tienes que encontrar la llave.");

        System.out.println("👍 Votando respuesta de Juan...");
        forum.votarComentario("Dudas", 1, true); // índice 1 es el segundo comentario

        System.out.println("📜 Comentarios en 'Dudas':");
        List<Comentario> comentarios = forum.getComentarios("Dudas");
        for (Comentario c : comentarios) {
            System.out.println("[" + c.getAutor() + "] " + c.getContenido() + " (votos: " + c.getVotos() + ")");
        }
    }
}
