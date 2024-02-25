package fr.ensicaen.pi.gpss.backend.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginForm(
        @Valid @NotNull @Email String email,
        @Valid @NotBlank @Size(min = 8) String password
) {
}
