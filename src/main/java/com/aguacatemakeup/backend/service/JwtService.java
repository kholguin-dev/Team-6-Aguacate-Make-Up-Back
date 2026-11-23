
package com.aguacatemakeup.backend.service;

import com.aguacatemakeup.backend.entity.Usuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {

    // Clave criptográfica que se utilizará para firmar y verificar los tokens.
    private final SecretKey claveFirma;

    // Tiempo durante el cual será válido el token, expresado en milisegundos.
    private final long duracionToken;

    // Constructor: recibe la configuración desde application.properties.
    public JwtService(
            @Value("${jwt.secret}") String claveSecretaBase64,
            @Value("${jwt.expiration}") long duracionToken) {

        // Convierte la clave Base64 nuevamente a bytes.
        byte[] claveBytes = Decoders.BASE64.decode(claveSecretaBase64);

        // Construye la clave criptográfica para el algoritmo HMAC.
        this.claveFirma = Keys.hmacShaKeyFor(claveBytes);

        this.duracionToken = duracionToken;
    }

    // 1. Generar Token JWT
    public String generarToken(Usuarios usuario) {
        // Captura la fecha y hora actual.
        Date fechaActual = new Date();
        // Calcula cuándo expirará el token.
        Date fechaExpiracion = new Date(
                fechaActual.getTime() + duracionToken
        );
        // Construye, firma y devuelve el token.
        return Jwts.builder()
                // Subject: identifica al usuario mediante su correo.
                .subject(usuario.getCorreo())
                // Claim personalizado: almacena el rol del usuario.
                .claim("rol", usuario.getRol().getNombre())
                // Fecha en la que se creó el token.
                .issuedAt(fechaActual)
                // Fecha en la que dejará de ser válido.
                .expiration(fechaExpiracion)
                // Firma el token usando HS256 y nuestra clave secreta.
                .signWith(claveFirma, Jwts.SIG.HS256)
                // Genera la cadena JWT definitiva.
                .compact();
    }
    // 2. Extra info del token
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                // Comprueba la firma usando nuestra clave.
                .verifyWith(claveFirma)
                // Construye el analizador.
                .build()
                // Analiza el JWT firmado y verifica su validez.
                .parseSignedClaims(token)
                // Obtiene los datos contenidos en el Payload.
                .getPayload();
    }
    // Extrae el correo electrónico almacenado en el Subject.
    public String extraerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }

    // 3. Validar el Token JWT
    public boolean esTokenValido(String token, Usuarios usuario) {
        try {
            // Extrae y verifica la información del token.
            Claims claims = extraerClaims(token);
            // Comprueba que el token pertenezca al usuario esperado.
            boolean mismoUsuario = Objects.equals(
                    claims.getSubject(),
                    usuario.getCorreo()
            );
            // Comprueba que la cuenta del usuario esté activa.
            boolean cuentaActiva = Boolean.TRUE.equals(
                    usuario.getActivo()
            );
            // El parser ya rechaza los tokens expirados.Esta comprobación adicional confirma la fecha.
            boolean noExpirado = claims.getExpiration() != null
                    && claims.getExpiration().after(new Date());
            return mismoUsuario && cuentaActiva && noExpirado;
        } catch (JwtException | IllegalArgumentException e) {
            // Si el token es inválido, está manipulado o expiró, la validación devuelve false.
            return false;
        }
    }
}
