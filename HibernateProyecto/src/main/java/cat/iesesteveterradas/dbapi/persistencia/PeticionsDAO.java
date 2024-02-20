package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeticionsDAO {
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracioDAO.class);

    public static Peticions creaPeticions(String model, String prompt, String path, Date data) {
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
}
