package fr.ensicaen.pi.gpss.backend.security.jwt;

import fr.ensicaen.pi.gpss.backend.expection_handler.InternalErrorEnum;
import fr.ensicaen.pi.gpss.backend.security.user_details.UserDetailsServiceImplementation;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImplementation userDetailsService;

    public AuthenticationTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImplementation userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(request);
            tryValidateToken(jwt);
            String username = jwtUtils.getUsernameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.setHeader(InternalErrorEnum.UNVALIDATED_JWT.name(), e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void tryValidateToken(String jwt) throws IllegalArgumentException {
        try {
            jwtUtils.validatedJwtToken(jwt);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty: " + e.getMessage());
        }
    }
}
