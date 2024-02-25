package fr.ensicaen.pi.gpss.backend.payload.response.user_manager;

import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserManagerDashboardDTO extends TemplateDTO {
    private final List<UserInformationDTO> authorizedClients;
    private final List<UserInformationDTO> authorizedBankPersonals;
    private final List<UserInformationDTO> blockedUsers;
    private final List<UserInformationDTO> pendingUsers;
    private final List<Role> availableRoles;
    private final List<Status> availableStatus;
}
