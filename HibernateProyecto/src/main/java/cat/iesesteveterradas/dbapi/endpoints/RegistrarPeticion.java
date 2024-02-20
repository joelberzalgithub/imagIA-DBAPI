package cat.iesesteveterradas.dbapi.endpoints;



import java.util.Date;

import org.json.JSONObject;
import cat.iesesteveterradas.dbapi.persistencia.Peticions;
import cat.iesesteveterradas.dbapi.persistencia.PeticionsDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/peticions/afegir")
public class RegistrarPeticion {
     @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput) {
        try {
            JSONObject input = new JSONObject(jsonInput);
            
            String prompt = input.optString("prompt", null);
            String model = input.optString("model", null);
            String imatges = input.optString("imatges", null);
        
            if (model == null || model.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Model requerit\"}").build();
            }

            
            if (prompt == null || prompt.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Prompt requerit\"}").build();
            }

            if (imatges == null || imatges.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Base 64 requerit\"}").build();
            }

            long currentTimeMillis = System.currentTimeMillis();

            // Create a new Date object with the current time
            Date currentDate = new Date(currentTimeMillis);


            Peticions peticio = PeticionsDAO.creaPeticions(model, prompt, imatges,currentDate);
            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Petició registrada correctament");

            
            JSONObject userData = new JSONObject();
            userData.put("id", peticio.getId());

            
            jsonResponse.put("data", userData);

            // Retorna la resposta
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en afegir la peticio\"}").build();
        }
    }
}



