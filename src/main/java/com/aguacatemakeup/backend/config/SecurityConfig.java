
package com.aguacatemakeup.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Configuración de las reglas de acceso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // 2. Desactivamos CSRF para nuestra API REST sin sesiones
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Definimos qué endpoints son públicos y cuáles protegidos
                .authorizeHttpRequests(auth -> auth

                        // REGISTRO: cualquier persona puede registrarse
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // LOGIN: cualquier persona puede iniciar sesión
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // CATÁLOGO PÚBLICO
                        .requestMatchers(HttpMethod.GET, "/api/productos/publicados").permitAll()

                        // CATEGORÍAS Y SUBCATEGORÍAS
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/subcategorias/**").permitAll()

                        // IMÁGENES PÚBLICAS DE PRODUCTOS
                        .requestMatchers(HttpMethod.GET, "/api/productos/*/imagenes").permitAll()

                        // Cualquier otra solicitud necesita autenticación
                        .anyRequest().authenticated()
                )

                // 4. Nuestra API trabajará sin sesiones HTTP
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Desactivamos los mecanismos de login predeterminados
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 6. Bean para proteger las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
