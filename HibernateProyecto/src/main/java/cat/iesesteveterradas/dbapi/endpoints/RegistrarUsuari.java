package cat.iesesteveterradas.dbapi.endpoints;

import cat.iesesteveterradas.dbapi.endpoints.UsuariService;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuari/registrar")
public class RegistrarUsuari {

    UsuariService usuariService = new UsuariService(); // Asume que tienes esta clase

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuari(RegistrarUsuari usuariDTO) {
        try {
            
            Usuaris usuari = usuariService.crearUsuari(usuariDTO);
            if (usuari != null) {
                return Response.ok().entity(new MensajeRespuesta("OK", "L'usuari s'ha creat correctament", usuari)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("ERROR", "No se pudo crear el usuario").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeRespuesta("ERROR", "Error del servidor")).build();
        }
    }
}

