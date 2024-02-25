package fr.ensicaen.pi.gpss.backend.service.api_access;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.expection_handler.BackOfficeExceptionWrapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.register.exception.AlreadyRegisteredException;
import fr.ensicaen.pi.gpss.backend.expection_handler.register.exception.PendingRegistrationException;
import fr.ensicaen.pi.gpss.backend.expection_handler.register.exception.UnauthorizedRegistrationException;
import fr.ensicaen.pi.gpss.backend.payload.request.UserRegistrationForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RegisterUserService extends UserCommonServices {
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;

    public RegisterUserService(
            UserInformationRepository userInformationRepository,
            UserInformationMapper userInformationMapper
    ){
        super(userInformationRepository, userInformationMapper);
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
    }

    public UserInformationDTO getClient(@Valid @NotNull UserRegistrationForm userToFind) {
        return super.getClientByEmail(userToFind.email());
    }

    private BackOfficeExceptionWrapper getException(@Valid @PositiveOrZero int status) throws IllegalArgumentException {
        if (status == Status.PENDING.id()) {
            return new PendingRegistrationException();
        }
        if (status == Status.UNAUTHORIZED.id()) {
            return new UnauthorizedRegistrationException();
        }
        if (status == Status.AUTHORIZED.id()) {
            return new AlreadyRegisteredException();
        }
        throw new IllegalArgumentException();
    }

    public void handleClientExists(@NotNull UserRegistrationForm userForm) throws IllegalArgumentException {
        UserInformationDTO user = getClient(userForm);
        throw this.getException(user.getRequestStatus().id());
    }
    public void saveUser(@NotNull UserRegistrationForm userData) {
        if (super.isClientExists(userData.email())) {
            handleClientExists(userData);
        }
        UserInformationDTO dto = userInformationMapper.toNewDTOFromRegistrationForm(userData);
        userInformationRepository.save(userInformationMapper.toEntity(dto));
    }
}
