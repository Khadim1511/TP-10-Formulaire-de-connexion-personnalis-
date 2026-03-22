package ma.fstg.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    /**
     * Affiche la page de login personnalisée.
     * Spring Security intercepte le POST vers /authenticate automatiquement.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Page d'accueil après connexion réussie.
     * Accessible aux utilisateurs authentifiés (USER et ADMIN).
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * Dashboard réservé aux administrateurs.
     * Spring Security bloque automatiquement l'accès si l'utilisateur n'a pas le rôle ADMIN.
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    /**
     * Dashboard réservé aux utilisateurs (USER et ADMIN).
     */
    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user-dashboard";
    }
}
