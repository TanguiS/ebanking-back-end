package fr.ensicaen.pi.gpss.backend.database.mapper.user;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.AccountManagerMapper;
import fr.ensicaen.pi.gpss.backend.payload.request.UserRegistrationForm;
import fr.ensicaen.pi.gpss.backend.tools.security.PasswordEncoderBean;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class UserInformationMapper implements DTOEntityMapper<UserInformationDTO, UserInformationEntity> {
    @Value("${be.ebanking.app.password.atm.default}")
    private String atmDefaultPassword;
    private final UserRoleMapper userRoleMapper;
    private final AccountManagerMapper accountManagerMapper;
    private final PasswordEncoder passwordEncoder;

    public UserInformationMapper(
            UserRoleMapper userRoleMapper,
            @Lazy AccountManagerMapper accountManagerMapper,
            PasswordEncoderBean passwordEncoderBean
    ) {
        this.userRoleMapper = userRoleMapper;
        this.accountManagerMapper = accountManagerMapper;
        passwordEncoder = passwordEncoderBean.getPasswordEncoder();
    }

    @Override
    public UserInformationDTO toDTO(UserInformationEntity entity) {
        if (entity == null) {
            return null;
        }
        UserInformationDTO.UserInformationDTOBuilder builder = UserInformationDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .role(userRoleMapper.toDTOCalledByUser(entity.getRole()))
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .requestStatus(entity.getRequestStatus());
        if (entity.getAccountsManager() instanceof AccountManagerEntity) {
            builder.accountsManager(entity.getAccountsManager().stream()
                            .map(accountManagerMapper::toDTOFromUserInformation)
                            .toList()
            );
        }
        return builder.build();
    }

    @Override
    public UserInformationEntity toEntity(UserInformationDTO dto) {
        if (dto == null) {
            return null;
        }
        List<AccountManagerEntity> accountManager = null;
        if (dto.getAccountsManager() != null) {
            accountManager = dto.getAccountsManager().stream()
                    .map(accountManagerMapper::toEntity)
                    .toList();
        }
        UserInformationEntity entity = new UserInformationEntity(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPassword(),
                dto.getEmail(),
                dto.getGender(),
                dto.getRequestStatus(),
                dto.getPhoneNumber(),
                userRoleMapper.toEntity(dto.getRole()),
                accountManager
        );
        entity.setIdUser(dto.getIdUser());
        return entity;
    }

    public UserInformationDTO toDTOFromBankerAccount(@NotNull UserInformationEntity entity) {
        return UserInformationDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .phoneNumber(entity.getPhoneNumber())
                .requestStatus(entity.getRequestStatus())
                .build();
    }

    public UserInformationDTO toNewDTOFromRegistrationForm(@NotNull UserRegistrationForm userForm) {
        return UserInformationDTO.builder()
                .phoneNumber(userForm.phoneNumber())
                .email(userForm.email())
                .gender(userForm.gender())
                .firstName(userForm.firstName())
                .requestStatus(Status.PENDING)
                .lastName(userForm.lastName())
                .password(passwordEncoder.encode(userForm.password()))
                .build();
    }

    public UserInformationDTO toNewATM(@PositiveOrZero int countATM) {
        return UserInformationDTO.builder()
                .email(String.format("atm.account.%d@ensibank.pro.fr", countATM))
                .gender(0)
                .lastName("Account")
                .firstName("ATM")
                .password(passwordEncoder.encode(atmDefaultPassword))
                .phoneNumber("00")
                .requestStatus(Status.AUTHORIZED)
                .build();
    }

    public UserInformationDTO toBasicDashboard(UserInformationEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserInformationDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .role(userRoleMapper.toDTOCalledByUser(entity.getRole()))
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    public UserInformationDTO toUserManagerDashboard(UserInformationEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserInformationDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .role(userRoleMapper.toDTOCalledByUser(entity.getRole()))
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .requestStatus(entity.getRequestStatus())
                .build();
    }
}
