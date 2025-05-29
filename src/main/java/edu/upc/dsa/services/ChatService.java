package edu.upc.dsa.services;

import edu.upc.dsa.models.ChatManager;
import edu.upc.dsa.models.Mensaje;
import edu.upc.dsa.models.UltimoMensajeDTO;

import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Api(value = "/chat", description = "Endpoint para el chat")
@Path("/chat")
public class ChatService {

    private ChatManager cm = ChatManager.getInstance();

    @GET
    @Path("/getChats")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getChats() {
        return cm.getChats();
    }

    @GET
    @Path("/getMensajes/{chat}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mensaje> getMensajes(@PathParam("chat") String chat) {
        return cm.getMensajes(chat);
    }

    @POST
    @Path("/enviarMensaje")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enviarMensaje(Mensaje m, @QueryParam("chat") String chat) {
        cm.enviarMensaje(chat, m.getAutor(), m.getContenido());
        return Response.status(201).build();
    }

    @POST
    @Path("/crearChat")
    public Response crearChat(@QueryParam("nombre") String nombre) {
        cm.crearChat(nombre);
        return Response.status(201).build();
    }

    @DELETE
    @Path("/eliminarMensaje")
    public Response eliminarMensaje(@QueryParam("chat") String chat, @QueryParam("index") int index) {
        cm.eliminarMensaje(chat, index);
        return Response.status(200).build();
    }

    @PUT
    @Path("/editarMensaje")
    public Response editarMensaje(@QueryParam("chat") String chat,
                                  @QueryParam("index") int index,
                                  @QueryParam("nuevoContenido") String nuevoContenido) {
        cm.editarMensaje(chat, index, nuevoContenido);
        return Response.status(200).build();
    }

    // Endpoint para obtener el último mensaje de cada chat privado del usuario
    @GET
    @Path("/ultimosMensajes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UltimoMensajeDTO> getUltimosMensajes(@QueryParam("usuario") String usuario) {
        List<UltimoMensajeDTO> resultado = new ArrayList<>();
        for (String chat : cm.getChats()) {
            if (chat.contains("_")) {
                String[] partes = chat.split("_");
                if (partes.length == 2 && (partes[0].equals(usuario) || partes[1].equals(usuario))) {
                    String otroUsuario = partes[0].equals(usuario) ? partes[1] : partes[0];
                    List<Mensaje> mensajes = cm.getMensajes(chat);
                    if (!mensajes.isEmpty()) {
                        Mensaje ultimo = mensajes.get(mensajes.size() - 1);
                        resultado.add(new UltimoMensajeDTO(otroUsuario, ultimo.getContenido()));
                    }
                }
            }
        }
        return resultado;
    }
}