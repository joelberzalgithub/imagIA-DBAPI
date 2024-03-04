package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Random;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.Grups;
import cat.iesesteveterradas.dbapi.persistencia.GrupsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Pla;
import cat.iesesteveterradas.dbapi.persistencia.QuotaDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuaris/admin_canvi_pla")
public class CanviPla {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput,@HeaderParam("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
            ? authorizationHeader.substring(7)
            : null;
            JSONObject input = new JSONObject(jsonInput);
            
            String telefon = input.optString("telefon", null);
            String pla = input.optString("pla", null);

            if (pla == null || pla.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Nickname requerido\"}").build();
            }

            
            if (telefon == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Telèfon requerit\"}").build();
            }

            Pla plan = QuotaDAO.obtenQuotaDePla(pla);
            Usuaris usuari = UsuarisDao.usuarioPorTelefon(telefon);
            
            UsuarisDao.actualizarPlaIdPorTelefono(plan.getId(), telefon);

            QuotaDAO.actualizarQuotaDeUsuario(usuari.getId(), plan.getQuota(), plan.getQuota(), 0);

            

            

           
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "L'usuari s'ha creat correctament");

            // Crear el objeto JSON para la parte "data"
            JSONObject userData = new JSONObject();
            

            // Añadir el objeto "data" al JSON de respuesta
            jsonResponse.put("data", userData);

            // Retorna la resposta
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en afegir el usuari\"}").build();
        }
    }
}
