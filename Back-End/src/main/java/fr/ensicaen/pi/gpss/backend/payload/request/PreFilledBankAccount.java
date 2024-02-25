package fr.ensicaen.pi.gpss.backend.payload.request;

import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PreFilledBankAccount (
    @Valid @NotNull @PositiveOrZero Integer amount,
    @Valid @NotNull BankAccountType bankAccountType
) {
}
