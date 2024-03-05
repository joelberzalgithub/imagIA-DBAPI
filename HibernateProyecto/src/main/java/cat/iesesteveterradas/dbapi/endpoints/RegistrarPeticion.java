package cat.iesesteveterradas.dbapi.endpoints;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.Peticions;
import cat.iesesteveterradas.dbapi.persistencia.PeticionsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Quota;
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

@Path("/peticions/afegir")
public class RegistrarPeticion {
    private static final Logger logger = LoggerFactory.getLogger(RegistrarPeticion.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirPeticio(@HeaderParam("Authorization") String authorizationHeader, String jsonInput) {
        List<String> path = new ArrayList<>();
        try {
            String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : null;
            JSONObject input = new JSONObject(jsonInput);

            String prompt = input.optString("prompt", null);
            String model = input.optString("model", null);
            JSONArray paths = input.getJSONArray("imatges");

            if (model == null || model.trim().isEmpty()) {
                logger.error("Modelo requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Model requerit\"}").build();
            }

            if (prompt == null || prompt.trim().isEmpty()) {
                logger.error("Prompt requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Prompt requerit\"}").build();
            }

            if (paths == null || paths.length() == 0) {
                logger.error("Base64 requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Base 64 requerit\"}").build();
            }

            Usuaris usuari = UsuarisDao.usuarioPorToken(token);
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);

            if (usuari == null) {
                logger.error("Apitoken no valida.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Apitoken no valida\"}").build();
            } else {
                Quota usuQuota = QuotaDAO.obtenerQuotaDeUsuario(usuari.getId());
                QuotaDAO.actualizarQuotaDeUsuario(usuari.getId(), usuQuota.getDisponible() - 1, usuQuota.getTotal(),
                        usuQuota.getConsumida() + 1);
            }

            for (int i = 0; i < paths.length(); i++) {
                String base64Image = paths.getString(i);
                String imageType = getImageType(base64Image);
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String nombre = generateRandomString(12);

                if (imageType.equals("JPG")) {
                    path.add("Imagenes/" + nombre + ".jpg");
                    try (FileOutputStream imageOutFile = new FileOutputStream("Imagenes/" + nombre + ".jpg")) {
                        imageOutFile.write(imageBytes);
                        logger.info("La imagen se ha creado exitosamente.");
                    } catch (IOException e) {
                        logger.error("Error al escribir la imagen " + e);

                    }
                } else {
                    path.add("Imagenes/" + nombre + ".png");
                    try (FileOutputStream imageOutFile = new FileOutputStream("Imagenes/" + nombre + ".png")) {
                        imageOutFile.write(imageBytes);
                        logger.info("La imagen se ha creado exitosamente.");
                    } catch (IOException e) {
                        logger.error("Error al escribir la imagen " + e);
                    }

                }
            }

            Peticions peticio = PeticionsDAO.creaPeticions(model, prompt, path, currentDate);
            UsuarisDao.updateUsuarioPeticionByApiToken(token, peticio.getId());

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Petició registrada correctament");

            JSONObject userData = new JSONObject();
            userData.put("id", peticio.getId());

            jsonResponse.put("data", userData);
            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            logger.error("Error en afegir la peticio ." + e);
            return Response.serverError()
                    .entity("{\"status\":\"ERROR\",\"message\":\"Error en afegir la peticio.\"}" + e).build();
        }
    }

    public static String getImageType(String base64String) {
        if (base64String.startsWith("iVBORw0KGgo")) {
            return "PNG";
        } else if (base64String.startsWith("/9j/")) {
            return "JPG";
        } else {
            return "Unknown";
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
