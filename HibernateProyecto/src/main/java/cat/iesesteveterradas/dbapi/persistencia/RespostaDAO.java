package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.query.Query;

public class RespostaDAO {
    private static final Logger logger = LoggerFactory.getLogger(RespostaDAO.class);

    public static Resposta crearResposta(String text, Peticions peticio) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Resposta resposta = null;
        try {
            tx = session.beginTransaction();
            resposta = new Resposta(text,peticio);
            session.save(resposta);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error al crear la resposta", e);
        } finally {
            session.close();
        }
        return resposta;
    }

    
}