package fr.ensicaen.pi.gpss.backend.security.jwt;

import fr.ensicaen.pi.gpss.backend.expection_handler.InternalErrorEnum;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointJwt.class);
    private final SingleResponseBuilder responseBuilder;

    public AuthenticationEntryPointJwt(SingleResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        String body = null;
        if (response.getHeader(InternalErrorEnum.UNVALIDATED_JWT.name()) != null) {
            body = response.getHeader(InternalErrorEnum.UNVALIDATED_JWT.name());
        }

        ResponseEntity<String> built = responseBuilder.setUnresolvedMappingRequest()
                .setUnauthorizedStatus().setData(body).build();
        String responseBody = built.getBody();
        HttpStatusCode responseStatus = built.getStatusCode();

        assert responseBody != null;
        response.getWriter().write(responseBody);
        response.setStatus(responseStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
