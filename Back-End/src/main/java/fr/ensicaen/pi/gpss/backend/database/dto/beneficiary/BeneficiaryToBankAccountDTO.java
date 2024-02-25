package fr.ensicaen.pi.gpss.backend.database.dto.beneficiary;

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
public class BeneficiaryToBankAccountDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idBeneficiaryToBankAccount;
    private BeneficiaryDTO beneficiary;
    private BankAccountDTO bankAccount;
}
