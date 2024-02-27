package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.query.Query;

public class PeticionsDAO {
    private static final Logger logger = LoggerFactory.getLogger(PeticionsDAO.class);

    public static Peticions creaPeticions(String model, String prompt, List<String> path, Date data) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Peticions peticio = null;
        try {
            tx = session.beginTransaction();
            peticio = new Peticions(path, data, prompt, model);
            session.save(peticio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error al crear el usuario", e);
        } finally {
            session.close();
        }
        return peticio;
    }

    public static Peticions findPeticionsById(long id) {
        Peticions peticions = null;
        Transaction transaction = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            peticions = session.get(Peticions.class, id);
            if (peticions == null) {
                logger.info("No se encontró ninguna Peticions con el ID: {}", id);
            }
            transaction.commit(); 
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al buscar Peticions con ID: {}", id, e);
        }
        return peticions;
    }
    
}
