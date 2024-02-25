package fr.ensicaen.pi.gpss.backend.database.dto.direct_debit;

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
public class LegalEntityDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idLegalEntity;
    private String name;
    private String numberSiret;
}
