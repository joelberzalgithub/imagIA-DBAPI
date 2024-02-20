package cat.iesesteveterradas.dbapi.persistencia;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarisDao {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracioDAO.class);

    public static Usuaris creaUsuario(String nickname, String telefon, String codiValidacio, String email) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris usuario = null;
        try {
            tx = session.beginTransaction();
            // Crea un nuevo usuario sin verificar si ya existe
            usuario = new Usuaris(nickname, telefon, codiValidacio, email);
            session.save(usuario);
            tx.commit();
            logger.info("Nuevo usuario creado con el nickname: {}", nickname);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Error al crear el usuario", e);
        } finally {
            session.close();
        }
        return usuario;
    }
}

