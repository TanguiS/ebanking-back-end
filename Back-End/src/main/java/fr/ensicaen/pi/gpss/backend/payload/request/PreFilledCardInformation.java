package fr.ensicaen.pi.gpss.backend.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PreFilledCardInformation(
        @Valid @NotNull @PositiveOrZero Integer expirationDateInYears,
        @Valid @NotNull @PositiveOrZero Integer upperLimitPerMonth,
        @Valid @NotNull @PositiveOrZero Integer upperLimitPerTransaction
) {
}
