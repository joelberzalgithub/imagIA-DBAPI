package cat.iesesteveterradas.dbapi.endpoints;


import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.Grups;
import cat.iesesteveterradas.dbapi.persistencia.PeticionsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Resposta;
import cat.iesesteveterradas.dbapi.persistencia.RespostaDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuaris/admin_obtenir_llista")
public class LlistatUsuaris {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response llistatUsuaris(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            
            String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
            ? authorizationHeader.substring(7)
            : null;

            Long id = UsuarisDao.encontrarUsuarioPorToken(token);

            if(id!=null){
                boolean admin = UsuarisDao.esUsuarioAdministrador(id);
                if(admin == false){
                    return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"Este usuario no es admin\"}").build();
                }
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"status\":\"ERROR\",\"message\":\"El token proporcionado no se ha encontrado.\"}").build();
            }

            List<Usuaris> usuarios = UsuarisDao.encontrarTodosLosUsuariosExcluyendoAdministradores();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Consulta realitzada correctament");
            
            JSONArray data = new JSONArray();
        
            for (Usuaris usuario : usuarios) {
                String existsToken = usuario.getApitoken();
                Boolean validat = false;
                if(existsToken!=null){
                    validat = true;
                }
                JSONObject userData = new JSONObject();
                userData.put("nickname", usuario.getNickname());
                userData.put("email", usuario.getEmail());
                userData.put("telefon", usuario.getTelefon());
                userData.put("validat",validat );
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

            String prettyJsonResponse = jsonResponse.toString(4);
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("{\"status\":\"ERROR\",\"message\":\"Error en auntenticar l'usuari\"}"+e).build();
        }
    }
}
