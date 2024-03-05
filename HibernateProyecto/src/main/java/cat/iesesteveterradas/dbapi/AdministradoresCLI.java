package cat.iesesteveterradas.dbapi;

import java.io.Console;
import java.util.Arrays;

public class AdministradoresCLI {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println(
                    "No es posible obtener la consola. Ejecuta el programa en un entorno que permita la interacción con la consola.");
            return;
        }

        String nickname = console.readLine("Introduce tu nickname: ");
        String email = console.readLine("Introduce tu email: ");

        char[] password = console.readPassword("Introduce tu contraseña: ");
        char[] confirmPassword = console.readPassword("Confirma tu contraseña: ");

        if (Arrays.equals(password, confirmPassword)) {
            System.out.println("Registro exitoso.");
            // Importante: Limpia los arrays de caracteres por razones de seguridad
            Arrays.fill(password, ' ');
            Arrays.fill(confirmPassword, ' ');
        } else {
            System.out.println("Las contraseñas no coinciden. Por favor, inténtalo de nuevo.");
        }
    }
}
