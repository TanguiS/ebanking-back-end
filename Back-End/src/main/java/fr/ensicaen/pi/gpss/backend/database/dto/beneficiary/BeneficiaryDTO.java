package fr.ensicaen.pi.gpss.backend.database.dto.beneficiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class BeneficiaryDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idBeneficiary;
    private String firstName;
    private String lastName;
    private String iban;
    private String rib;
    @PositiveOrZero
    private Integer idBankAccount;
}
