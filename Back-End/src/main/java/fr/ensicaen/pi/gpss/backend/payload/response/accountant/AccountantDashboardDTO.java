package fr.ensicaen.pi.gpss.backend.payload.response.accountant;


import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountantDashboardDTO extends TemplateDTO {
    @NotNull
    private final List<TransactionTupleDTO> transactionTuple;
    @NotNull @PositiveOrZero
    private final Integer maxPossiblePagesWithCurrentParameters;
}
