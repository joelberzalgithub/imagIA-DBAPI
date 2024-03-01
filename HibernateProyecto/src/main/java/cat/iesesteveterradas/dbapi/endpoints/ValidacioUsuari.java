package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Random;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuari/validar")
public class ValidacioUsuari {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput) {
        try {
            JSONObject input = new JSONObject(jsonInput);
            
            String telefon = input.optString("telefon", null);
            String codi_validacio = input.optString("codi_validacio", null);


            // Validación para 'nickname'
            if (codi_validacio == null || codi_validacio.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Nickname requerido\"}").build();
            }

            // Validación para 'telefon'
            if (telefon == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Telèfon requerit\"}").build();
            }

            String token = generateRandomString(30);

            
            String codi = UsuarisDao.obtenerCodigoValidacionPorTelefono(telefon);

            if (!codi.equals(codi_validacio)){
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Codi validacio diferents\"}").build();
            }

            UsuarisDao.updateUsuarioApitokenPorTelefono(telefon, token);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "L'usuari s'ha validat correctament");

            
            JSONObject userData = new JSONObject();
            userData.put("api_key", token);

            // Añadir el objeto "data" al JSON de respuesta
            jsonResponse.put("data", userData);

            // Retorna la resposta
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) { 
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en validar el usuari\"}").build();
        }
    }

    public static String generateRandomString(int targetStringLength) {
        int leftLimit = 48; 
        int rightLimit = 122; 
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();

        return generatedString;
    }

}
