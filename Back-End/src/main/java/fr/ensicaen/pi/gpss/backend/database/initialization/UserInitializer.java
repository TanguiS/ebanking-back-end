package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserRoleDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserRoleMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserRoleRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.UserRegistrationForm;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({
        "getMasterForm",
        "getTransactionCollectorForm",
        "getSimulatorForm",
        "getBankerForm",
        "getAccountantForm",
        "getBabylonClientForm"}
)
public class UserInitializer {
    private final UserInformationRepository userInformationRepository;
    private final UserFormComponent userFormComponent;
    private final UserInformationMapper userInformationMapper;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private final boolean isAllowed;

    public UserInitializer(
            UserInformationRepository userInformationRepository,
            UserInformationMapper userInformationMapper,
            UserRoleRepository userRoleRepository,
            UserRoleMapper userRoleMapper,
            UserFormComponent userFormComponent,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.userInformationRepository = userInformationRepository;
        this.userInformationMapper = userInformationMapper;
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
        this.userFormComponent = userFormComponent;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        if (isUserFormDoesNotExist(userFormComponent.getMasterForm())) {
            initiateUser(userFormComponent.getMasterForm(), Role.ROLE_MASTER);
        }
        if (isUserFormDoesNotExist(userFormComponent.getAccountantForm())) {
            initiateUser(userFormComponent.getAccountantForm(), Role.ROLE_ACCOUNTANT);
        }
        if (isUserFormDoesNotExist(userFormComponent.getBankerForm())) {
            initiateUser(userFormComponent.getBankerForm(), Role.ROLE_BANKER);
        }
        if (isUserFormDoesNotExist(userFormComponent.getSimulatorForm())) {
            initiateUser(userFormComponent.getSimulatorForm(), Role.ROLE_SIMULATOR);
        }
        if (isUserFormDoesNotExist(userFormComponent.getTransactionCollectorForm())) {
            initiateUser(userFormComponent.getTransactionCollectorForm(), Role.ROLE_TRANSACTION_COLLECTOR);
        }
        if (isUserFormDoesNotExist(userFormComponent.getBabylonClientForm())) {
            initiateUser(userFormComponent.getBabylonClientForm(), Role.ROLE_CLIENT);
        }
        if (isUserFormDoesNotExist(userFormComponent.getBabylonJuniorClientForm())) {
            initiateUser(userFormComponent.getBabylonJuniorClientForm(), Role.ROLE_CLIENT);
        }
    }

    private boolean isUserFormDoesNotExist(@Valid @NotNull UserRegistrationForm form) {
        return userInformationRepository.countAllByEmailIsLikeIgnoreCase(form.email()) == 0;
    }

    private UserInformationDTO buildAuthorizedUser(@Valid @NotNull @NotNull UserRegistrationForm form) {
        return UserInformationDTO.builder()
                .firstName(form.firstName())
                .lastName(form.lastName())
                .email(form.email())
                .gender(form.gender())
                .phoneNumber(form.phoneNumber())
                .password(form.password())
                .requestStatus(Status.AUTHORIZED)
                .build();
    }

    private UserInformationEntity getUserInformationEntity(UserRegistrationForm form) {
        UserInformationDTO user = buildAuthorizedUser(form);
        return userInformationRepository.save(userInformationMapper.toEntity(user));
    }

    private void initiateUser(@NotNull UserRegistrationForm form, @NotNull Role roleType) {
        UserInformationEntity save = getUserInformationEntity(form);
        UserRoleDTO role = userRoleMapper.toNew(userInformationMapper.toDTO(save), roleType);
        userRoleRepository.save(userRoleMapper.toEntity(role));
    }

    public UserInformationDTO getBankerDTO() {
        UserInformationEntity user = userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                userFormComponent.getBankerForm().email()
        );
        return userInformationMapper.toDTO(user);
    }

    public UserInformationDTO getBabylonDTO() {
        UserInformationEntity user = userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                userFormComponent.getBabylonClientForm().email()
        );
        return userInformationMapper.toDTO(user);
    }

    public UserInformationDTO getBabylonJuniorDTO() {
        UserInformationEntity user = userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                userFormComponent.getBabylonJuniorClientForm().email()
        );
        return userInformationMapper.toDTO(user);
    }
}
