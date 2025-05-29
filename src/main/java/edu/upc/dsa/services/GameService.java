package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.models.ForumManager;
import edu.upc.dsa.models.ChatManager;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Api(value = "/usuarios", description = "Endpoint to Usuario Service")
@Path("/usuarios")
public class GameService {

    private GameManager gm;
    private static boolean initialized = false; // Bandera estática para usuarios
    private static boolean forumAndChatInitialized = false; // Bandera para foro y chat
    private static final Logger logger = Logger.getLogger(GameService.class.getName());

    public GameService() throws UsuarioYaRegistradoException {
        this.gm = GameManagerImpl.getInstance();
        if (!initialized) {
            try {
                this.gm.initTestUsers();
                initialized = true;
            } catch (UsuarioYaRegistradoException e) {
                logger.info("Usuarios ya registrados, pero objetos se cargan igual");
                this.gm.initTestUsers();
                initialized = true;
            }
        }
        // Inicializar foro y chat solo una vez
        if (!forumAndChatInitialized) {
            ForumManager.getInstance().initTestForum();
            ChatManager.getInstance().initTestChat();
            forumAndChatInitialized = true;
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUsuario(UsuReg usuReg) {
        try {
            gm.addUsuario(usuReg.getId(), usuReg.getName(), usuReg.getApellidos(),usuReg.getPswd(), usuReg.getMail(), usuReg.getPregunta(), usuReg.getRespuesta());
            return Response.status(201).build();
        } catch (UsuarioYaRegistradoException e) {
            return Response.status(409).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(Usulogin loginData) {
        try {
            UsuarioEnviar u = gm.login(loginData.getIdoname(), loginData.getPswd());
            return Response.status(200).entity(u).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") String id) {
        try {
            gm.deleteUsuario(id);
            return Response.status(200).build();
        } catch (UsuarioNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno").build();
        }
    }

    @PUT
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprarObjeto(Compra compra) {
        try {
            DevolverCompra usuarioActualizado = gm.Comprar(compra);
            return Response.status(200).entity(usuarioActualizado).build();
        } catch (UsuarioNoAutenticadoException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoSuficientesTarrosException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/armas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArmas() {
        System.out.println("va el getArmas():");
        ConsultaTienda armas = gm.findArmas();
        return Response.status(200).entity(armas).build();
    }

    @GET
    @Path("/tienda/skins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkin() {
        ConsultaTienda t = gm.findSkins();
        return Response.status(200).entity(t).build();
    }

    @GET
    @Path("/login/recordarContraseña")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getlogin(@QueryParam("id")String u) {
        try {
            String pregunta = gm.obtenerContra(u);
            return Response.ok(pregunta).build();
        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login/recuperarCuenta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuario(OlvContra usu) {
        try {
            Usuario u = gm.relogin(usu.getId(), usu.getRespuesta());
            return Response.status(200).entity(u).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @POST
    @Path("/login/cambiarContraseña")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cambiarContra(Usulogin u) {
        try {
            gm.CambiarContra(u.getIdoname(), u.getPswd());
            return Response.status(200).entity("Contraseña cambiada con éxito").build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/{id}/armas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArmasUsuario(@PathParam("id")String u) {
        try {
            System.out.println("Pidiendo armas de usuario: " + u);
            ConsultaTienda armas = gm.armasUsuario(u);
            System.out.println("Respuesta armas: " + armas);
            return Response.ok(armas).build();
        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayObjetos e) {
            System.out.println("No hay objetos para el usuario " + u);
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error interno al serializar: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/tienda/{id}/skins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkinUsuario(@PathParam("id")String u) {
        try {
            System.out.println("va el getSkinUsuario():");
            ConsultaTienda skins = gm.skinsUsuario(u);
            return Response.ok(skins).build();
        } catch (CredencialesIncorrectasException e) {
            System.out.println("Error interno del servidor");
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayObjetos e) {
            System.out.println("Error no hay objetos");
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/tienda/{id}/intercambio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Conversion(@PathParam("id") String id) {
        try {
            Intercambio i = gm.intercambio(id);
            return Response.status(200).entity(i).build();
        } catch (CredencialesIncorrectasException e) {
            return Response.status(401).entity(e.getMessage()).build();
        } catch (NoHayFlores e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/informacion/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRankingConPosicion(@PathParam("userId") String userId) {
        try {
            InfoList infoList = gm.rankingConPosicion(userId);
            return Response.status(200).entity(infoList).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error interno").build();
        }
    }
}