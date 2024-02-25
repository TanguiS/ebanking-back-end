package fr.ensicaen.pi.gpss.backend.payload.request;


import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.Iban;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PreFilledBeneficiary (
        @Valid @NotBlank String firstName,
        @Valid @NotBlank String lastName,
        @Valid @NotBlank @Iban String iban
) {
}
