package cat.iesesteveterradas.dbapi;

import java.io.Console;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import cat.iesesteveterradas.dbapi.persistencia.Grups;
import cat.iesesteveterradas.dbapi.persistencia.GrupsDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuaris;
import cat.iesesteveterradas.dbapi.persistencia.UsuarisDao;

public class AdministradoresCLI {

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println(
                    "No es posible obtener la consola. Ejecuta el programa en un entorno que permita la interacción con la consola.");
            return;
        }

        while (true) {
            System.out.println("Menú:");
            System.out.println("1. Crear usuario");
            System.out.println("0. Apagar");

            String opcion = console.readLine("Elige una opción: ");

            switch (opcion) {
                case "1":
                    registrarUsuario(console);
                    break;
                case "0":
                    System.out.println("Apagando el programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void registrarUsuario(Console console) {
        String nickname = console.readLine("Introduce tu nickname: ");
        String email = console.readLine("Introduce tu email: ");

        char[] password = console.readPassword("Introduce tu contraseña: ");
        char[] confirmPassword = console.readPassword("Confirma tu contraseña: ");

        if (Arrays.equals(password, confirmPassword)) {
            String passwordString = new String(confirmPassword);
            String hashedPassword = DigestUtils.sha256Hex(passwordString);
            Usuaris usuari = UsuarisDao.creaUsuarioAdmin(nickname, hashedPassword, email);
            Grups grup = GrupsDAO.findGroupByName("Administrador");

            UsuarisDao.addUserToGroup(usuari.getId(), grup.getId());
            System.out.println("Registro exitoso.");

            Arrays.fill(password, ' ');
            Arrays.fill(confirmPassword, ' ');
        } else {
            System.out.println("Las contraseñas no coinciden. Por favor, inténtalo de nuevo.");
            Arrays.fill(password, ' ');
            Arrays.fill(confirmPassword, ' ');
        }
    }
}
