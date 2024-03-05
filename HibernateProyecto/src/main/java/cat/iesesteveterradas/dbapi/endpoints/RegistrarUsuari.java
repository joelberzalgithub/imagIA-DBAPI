package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Date;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.Grups;
import cat.iesesteveterradas.dbapi.persistencia.GrupsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Pla;
import cat.iesesteveterradas.dbapi.persistencia.QuotaDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuari/registrar")
public class RegistrarUsuari {
    private static final Logger logger = LoggerFactory.getLogger(RegistrarUsuari.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput) {
        try {

            JSONObject input = new JSONObject(jsonInput);

            String telefon = input.optString("telefon", null);
            String nickname = input.optString("nickname", null);
            String email = input.optString("email", null);

            if (nickname == null || nickname.trim().isEmpty()) {
                logger.error("Nickname requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Nickname requerido\"}").build();
            }

            if (telefon == null) {
                logger.error("Telefono requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Telèfon requerit\"}").build();
            }

            if (email == null || email.trim().isEmpty()) {
                logger.error("Email requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Email requerit\"}").build();
            }

            Random random = new Random();
            int codigo1 = random.nextInt(900000) + 100000;
            String codigo = String.valueOf(codigo1);

            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);

            Pla pla = QuotaDAO.obtenQuotaDePla("Free");
            Usuaris usuari = UsuarisDao.creaUsuario(nickname, telefon, email, codigo, pla, currentDate);

            QuotaDAO.creaQuota(pla.getQuota(), pla.getQuota(), 0, usuari);

            Grups grup = GrupsDAO.findGroupByName("Cliente");

            UsuarisDao.addUserToGroup(usuari.getId(), grup.getId());

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "L'usuari s'ha creat correctament");

            // Crear el objeto JSON para la parte "data"
            JSONObject userData = new JSONObject();
            userData.put("nickname", nickname);
            userData.put("email", email);
            userData.put("codi_validacio", codigo);

            jsonResponse.put("data", userData);

            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            logger.error("Error en afegir l'usuari." + e);
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en afegir l'usuari.\"}")
                    .build();
        }
    }
}