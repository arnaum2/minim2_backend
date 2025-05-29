package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Issue;
import edu.upc.dsa.models.Usuario;
import edu.upc.dsa.models.UsuarioEnviar;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

@Api(value = "/issues", description = "Les denuncies")
@Path("/issues")
public class IssueService {
    private GameManager um;
    private static final List<Issue> issues = new ArrayList<>();
    static {
        issues.add(new Issue("2025-05-28T14:00:00", "Estic farta", "Malboro",   "Usuari no respectuós en el xat"));
        issues.add(new Issue("2025-05-28T15:30:00", "M'han cansat", "Gold",     "Contingut inapropiat en el fòrum"));
        issues.add(new Issue("2025-05-29T09:15:00","Dificultat nivell", "charlie", "No em paren de matar, quina merda"));
    }
    public IssueService() {
        this.um = GameManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "Enviar Issue", notes = "Envia una denuncia")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Exitós"),
            @ApiResponse(code = 500, message = "Error de validació")
    })
    @Path("/addIssue")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addIssue(Issue issue) throws MissingDataException {
        GameManager manager = GameManagerImpl.getInstance();
        issues.add(issue);
        try {
            manager.addIssue(issue.getDate(), issue.getTitol(), issue.getInformer(), issue.getMessage());
            return Response.status(201).entity(issue).build();
        } catch (Exception e) {
            return Response.status(500).entity(issue).build();
        }
    }

    @GET
    @ApiOperation(value = "Obtenir una llista de tots els issues", notes = "issues dels usuaris")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Issue.class, responseContainer="List"),
    })
    @Path("/llistaIssues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIssues() {

        GenericEntity<List<Issue>> entity = new GenericEntity<List<Issue>>(issues){};
        return Response.ok(entity).build();
    }
}