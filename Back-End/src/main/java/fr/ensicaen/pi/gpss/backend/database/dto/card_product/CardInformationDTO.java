package fr.ensicaen.pi.gpss.backend.database.dto.card_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class CardInformationDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idCardInformation;
    private String pan;
    private Timestamp expirationCardDate;
    @Positive
    private Integer cvx2;
    @Positive
    private Integer upperLimitPerMonth;
    @Positive
    private Integer upperLimitPerTransaction;
    private CardProductDTO cardProduct;
    //NOT ENTITY
    @Positive
    private Integer expirationDateInYears;
}
