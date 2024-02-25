package fr.ensicaen.pi.gpss.backend.payload.request;

import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserCredentialUpdate(
        @Valid @NotNull Role role,
        @Valid @NotNull Status status
) {
}
