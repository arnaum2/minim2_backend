package edu.upc.dsa.services;

import edu.upc.dsa.models.ForumManager;
import edu.upc.dsa.models.Comentario;
import edu.upc.dsa.models.UltimoComentarioDTO;

import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Api(value = "/foro", description = "Endpoint para el foro")
@Path("/foro")
public class ForumService {

    private ForumManager fm = ForumManager.getInstance();

    @GET
    @Path("/getTemas")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getTemas() {
        return fm.getTemas();
    }

    @GET
    @Path("/getComentarios/{tema}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comentario> getComentarios(@PathParam("tema") String tema) {
        return fm.getComentarios(tema);
    }

    @POST
    @Path("/publicarComentario")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response publicarComentario(Comentario c, @QueryParam("tema") String tema) {
        fm.publicarComentario(tema, c.getAutor(), c.getContenido());
        return Response.status(201).build();
    }

    @POST
    @Path("/crearTema")
    public Response crearTema(@QueryParam("nombre") String nombre) {
        fm.crearTema(nombre);
        return Response.status(201).build();
    }

    @DELETE
    @Path("/eliminarComentario")
    public Response eliminarComentario(@QueryParam("tema") String tema, @QueryParam("index") int index) {
        fm.eliminarComentario(tema, index);
        return Response.status(200).build();
    }

    @PUT
    @Path("/editarComentario")
    public Response editarComentario(@QueryParam("tema") String tema,
                                     @QueryParam("index") int index,
                                     @QueryParam("nuevoContenido") String nuevoContenido) {
        fm.editarComentario(tema, index, nuevoContenido);
        return Response.status(200).build();
    }

    @PUT
    @Path("/votarComentario")
    public Response votarComentario(@QueryParam("tema") String tema,
                                    @QueryParam("index") int index,
                                    @QueryParam("positivo") boolean positivo) {
        fm.votarComentario(tema, index, positivo);
        return Response.status(200).build();
    }

    // Endpoint para obtener el último comentario de cada tema
    @GET
    @Path("/ultimosComentarios")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UltimoComentarioDTO> getUltimosComentarios() {
        List<UltimoComentarioDTO> resultado = new ArrayList<>();
        for (String tema : fm.getTemas()) {
            List<Comentario> comentarios = fm.getComentarios(tema);
            if (!comentarios.isEmpty()) {
                Comentario ultimo = comentarios.get(comentarios.size() - 1);
                resultado.add(new UltimoComentarioDTO(tema, ultimo.getContenido()));
            }
        }
        return resultado;
    }
}