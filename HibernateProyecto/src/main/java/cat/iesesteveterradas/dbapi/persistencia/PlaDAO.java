package cat.iesesteveterradas.dbapi.persistencia;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaDAO {
    private static final Logger logger = LoggerFactory.getLogger(Pla.class);

    public static Quota creaQuota(Integer disponible, Integer total, Integer consumida, Usuaris usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Quota quota = null;
        try {
            tx = session.beginTransaction();
            quota = new Quota(total, consumida, disponible, usuari);
            session.save(quota);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error al crear la quota", e);
        } finally {
            session.close();
        }
        return quota;
    }
}
