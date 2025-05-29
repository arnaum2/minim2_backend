package edu.upc.dsa.services;

import edu.upc.dsa.models.FriendManager;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Api(value = "/amigos", description = "Endpoint para gestión de amigos")
@Path("/amigos")
public class FriendService {

    private FriendManager fm = FriendManager.getInstance();

    @GET
    @Path("/getAmigos/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAmigos(@PathParam("usuario") String usuario) {
        return fm.getAmigos(usuario);
    }

    @POST
    @Path("/enviarSolicitud")
    public Response enviarSolicitud(@QueryParam("de") String de, @QueryParam("a") String a) {
        fm.enviarSolicitud(de, a);
        return Response.status(201).build();
    }

    @POST
    @Path("/aceptarSolicitud")
    public Response aceptarSolicitud(@QueryParam("de") String de, @QueryParam("a") String a) {
        fm.aceptarSolicitud(de, a);
        return Response.status(200).build();
    }

    @GET
    @Path("/sonAmigos")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean sonAmigos(@QueryParam("usuario1") String usuario1, @QueryParam("usuario2") String usuario2) {
        return fm.sonAmigos(usuario1, usuario2);
    }

    @GET
    @Path("/solicitudesPendientes/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getSolicitudesPendientes(@PathParam("usuario") String usuario) {
        return fm.getSolicitudesPendientes(usuario);
    }
}