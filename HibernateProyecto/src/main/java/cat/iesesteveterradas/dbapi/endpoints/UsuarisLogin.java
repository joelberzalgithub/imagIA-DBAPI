package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Random;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.PeticionsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Resposta;
import cat.iesesteveterradas.dbapi.persistencia.RespostaDAO;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuari/login")
public class UsuarisLogin {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUsuaris(String jsonInput) {
        try {
            JSONObject input = new JSONObject(jsonInput);
            
            
            String email = input.optString("email", null);
            String contrasena = input.optString("password", null);
        
            if (email == null || email.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Email requerit\"}").build();
            }

            if (contrasena == null || contrasena.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Contrasenya requerida\"}").build();
            }

            Long userId = UsuarisDao.encontrarUsuarioPorEmailYContrasena(email, contrasena);

            if (userId != null){
                boolean isAdmin = UsuarisDao.esUsuarioAdministrador(userId);
                if(isAdmin == false){
                    return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Este usuario no es admin\"}").build();
                }
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"No se encontró ningún usuario con el email: "+email+" y contraseña proporcionada.\"}").build();
            }

            
            String token = generateRandomString(30);

            
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Usuari autenticat correctament");
            
            JSONObject userData = new JSONObject();
            userData.put("api_key", token);

            
            jsonResponse.put("data", userData);

            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en auntenticar l'usuari\"}").build();
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
