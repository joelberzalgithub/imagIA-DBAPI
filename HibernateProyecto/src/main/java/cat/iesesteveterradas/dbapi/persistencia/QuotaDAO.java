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

    public static boolean actualizarQuotaDeUsuario(Long usuariId, int disponibleNuevo, int totalNuevo,
            int consumidaNueva) {
        Transaction transaction = null;
        boolean actualizado = false;
        Session session = null;
        try {
            session = SessionFactoryManager.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Quota quota = session.createQuery("FROM Quota q WHERE q.usuari.id = :usuariId", Quota.class)
                    .setParameter("usuariId", usuariId)
                    .uniqueResult();

            if (quota != null) {

                quota.setDisponible(disponibleNuevo);
                quota.setTotal(totalNuevo);
                quota.setConsumida(consumidaNueva);
                session.saveOrUpdate(quota);
                actualizado = true;
                logger.info("Actualizada correctamente la quota del usuario " + usuariId);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar la quota para el usuario con ID: " + usuariId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return actualizado;
    }

    public static Quota obtenerQuotaDeUsuario(Long usuariId) {
        Quota quota = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            quota = session.createQuery("FROM Quota q WHERE q.usuari.id = :usuariId", Quota.class)
                    .setParameter("usuariId", usuariId)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error al obtener la quota para el usuario con ID: " + usuariId, e);
        }
        return quota;
    }

}
