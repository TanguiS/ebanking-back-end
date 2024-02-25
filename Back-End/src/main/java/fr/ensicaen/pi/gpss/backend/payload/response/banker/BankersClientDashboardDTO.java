package fr.ensicaen.pi.gpss.backend.payload.response.banker;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BankersClientDashboardDTO extends TemplateDTO {
    @NotNull
    private final UserInformationDTO client;
    @NotNull
    private final List<BankAccountDTO> bankAccounts;
}
