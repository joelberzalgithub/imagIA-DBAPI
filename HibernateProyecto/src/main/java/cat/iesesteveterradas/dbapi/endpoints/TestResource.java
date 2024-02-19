package cat.iesesteveterradas.dbapi.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/deu")
public class TestResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testxd()
    {
        return "Hola món!";
    }
}
