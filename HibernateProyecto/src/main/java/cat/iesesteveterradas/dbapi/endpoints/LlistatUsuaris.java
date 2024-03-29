package cat.iesesteveterradas.dbapi.endpoints;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.Grups;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuaris/admin_obtenir_llista")
public class LlistatUsuaris {
    private static final Logger logger = LoggerFactory.getLogger(LlistatUsuaris.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response llistatUsuaris(@HeaderParam("Authorization") String authorizationHeader) {
        try {

            String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : null;

            Long id = UsuarisDao.encontrarUsuarioPorToken(token);

            if (id != null) {
                boolean admin = UsuarisDao.esUsuarioAdministrador(id);
                if (admin == false) {
                    logger.error("Este usuario no es admin.");
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"status\":\"ERROR\",\"message\":\"Este usuario no es admin.\"}").build();
                }
            } else {
                logger.error("El token proporcionado no se ha encontrado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"El token proporcionado no se ha encontrado.\"}")
                        .build();
            }

            List<Usuaris> usuarios = UsuarisDao.encontrarTodosLosUsuariosExcluyendoAdministradores();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Consulta realitzada correctament");

            JSONArray data = new JSONArray();

            for (Usuaris usuario : usuarios) {
                String existsToken = usuario.getApitoken();
                Boolean validat = false;
                if (existsToken != null) {
                    validat = true;
                }
                JSONObject userData = new JSONObject();
                userData.put("nickname", usuario.getNickname());
                userData.put("email", usuario.getEmail());
                userData.put("telefon", usuario.getTelefon());
                userData.put("validat", validat);
                userData.put("pla", usuario.getPla().getNom());

                JSONArray grups = new JSONArray();
                for (Grups grup : usuario.getGrups()) {
                    grups.put(grup.getNom());
                }
                userData.put("grups", grups);

                JSONObject quota = new JSONObject();
                quota.put("total", usuario.getQuota().getTotal());
                quota.put("consumida", usuario.getQuota().getConsumida());
                quota.put("disponible", usuario.getQuota().getDisponible());

                userData.put("quota", quota);

                data.put(userData);
            }

            jsonResponse.put("data", data);
            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            logger.error("Error en agafar els usuaris." + e);

            return Response.serverError()
                    .entity("{\"status\":\"ERROR\",\"message\":\"Error en agafar els usuaris.\"}" + e).build();
        }
    }
}
