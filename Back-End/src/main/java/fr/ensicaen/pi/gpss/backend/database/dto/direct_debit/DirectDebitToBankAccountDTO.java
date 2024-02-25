package fr.ensicaen.pi.gpss.backend.database.dto.direct_debit;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class DirectDebitToBankAccountDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idDirectDebitToBankAccount;
    private DirectDebitDTO directDebit;
    private BankAccountDTO bankAccount;
}
