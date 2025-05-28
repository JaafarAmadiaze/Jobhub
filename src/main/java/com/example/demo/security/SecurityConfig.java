package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // dev only!
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // ⚠️ Désactiver CSRF uniquement en dev/test
                .authorizeHttpRequests(auth -> auth
                        // Accès public
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()

                        .requestMatchers("/listJobs","/listCompanies","company/**").hasAnyRole("CANDIDATE", "RECRUITER")


                        // Routes accessibles aux candidats
                        .requestMatchers(
                                
                                "/myApplications",
                                "/apply",
                                "/cancelApplication/**"
                        ).hasRole("CANDIDATE")

                        // Routes accessibles aux recruteurs
                        .requestMatchers(

                                "/createJob",
                                "/editJob",
                                "/deleteJob",
                                "/applicationsReceived",
                                "/saveJob",
                                "/updateStatus",
                                "/createCompany",
                                "/editCompany",
                                "/deleteCompany"
                        ).hasRole("RECRUITER")

                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/listJobs", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ✅ Production-safe encoder
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

