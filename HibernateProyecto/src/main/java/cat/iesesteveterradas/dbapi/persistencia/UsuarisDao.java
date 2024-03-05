package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarisDao {

    private static final Logger logger = LoggerFactory.getLogger(UsuarisDao.class);

    public static Usuaris creaUsuario(String nickname, String telefon, String email, String codi_validacio, Pla pla,
            Date data) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris usuario = null;

        try {
            tx = session.beginTransaction();
            usuario = new Usuaris(nickname, telefon, email, codi_validacio, pla, data);
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
            if (tx != null)
                tx.rollback();
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
            if (tx != null)
                tx.rollback();
            logger.error("Error al obtener el código de validación para el usuario con teléfono: {}", telefon, e);
        } finally {
            session.close();
        }

        return codigoValidacion;
    }

    public static Long encontrarUsuarioPorToken(String apitoken) {
        Transaction transaction = null;
        boolean updateSuccess = false;
        Long id = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE apitoken = :apitoken")
                    .setParameter("apitoken", apitoken)
                    .uniqueResult();

            if (usuario == null) {
                logger.info("No se encontró ningún usuario con el apitoken: {}", apitoken);

            } else {
                id = usuario.getId();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar el usuario con apitoken: {}", apitoken, e);

        }
        return id;
    }

    public static Usuaris usuarioPorToken(String apitoken) {
        Transaction transaction = null;
        boolean updateSuccess = false;
        Usuaris usuari = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE apitoken = :apitoken")
                    .setParameter("apitoken", apitoken)
                    .uniqueResult();

            if (usuario == null) {
                logger.info("No se encontró ningún usuario con el apitoken: {}", apitoken);

            } else {
                usuari = usuario;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar el usuario con apitoken: {}", apitoken, e);

        }
        return usuari;
    }

    public static List<Usuaris> encontrarTodosLosUsuarios() {
        Transaction transaction = null;
        List<Usuaris> listaDeUsuarios = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            listaDeUsuarios = session.createQuery("FROM Usuaris", Usuaris.class).list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al recuperar los usuarios", e);
        }
        return listaDeUsuarios;
    }

    public static List<Usuaris> encontrarTodosLosUsuariosExcluyendoAdministradores() {
        Transaction transaction = null;
        List<Usuaris> listaUsuarios = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String nombreGrupoAdministradores = "Administrador";
            String hql = "SELECT u FROM Usuaris u WHERE NOT EXISTS (FROM u.grups g WHERE g.nom = :nombreGrupoAdministradores)";

            listaUsuarios = session.createQuery(hql, Usuaris.class)
                    .setParameter("nombreGrupoAdministradores", nombreGrupoAdministradores)
                    .list();

            transaction.commit();
            logger.info(listaUsuarios.toString());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al recuperar los usuarios excluyendo administradores", e);
        }
        return listaUsuarios;
    }

    public static boolean esUsuarioAdministrador(Long userId) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        boolean esAdministrador = false;
        try {
            tx = session.beginTransaction();
            Long count = (Long) session
                    .createQuery(
                            "SELECT COUNT(g) FROM Usuaris u JOIN u.grups g WHERE u.id = :userId AND g.nom = :groupName")
                    .setParameter("userId", userId)
                    .setParameter("groupName", "Administrador")
                    .uniqueResult();

            tx.commit();
            esAdministrador = count > 0;
            if (esAdministrador) {
                logger.info("El usuario con ID: {} es administrador", userId);
            } else {
                logger.info("El usuario con ID: {} no es administrador", userId);
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al verificar si el usuario con ID: {} es administrador", userId, e);
        } finally {
            session.close();
        }

        return esAdministrador;
    }

    public static Usuaris encontrarUsuarioPorEmailYContrasena(String email, String contrasena) {
        Transaction transaction = null;
        Usuaris usuario = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE email = :email AND contrasena = :contrasena")
                    .setParameter("email", email)
                    .setParameter("contrasena", contrasena)
                    .uniqueResult();
            if (usuario != null) {
                logger.info("Usuario encontrado con el email: {}", email);
            } else {
                logger.info("No se encontró ningún usuario con el email: {} y contraseña proporcionada.", email);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al buscar el usuario con email: {} y contraseña proporcionada.", email, e);
        }
        return usuario;
    }

    public static boolean actualizarApiTokenDeUsuario(Long usuarioId, String nuevoApiToken) {
        Transaction transaction = null;
        boolean actualizacionExitosa = false;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = session.get(Usuaris.class, usuarioId);
            if (usuario != null) {
                usuario.setApitoken(nuevoApiToken);
                session.update(usuario); // Guardamos los cambios
                transaction.commit();
                actualizacionExitosa = true;
                logger.info("apiToken actualizado para el usuario con ID: {}", usuarioId);
            } else {
                logger.info("No se encontró ningún usuario con el ID: {}", usuarioId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar el apiToken para el usuario con ID: {}", usuarioId, e);
        }
        return actualizacionExitosa;
    }

    public static boolean addUserToGroup(Long userId, Long groupId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuari = session.get(Usuaris.class, userId);
            Grups grup = session.get(Grups.class, groupId);

            if (usuari == null || grup == null) {
                System.out.println("Usuario o grupo no encontrado");
                return false;
            }
            usuari.getGrups().add(grup);
            grup.getUsuaris().add(usuari);
            session.saveOrUpdate(usuari);
            session.saveOrUpdate(grup);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static Usuaris usuarioPorTelefon(String telefon) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuaris usuari = null;

        try {
            tx = session.beginTransaction();

            // Busca el usuario por número de teléfono
            Usuaris usuario = (Usuaris) session.createQuery("FROM Usuaris WHERE telefon = :telefon")
                    .setParameter("telefon", telefon)
                    .uniqueResult();

            if (usuario != null) {
                usuari = usuario;
                logger.info("Código de validación obtenido para el usuario con teléfono: {}", telefon);
            } else {
                logger.error("Usuario no encontrado con el teléfono: {}", telefon);
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al obtener el código de validación para el usuario con teléfono: {}", telefon, e);
        } finally {
            session.close();
        }

        return usuari;
    }

    public static boolean actualizarPlaIdPorTelefono(Long plaId, String telefono) {
        Transaction transaction = null;
        boolean actualizado = false;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int filasActualizadas = session.createQuery("UPDATE Usuaris SET pla_id = :plaId WHERE telefon = :telefono")
                    .setParameter("plaId", plaId)
                    .setParameter("telefono", telefono)
                    .executeUpdate();

            if (filasActualizadas > 0) {
                actualizado = true;
                logger.info("Se actualizó correctamente el pla_id para el usuario con teléfono: {}", telefono);
            } else {
                logger.info("No se encontró ningún usuario con el teléfono: {} para actualizar el pla_id.", telefono);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar el pla_id para el usuario con teléfono: {}", telefono, e);
        }
        return actualizado;
    }

    public static boolean actualizarFechaDeUsuario(Long usuarioId, Date nuevaFecha) {
        Transaction transaction = null;
        boolean actualizado = false;
        try (Session session = SessionFactoryManager.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuaris usuario = session.get(Usuaris.class, usuarioId);

            if (usuario != null) {

                usuario.setDate(nuevaFecha);
                session.update(usuario);
                actualizado = true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar la fecha para el usuario con ID: " + usuarioId, e);
        }
        return actualizado;
    }

}
