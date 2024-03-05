package cat.iesesteveterradas.dbapi.endpoints;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(CanviPla.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirUsuari(String jsonInput, @HeaderParam("Authorization") String authorizationHeader) {
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
                            .entity("{\"status\":\"ERROR\",\"message\":\"Este usuario no es admin\"}").build();
                }
            } else {
                logger.error("El token proporcionado no se ha encontrado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"El token proporcionado no se ha encontrado.\"}")
                        .build();
            }
            JSONObject input = new JSONObject(jsonInput);

            String telefon = input.optString("telefon", null);
            String pla = input.optString("pla", null);

            if (pla == null || pla.trim().isEmpty()) {
                logger.error("Plan requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Nickname requerido\"}").build();
            }

            if (telefon == null) {
                logger.error("Telefono requerido no proporcionado.");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":\"ERROR\",\"message\":\"Telèfon requerit\"}").build();
            }

            Pla plan = QuotaDAO.obtenQuotaDePla(pla);
            Usuaris usuari = UsuarisDao.usuarioPorTelefon(telefon);

            UsuarisDao.actualizarPlaIdPorTelefono(plan.getId(), telefon);

            QuotaDAO.actualizarQuotaDeUsuario(usuari.getId(), plan.getQuota(), plan.getQuota(), 0);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Pla canviat correctament");

            JSONObject userData = new JSONObject();
            userData.put("pla", plan.getNom());
            JSONObject quotas = new JSONObject();
            quotas.put("total", plan.getQuota());
            quotas.put("consumida", 0);
            quotas.put("disponible", plan.getQuota());
            userData.put("quota", quotas);
            jsonResponse.put("data", userData);

            logger.info("Petición registrada correctamente: {}", jsonResponse.toString());
            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en cambiar el Pla\"}")
                    .build();
        }
    }
}
