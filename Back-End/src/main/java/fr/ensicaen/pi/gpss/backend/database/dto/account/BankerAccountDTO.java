package fr.ensicaen.pi.gpss.backend.database.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class BankerAccountDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idBankerAccount;
    private UserInformationDTO userInformation;
}
