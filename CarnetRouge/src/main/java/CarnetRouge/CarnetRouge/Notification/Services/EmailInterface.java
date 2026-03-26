package CarnetRouge.CarnetRouge.Notification.Services;

public interface EmailInterface {
    // ✅ Email de bienvenue envoyé à la création d'un utilisateur
    void envoyerEmailBienvenue(String destinataire, String prenom, String nom,
                               String motDePasse, String role);

    // ✅ Corps HTML de l'email
    String construireCorpsEmail(String prenom, String nom, String email,
                                String motDePasse, String role);
}
