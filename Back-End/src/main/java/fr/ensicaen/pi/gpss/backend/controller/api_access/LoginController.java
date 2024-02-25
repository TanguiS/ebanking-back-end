package fr.ensicaen.pi.gpss.backend.controller.api_access;

import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.IllegalLoginFormException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UnauthorizedLoginException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UserNotFoundException;
import fr.ensicaen.pi.gpss.backend.expection_handler.login.exception.UserWrongPasswordException;
import fr.ensicaen.pi.gpss.backend.payload.request.UserLoginForm;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.security.jwt.JwtUtils;
import fr.ensicaen.pi.gpss.backend.service.api_access.LoginUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Validated
public class LoginController {
    private final LoginUserService loginService;
    private final SingleResponseBuilder responseBuilder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public LoginController(
            LoginUserService loginService,
            SingleResponseBuilder responseBuilder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils
    ) {
        this.loginService = loginService;
        this.responseBuilder = responseBuilder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<String> login(@Valid @NotNull @RequestBody UserLoginForm userData)
            throws IllegalLoginFormException,
            UserWrongPasswordException
    {
        if (!loginService.isClientExists(userData.email())) {
            throw new UserNotFoundException();
        }
        if (!loginService.hasAuthorizedStatus(userData)) {
            throw new UnauthorizedLoginException();
        }
        if (!loginService.checkPassword(userData)) {
            throw new UserWrongPasswordException();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userData.email(), userData.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return responseBuilder
                .setLoginRequest()
                .setOkStatus()
                .setBearerAccessToken(jwt)
                .setData(loginService.getOutput(userData))
                .build();
    }
}