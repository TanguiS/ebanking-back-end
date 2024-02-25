package fr.ensicaen.pi.gpss.backend.database.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.AccountManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encoded.Encoded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class UserInformationDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idUser;
    private String firstName;
    private String lastName;
    @Encoded
    private String password;
    @Email
    private String email;
    @PositiveOrZero
    private int gender;
    private Status requestStatus;
    @NotBlank
    private String phoneNumber;
    private UserRoleDTO role;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AccountManagerDTO> accountsManager;
}
