package cat.iesesteveterradas.dbapi.persistencia;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotaDAO {
    private static final Logger logger = LoggerFactory.getLogger(Quota.class);

    public static Quota creaQuota(Integer disponible, Integer total, Integer consumida, Usuaris usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Quota quota = null;
        try {
            tx = session.beginTransaction();
            quota = new Quota(total,consumida,disponible,usuari);
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

    public static Pla obtenQuotaDePla(String nombrePla) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Pla pla = null;
        try {
            // Busca el plan por nombre
            String hql = "FROM Pla WHERE nom = :nombrePla";
            Query query = session.createQuery(hql);
            query.setParameter("nombrePla", nombrePla);
            pla = (Pla) query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener la cuota del plan " + nombrePla, e);
        } finally {
            session.close();
        }
        return pla; 
    }
    
    




}
