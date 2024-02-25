package fr.ensicaen.pi.gpss.backend.database.dto.direct_debit;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Schedule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class DirectDebitDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idDirectDebit;
    private String iban;
    private Schedule recurrence;
    private IndividualDTO individual;
    private LegalEntityDTO legalEntity;
}
