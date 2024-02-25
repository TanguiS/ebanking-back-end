package fr.ensicaen.pi.gpss.backend.controller.api_access;

import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.payload.request.UserRegistrationForm;
import fr.ensicaen.pi.gpss.backend.payload.response.SingleResponseBuilder;
import fr.ensicaen.pi.gpss.backend.service.api_access.RegisterUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@Validated
public class RegisterController {
    private final RegisterUserService registerUserService;
    private final SingleResponseBuilder responseBuilder;

    public RegisterController(RegisterUserService registerUserService, SingleResponseBuilder responseBuilder) {
        this.registerUserService = registerUserService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping()
    public ResponseEntity<String> addClient(@Valid @NotNull @RequestBody UserRegistrationForm userData)
            throws IllegalArgumentException, BackOfficeExceptionWrapper {
        registerUserService.saveUser(userData);
        return responseBuilder.setRegisterRequest().setAcceptedStatus().build();
    }
}