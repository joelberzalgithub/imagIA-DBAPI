package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuari/validar")
public class ValidacioUsuari {
    private static final Logger logger = LoggerFactory.getLogger(ValidacioUsuari.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput) {
        try {
            JSONObject input = new JSONObject(jsonInput);

            String telefon = input.optString("telefon", null);
            String codi_validacio = input.optString("codi_validacio", null);

            if (codi_validacio == null || codi_validacio.trim().isEmpty()) {
                logger.error("Codigo de validacion requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Codigo de validacion requerido.\"}").build();
            }

            if (telefon == null) {
                logger.error("Telefono requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Telèfon requerit\"}").build();
            }

            String token = generateRandomString(30);

            String codi = UsuarisDao.obtenerCodigoValidacionPorTelefono(telefon);

            if (!codi.equals(codi_validacio)) {
                logger.error("Codigo de validacion diferentes.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Codi validacio diferents.\"}").build();
            }

            UsuarisDao.updateUsuarioApitokenPorTelefono(telefon, token);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "L'usuari s'ha validat correctament");

            JSONObject userData = new JSONObject();
            userData.put("api_key", token);

            jsonResponse.put("data", userData);

            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            logger.error("Error en validar el usuari. " + e);
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en validar el usuari.\"}")
                    .build();
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
