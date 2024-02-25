package fr.ensicaen.pi.gpss.backend.database.dto.card_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.database.enumerate.PriorityUseLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class SoftwareCardDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idSoftwareCard;
    private CardScheme cardScheme;
    private PriorityUseLevel priorityUseLevel;
    private CardProductDTO cardProduct;
}
