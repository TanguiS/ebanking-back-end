package fr.ensicaen.pi.gpss.backend.service.account;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserRoleMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.UserCredentialUpdate;
import fr.ensicaen.pi.gpss.backend.payload.response.user_manager.UserManagerDashboardDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Validated
public class UserManagerService {
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final UserRoleMapper userRoleMapper;

    public UserManagerService(
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            UserRoleMapper userRoleMapper
    ) {
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        this.userRoleMapper = userRoleMapper;
    }

    private static List<UserInformationDTO> getFormattedUsers(List<UserInformationDTO> authorizedClients) {
        return authorizedClients.isEmpty() ? null : authorizedClients;
    }

    private static List<UserInformationDTO> getFilteredUsers(
            List<UserInformationDTO> users, Predicate<UserInformationDTO> userFilter
    ) {
        List<UserInformationDTO> authorizedClients = users.stream()
                .filter(userFilter)
                .toList();
        users.removeAll(authorizedClients);
        return authorizedClients;
    }

    public UserManagerDashboardDTO getUserManagerDashboard() {
        List<UserInformationDTO> users = userInformationRepository.findAll().stream()
                .map(userInformationMapper::toUserManagerDashboard)
                .collect(Collectors.toCollection(ArrayList::new));
        List<UserInformationDTO> authorizedClients = getFilteredUsers(
                users, value -> value.getRequestStatus() == Status.AUTHORIZED &&
                        value.getRole().getRoleType() == Role.ROLE_CLIENT
        );
        List<UserInformationDTO> authorizedBankPersonals = getFilteredUsers(
                users, value -> value.getRequestStatus() == Status.AUTHORIZED
        );
        List<UserInformationDTO> pendingUsers = getFilteredUsers(
                users, value -> value.getRequestStatus() == Status.PENDING
        );
        return new UserManagerDashboardDTO(
                getFormattedUsers(authorizedClients),
                getFormattedUsers(authorizedBankPersonals),
                getFormattedUsers(users),
                getFormattedUsers(pendingUsers),
                Arrays.stream(Role.values()).toList(),
                Arrays.stream(Status.values()).toList()
        );
    }

    public UserInformationDTO updateUserCredentialToAuthorizeWithRole(
            @Valid @NotNull @PositiveOrZero Integer idUser,
            @Valid @NotNull UserCredentialUpdate credential
    ) throws IllegalArgumentException {
        //Basic Service (You can block users without any consequences on the other TABLE / service, ex BANKER still have him as client
        Optional<UserInformationEntity> byId = userInformationRepository.findById(idUser);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }
        if (byId.get().getRequestStatus() == credential.status()) {
            throw new IllegalArgumentException("User already has this status");
        }
        UserInformationEntity user = byId.get();
        user.setRequestStatus(credential.status());

        if (user.getRole() == null) {
            user.setRole(userRoleMapper.toEntity(userRoleMapper.toNew(
                    userInformationMapper.toDTO(user), credential.role()
            )));
        }

        return userInformationMapper.toUserManagerDashboard(
                userInformationRepository.save(user)
        );
    }

}
