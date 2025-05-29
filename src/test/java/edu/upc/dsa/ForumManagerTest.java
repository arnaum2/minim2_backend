package edu.upc.dsa;
import edu.upc.dsa.models.Comentario;
import edu.upc.dsa.models.ForumManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ForumManagerTest {

    private ForumManager forumManager;

    @BeforeEach
    void setUp() {
        java.io.File f = new java.io.File("forum.json");
        if (f.exists()) f.delete();
        ForumManager.resetInstance(); // Resetea la instancia para pruebas
        forumManager = ForumManager.getInstance();
        forumManager.initTestForum(); // Inicializa datos de prueba
    }

    @Test
    void testCrearTema() {
        forumManager.crearTema("NuevoTema");
        Set<String> temas = forumManager.getTemas();
        assertTrue(temas.contains("NuevoTema"));
    }

    @Test
    void testPublicarComentario() {
        forumManager.publicarComentario("Dudas", "Pedro", "¿Cómo se pasa de nivel?");
        List<Comentario> comentarios = forumManager.getComentarios("Dudas");
        assertEquals(2, comentarios.size());
        assertEquals("Pedro", comentarios.get(1).getAutor());
        assertEquals("¿Cómo se pasa de nivel?", comentarios.get(1).getContenido());
    }

    @Test
    void testEditarComentario() {
        forumManager.editarComentario("Dudas", 0, "Nueva duda");
        List<Comentario> comentarios = forumManager.getComentarios("Dudas");
        assertEquals("Nueva duda", comentarios.get(0).getContenido());
    }

    @Test
    void testEliminarComentario() {
        forumManager.eliminarComentario("Dudas", 0);
        List<Comentario> comentarios = forumManager.getComentarios("Dudas");
        assertEquals(0, comentarios.size());
    }

    @Test
    void testVotarComentario() {
        forumManager.votarComentario("Dudas", 0, true);
        List<Comentario> comentarios = forumManager.getComentarios("Dudas");
        assertEquals(1, comentarios.get(0).getVotos());
    }

    @Test
    void testGetUltimosComentarios() {
        // Añade comentarios a ambos temas
        forumManager.publicarComentario("General", "Pedro", "¡Hola a todos!");
        forumManager.publicarComentario("General", "Juan", "Bienvenido Pedro");
        forumManager.publicarComentario("Dudas", "Ana", "¿Cómo se juega?");
        forumManager.publicarComentario("Dudas", "Pedro", "¡Gracias!");

        // Implementación directa del endpoint
        List<edu.upc.dsa.models.UltimoComentarioDTO> ultimos = new java.util.ArrayList<>();
        for (String tema : forumManager.getTemas()) {
            List<edu.upc.dsa.models.Comentario> comentarios = forumManager.getComentarios(tema);
            if (!comentarios.isEmpty()) {
                edu.upc.dsa.models.Comentario ultimo = comentarios.get(comentarios.size() - 1);
                ultimos.add(new edu.upc.dsa.models.UltimoComentarioDTO(tema, ultimo.getContenido()));
            }
        }

        assertEquals(2, ultimos.size());
        edu.upc.dsa.models.UltimoComentarioDTO general = ultimos.stream().filter(u -> u.getTema().equals("General")).findFirst().orElse(null);
        edu.upc.dsa.models.UltimoComentarioDTO dudas = ultimos.stream().filter(u -> u.getTema().equals("Dudas")).findFirst().orElse(null);

        assertNotNull(general);
        assertEquals("Bienvenido Pedro", general.getUltimoComentario());

        assertNotNull(dudas);
        assertEquals("¡Gracias!", dudas.getUltimoComentario());
    }
}