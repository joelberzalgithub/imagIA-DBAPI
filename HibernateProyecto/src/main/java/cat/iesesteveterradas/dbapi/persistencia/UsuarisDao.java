package cat.iesesteveterradas.dbapi.persistencia;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarisDao {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarisDao.class);

    public static Usuaris creaUsuario(String nickname, String telefon, String email,String codi_validacio) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris usuario = null;
        
        try {
            tx = session.beginTransaction();
            usuario = new Usuaris(nickname, telefon, email,codi_validacio);
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
                return false;
            } else {
                
                Peticions peticion = session.get(Peticions.class, idPeticio);
                if (peticion != null) {
                    usuario.addPeticio(peticion);
                    session.saveOrUpdate(usuario); 
                    logger.info("Peticion con ID: {} añadida al usuario con apitoken: {}", idPeticio, apitoken);
                    updateSuccess = true;
                } else {
                    logger.info("No se encontró ninguna Peticion con el ID: {}", idPeticio);
                    return false;
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

    public static boolean updateUsuarioApitokenPorTelefono(String telefon, String apitoken) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        boolean updateSuccess = false;
        try {
            tx = session.beginTransaction();
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE telefon = :telefon")
                                                .setParameter("telefon", telefon)
                                                .uniqueResult();   
            if (usuario != null) {
                // Actualiza el apitoken
                usuario.setApitoken(apitoken);
                session.update(usuario);
                tx.commit();
                logger.info("Apitoken actualizado para el usuario con teléfono: {}", telefon);
                updateSuccess = true;
            } else {
                logger.error("Usuario no encontrado con el teléfono: {}", telefon);
                
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error al actualizar el apitoken para el usuario con teléfono: {}", telefon, e);
        } finally {
            session.close();
        }

        return updateSuccess;
    }

    public static String obtenerCodigoValidacionPorTelefono(String telefon) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        String codigoValidacion = null;

        try {
            tx = session.beginTransaction();
            
            // Busca el usuario por número de teléfono
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE telefon = :telefon")
                                                .setParameter("telefon", telefon)
                                                .uniqueResult();
            
            if (usuario != null) {
                codigoValidacion = usuario.getCodivalidacio();
                logger.info("Código de validación obtenido para el usuario con teléfono: {}", telefon);
            } else {
                logger.error("Usuario no encontrado con el teléfono: {}", telefon);
                // Manejar el caso de usuario no encontrado según se necesite
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error al obtener el código de validación para el usuario con teléfono: {}", telefon, e);
        } finally {
            session.close();
        }

        return codigoValidacion;
    }




    public static boolean encontrarUsuarioPorToken(String apitoken) {
        Transaction transaction = null;
        boolean updateSuccess = false;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE apitoken = :apitoken")
                                                .setParameter("apitoken", apitoken)
                                                .uniqueResult();
    
            if (usuario == null) {
                logger.info("No se encontró ningún usuario con el apitoken: {}", apitoken);
                updateSuccess = false;
            } else{
                updateSuccess = true;
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

