package fr.ensicaen.pi.gpss.backend.payload.response.banker;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BankerDashboardDTO extends TemplateDTO {
    @NotNull
    private final List<BankersClientDashboardDTO> clients;
    @NotEmpty
    private final List<CardProductDTO> cardProducts;
}
