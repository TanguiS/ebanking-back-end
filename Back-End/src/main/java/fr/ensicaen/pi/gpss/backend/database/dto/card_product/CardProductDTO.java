package fr.ensicaen.pi.gpss.backend.database.dto.card_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class CardProductDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idCardProduct;
    @NotBlank
    private String name;
    private CardType cardType;
    @Positive
    private Integer contactlessUpperLimitPerTransaction;
    @Positive
    private Integer numberOfContactlessTransactionBeforeAskingPin;
    @NotEmpty
    private List<SoftwareCardDTO> softwareCards;
    @NotEmpty
    private List<AuthorizationPolicyDTO> authorizationPolicies;
}
