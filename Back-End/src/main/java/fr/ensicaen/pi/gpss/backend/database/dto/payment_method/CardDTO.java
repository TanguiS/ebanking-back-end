package fr.ensicaen.pi.gpss.backend.database.dto.payment_method;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class CardDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idCard;
    private CardStatus cardStatus;
    private CardInformationDTO cardInformation;
}
