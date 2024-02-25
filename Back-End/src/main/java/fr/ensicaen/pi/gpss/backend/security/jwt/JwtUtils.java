package fr.ensicaen.pi.gpss.backend.security.jwt;

import fr.ensicaen.pi.gpss.backend.security.user_details.UserDetailsImplementation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${be.ebanking.app.jwtSecret}")
    private String jwtSecret;
    @Value("${be.ebanking.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(@NotNull Authentication authentication) {
        UserDetailsImplementation userPrincipal = (UserDetailsImplementation) authentication.getPrincipal();

        return Jwts
                .builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getHMACKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getHMACKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String getUsernameFromJwtToken(@NotBlank String token) {
        return Jwts.parserBuilder().setSigningKey(getHMACKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public void validatedJwtToken(@NotBlank String authToken) {
        Jwts.parserBuilder().setSigningKey(getHMACKey()).build().parseClaimsJws(authToken);
    }

    public String parseJwt(HttpServletRequest request) throws IllegalArgumentException {
        String headerAuth = request.getHeader(TokenFormat.BEARER.key());

        if (headerAuth == null) {
            throw new IllegalArgumentException("'Token' key header is missing");
        }
        if (!headerAuth.startsWith(TokenFormat.BEARER.prefix())) {
            throw new IllegalArgumentException("Token does not start with '" + TokenFormat.BEARER.prefix() + "'");
        }
        return headerAuth.substring(TokenFormat.BEARER.prefix().length());
    }
}
