package fr.ensicaen.pi.gpss.backend.payload.response.accountant;


import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionTupleDTO extends TemplateDTO {
        @NotNull
        private final TransactionDTO debtor;
        @NotNull
        private final TransactionDTO creditor;
}
