package cat.iesesteveterradas.dbapi.persistencia;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaDAO {
    private static final Logger logger = LoggerFactory.getLogger(Pla.class);

    public static Pla obtenerPlaPorId(Long id) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Pla pla = null;
        try {
            tx = session.beginTransaction();
            pla = session.get(Pla.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error al obtener el Pla con ID: " + id, e);
        } finally {
            session.close();
        }
        return pla;
    }
}
