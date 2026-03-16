package CarnetRouge.CarnetRouge.GDU.Initialisation;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Admin123!";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Mot de passe chiffré : " + encodedPassword);

        // Compare avec le mot de passe stocké en base
        boolean matches = encoder.matches(rawPassword, "$2a$10$DrEHYqRM8i5x05xJRE6Q/e6NctD0YqY1OIL.JduLv8FQSuJL.C.gK");
        System.out.println("Correspondance : " + matches);
    }
}
