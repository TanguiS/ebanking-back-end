package fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class RequestCardDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idRequestCard;
    private RequestPaymentMethodStatusDTO requestPaymentMethodStatus;
    private CardInformationDTO cardInformation;
}
