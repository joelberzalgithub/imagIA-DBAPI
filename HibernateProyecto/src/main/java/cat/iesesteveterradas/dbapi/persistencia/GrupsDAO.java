package cat.iesesteveterradas.dbapi.persistencia;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrupsDAO {
    

    private static final Logger logger = LoggerFactory.getLogger(Quota.class);
    public static Grups findGroupByName(String nom) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Grups grup = null;
        try {
            // Busca el grupo por nombre
            String hql = "FROM Grups WHERE nom = :nom";
            Query query = session.createQuery(hql);
            query.setParameter("nom", nom);
            grup = (Grups) query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error al buscar el grupo con nombre " + nom, e);
        } finally {
            session.close();
        }
        return grup;
    }
}
