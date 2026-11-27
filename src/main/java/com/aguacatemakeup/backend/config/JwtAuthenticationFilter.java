
package com.aguacatemakeup.backend.config;

import com.aguacatemakeup.backend.entity.Usuarios;
import com.aguacatemakeup.backend.repository.UsuariosRepository;
import com.aguacatemakeup.backend.service.JwtService;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Repositorio para buscar al usuario en la base de datos.
    private final UsuariosRepository usuariosRepository;

    // Extrae y valida la información del JWT.
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el encabezado Authorization de la petición HTTP.
        String encabezado = request.getHeader("Authorization");
        // 2. Comprobar que exista el encabezado y que utilice Bearer.
        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
   // Si no hay token, continúa la cadena de filtros Spring Security decidirá si la ruta permite acceso anónimo.
            filterChain.doFilter(request, response);
            return;
        }
        // 3. Extraer el JWT eliminando el prefijo "Bearer".
        String token = encabezado.substring(7);
        try { // 4. Extraer el correo del usuario desde el JWT.
            String correo = jwtService.extraerCorreo(token);
            // 5. Evitar autenticar nuevamente si ya existe autenticación.
            if (correo != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                // 6. Buscar al usuario en la base de datos mediante su correo.
                Usuarios usuario = usuariosRepository.findByCorreo(correo)
                        .orElse(null);
                // 7. Comprobar que el usuario exista y que el token sea válido.
                if (usuario != null &&
                        jwtService.esTokenValido(token, usuario)) {
                    // 8. Obtener el rol del usuario.
                    String rol = usuario.getRol().getNombre();
                    // 9. Convertir el rol en una autoridad de Spring Security.
                    SimpleGrantedAuthority autoridad =
                            new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());
                    // 10. Crear el objeto que representa al usuario autenticado.
                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getCorreo(),
                                    null,
                                    List.of(autoridad)
                            );

                    // 11. Agregar detalles de la petición HTTP.
                    autenticacion.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );
                    // 12. Registrar la autenticación en el contexto de seguridad.
                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            // Si el JWT es inválido, no autenticamos al usuario.
            // Las rutas protegidas serán rechazadas por Spring Security.
            SecurityContextHolder.clearContext();
        }
        // 13. Continuar con los siguientes filtros de Spring Security.
        filterChain.doFilter(request, response);
    }
}
