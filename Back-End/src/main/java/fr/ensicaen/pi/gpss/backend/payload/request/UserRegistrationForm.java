package fr.ensicaen.pi.gpss.backend.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserRegistrationForm (
        @Valid @NotBlank String firstName,
        @Valid @NotBlank String lastName,
        @Valid @NotBlank @Email String email,
        @Valid @NotBlank @Size(min = 8) String password,
        @Valid @NotNull @PositiveOrZero Integer gender,
        @Valid @NotBlank String phoneNumber
) {
}
