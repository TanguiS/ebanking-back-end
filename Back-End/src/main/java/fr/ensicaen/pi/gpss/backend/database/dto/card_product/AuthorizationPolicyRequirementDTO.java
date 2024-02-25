package fr.ensicaen.pi.gpss.backend.database.dto.card_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class AuthorizationPolicyRequirementDTO extends TemplateDTO {
    @Positive
    private Integer lowerConsecutiveOfflineLimit;
    @Positive
    private Integer upperConsecutiveOfflineLimit;
    @Positive
    private Integer cumulativeTotalTransactionAmountLimit;
    @Positive
    private Integer cumulativeTotalTransactionAmountUpperLimit;
}
