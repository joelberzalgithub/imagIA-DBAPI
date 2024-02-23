package cat.iesesteveterradas.dbapi.persistencia;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarisDao {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarisDao.class);

    public static Usuaris creaUsuario(String nickname, String telefon, String email,String token) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris usuario = null;
        
        try {
            tx = session.beginTransaction();
            usuario = new Usuaris(nickname, telefon, email,token);
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

    public static boolean updateUsuarioPeticionByApiToken(String apitoken, Long idPeticio) {
        Transaction transaction = null;
        boolean updateSuccess = false;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE apitoken = :apitoken")
                                                .setParameter("apitoken", apitoken)
                                                .uniqueResult();
    
            if (usuario == null) {
                logger.info("No se encontró ningún usuario con el apitoken: {}", apitoken);
            } else {
                
                Peticions peticion = session.get(Peticions.class, idPeticio);
                if (peticion != null) {
                    usuario.addPeticio(peticion);
                    session.saveOrUpdate(usuario); 
                    logger.info("Peticion con ID: {} añadida al usuario con apitoken: {}", idPeticio, apitoken);
                    updateSuccess = true;
                } else {
                    logger.info("No se encontró ninguna Peticion con el ID: {}", idPeticio);
                }
            }
    
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar el usuario con apitoken: {}", apitoken, e);
            updateSuccess = false;
        }
        return updateSuccess;
    }
    
}

