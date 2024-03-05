package cat.iesesteveterradas.dbapi.endpoints;

import java.util.Date;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.iesesteveterradas.dbapi.persistencia.Pla;
import cat.iesesteveterradas.dbapi.persistencia.PlaDAO;
import cat.iesesteveterradas.dbapi.persistencia.Quota;
import cat.iesesteveterradas.dbapi.persistencia.QuotaDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuaris/consultar_quota")
public class ConsultarQuota {
    private static final Logger logger = LoggerFactory.getLogger(ConsultarQuota.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response llistatUsuaris(@HeaderParam("Authorization") String authorizationHeader) {
        try {

            String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : null;

            Long id = UsuarisDao.encontrarUsuarioPorToken(token);

            if (id == null) {
                logger.error("El token proporcionado no se ha encontrado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"El token proporcionado no se ha encontrado.\"}")
                        .build();
            }
            long currentTimeMillis = System.currentTimeMillis();

            Quota usuQuota = QuotaDAO.obtenerQuotaDeUsuario(id);

            Usuaris usuari = UsuarisDao.usuarioPorToken(token);

            long differenceInMillis = currentTimeMillis - usuari.getDate().getTime();
            long oneDayInMillis = 24 * 60 * 60 * 1000;

            if (differenceInMillis >= oneDayInMillis) {
                QuotaDAO.actualizarQuotaDeUsuario(usuari.getId(), usuQuota.getTotal(), usuQuota.getTotal(), 0);
                Date newDate = new Date(usuari.getDate().getTime() + (24 * 60 * 60 * 1000));
                UsuarisDao.actualizarFechaDeUsuario(usuari.getId(), newDate);
                logger.info("Ha pasado al menos un día.");
            } else {
                if (usuQuota.getConsumida() == usuQuota.getTotal()) {
                    logger.error("Too many request");
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"status\":\"ERROR\",\"message\":\"429\"}").build();
                }

            }

            Pla plan = PlaDAO.obtenerPlaPorId(usuari.getPla().getId());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Quota consultada correctament");

            JSONObject userData = new JSONObject();
            userData.put("pla", plan.getNom());
            JSONObject quotas = new JSONObject();
            quotas.put("total", usuQuota.getTotal());
            quotas.put("consumida", 0);
            quotas.put("disponible", usuQuota.getTotal());
            userData.put("quota", quotas);
            jsonResponse.put("data", userData);

            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            logger.error("Error en consultar la quota " + e);
            return Response.serverError()
                    .entity("{\"status\":\"ERROR\",\"message\":\"Error en consultar la quota\"}" + e).build();
        }
    }
}
