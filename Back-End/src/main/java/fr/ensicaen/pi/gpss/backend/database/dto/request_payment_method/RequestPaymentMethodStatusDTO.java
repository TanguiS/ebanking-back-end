package fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class RequestPaymentMethodStatusDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idRequestPaymentMethodStatus;
    private Timestamp userRequestPaymentMethodDate;
    private Timestamp bankRequestPaymentMethodDate;
    private Timestamp bankReceivedPaymentMethodDate;
    private Timestamp userReceivedPaymentMethodDate;
    private OrderStatus orderStatus;
}
