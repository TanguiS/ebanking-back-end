package fr.ensicaen.pi.gpss.backend.database.dto.card_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AuthorizationType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.PriorityUseLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class AuthorizationPolicyDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idAuthorizationPolicy;
    private AuthorizationType authorizationType;
    private PriorityUseLevel priorityUseLevel;
    private AuthorizationPolicyRequirementDTO requirement;
    private CardProductDTO cardProduct;
}
