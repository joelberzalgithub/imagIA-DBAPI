package cat.iesesteveterradas.dbapi.endpoints;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.Peticions;
import cat.iesesteveterradas.dbapi.persistencia.PeticionsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Resposta;
import cat.iesesteveterradas.dbapi.persistencia.RespostaDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/respostes/afegir")
public class RegistrarResposta {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirResposta(String jsonInput) {
        try {
            JSONObject input = new JSONObject(jsonInput);
            
            
            Long id = input.optLong("id_peticio");
            String text = input.optString("text_generat", null);
        
            if (text == null || text.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Resposta requerida\"}").build();
            }

            if (id== null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Id requerit\"}").build();
            }
            
            Resposta resposta = RespostaDAO.crearResposta(text, PeticionsDAO.findPeticionsById(id));

            
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Resposta registrada correctament");

            
            JSONObject userData = new JSONObject();
            userData.put("id", resposta.getId());
            userData.put("id_peticio", id);

            
            jsonResponse.put("data", userData);

            // Retorna la resposta
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en afegir la peticio\"}").build();
        }
    }
}
