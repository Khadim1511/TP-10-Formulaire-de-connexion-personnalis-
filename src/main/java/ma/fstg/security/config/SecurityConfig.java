package ma.fstg.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Définit les utilisateurs en mémoire (sans base de données).
     * {noop} indique que le mot de passe n'est PAS encodé (usage pédagogique uniquement).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}1234")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    /**
     * Configure la chaîne de filtres de sécurité (SecurityFilterChain).
     * Définit les règles d'accès, le formulaire de login et le logout.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Pages accessibles sans authentification
                .requestMatchers("/login", "/css/**").permitAll()
                // Routes réservées aux ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Routes accessibles aux USER et ADMIN
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                // Toutes les autres routes nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Page de login personnalisée
                .loginPage("/login")
                // URL qui traite la soumission du formulaire (gérée automatiquement par Spring Security)
                .loginProcessingUrl("/authenticate")
                // Redirection après connexion réussie (true = toujours vers /home)
                .defaultSuccessUrl("/home", true)
                // Redirection en cas d'échec
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                // URL pour déclencher le logout
                .logoutUrl("/logout")
                // Redirection après déconnexion réussie
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
