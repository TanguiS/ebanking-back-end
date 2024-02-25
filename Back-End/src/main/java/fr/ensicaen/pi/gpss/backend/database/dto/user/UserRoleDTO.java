package fr.ensicaen.pi.gpss.backend.database.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class UserRoleDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idRole;
    private Role roleType;
    @JsonIgnore
    private UserInformationDTO userInformation;
}
