
package com.aguacatemakeup.backend.config;

import lombok.RequiredArgsConstructor;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro JWT.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Configuración de las reglas de acceso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Desactivamos CSRF para nuestra API REST sin sesiones
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Definimos qué endpoints son públicos y cuáles protegidos
                .authorizeHttpRequests(auth -> auth

                        // REGISTRO: cualquier persona puede registrarse
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/usuarios"
                        ).permitAll()

                        // LOGIN: cualquier persona puede iniciar sesión
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        ).permitAll()

                        // CATÁLOGO PÚBLICO
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos/publicados"
                        ).permitAll()

                        // CATEGORÍAS Y SUBCATEGORÍAS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categorias/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/subcategorias/**"
                        ).permitAll()

                        // IMÁGENES PÚBLICAS DE PRODUCTOS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos/*/imagenes"
                        ).permitAll()

                        // ADMINISTRADOR: consultar todos los usuarios
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: consultar un usuario por correo
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios/correo"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: consultar un usuario por ID
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/usuarios/*"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: actualizar usuarios
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/usuarios/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: desactivar usuarios
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/usuarios/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: consultar todos los productos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: consultar un producto por ID
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos/*"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: crear productos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/productos/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: actualizar productos
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/productos/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: publicar o modificar estado de productos
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/productos/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: eliminar productos
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/productos/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: administración de categorías
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categorias/**"
                        ).hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR: administración de subcategorías
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/subcategorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/subcategorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/subcategorias/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/subcategorias/**"
                        ).hasRole("ADMINISTRADOR")

                        // Cualquier otra solicitud necesita autenticación
                        .anyRequest().authenticated()
                )

                // 4. Nuestra API trabajará sin sesiones HTTP
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Desactivamos los mecanismos de login predeterminados
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Incorpora nuestro filtro JWT antes del filtro estándar.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuracionCors = new CorsConfiguration();

        // Orígenes permitidos del frontend
        configuracionCors.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        // Métodos HTTP permitidos
        configuracionCors.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Headers permitidos
        configuracionCors.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        // No estamos utilizando cookies de autenticación
        configuracionCors.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource origenConfiguracion =
                new UrlBasedCorsConfigurationSource();

        origenConfiguracion.registerCorsConfiguration(
                "/**",
                configuracionCors
        );

        return origenConfiguracion;
    }



    // 6. Bean para proteger las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

