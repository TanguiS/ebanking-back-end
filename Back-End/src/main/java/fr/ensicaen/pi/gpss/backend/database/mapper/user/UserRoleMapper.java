package fr.ensicaen.pi.gpss.backend.database.mapper.user;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserRoleDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserRoleEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class UserRoleMapper implements DTOEntityMapper<UserRoleDTO, UserRoleEntity> {
    private final UserInformationMapper userInformationMapper;

    public UserRoleMapper(@Lazy UserInformationMapper userInformationMapper) {
        this.userInformationMapper = userInformationMapper;
    }

    @Override
    public UserRoleDTO toDTO(UserRoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserRoleDTO.builder()
                .idRole(entity.getIdRole())
                .roleType(entity.getRoleType())
                .userInformation(userInformationMapper.toDTO(entity.getUserInformation()))
                .build();
    }

    @Override
    public UserRoleEntity toEntity(UserRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        UserRoleEntity entity = new UserRoleEntity(
                dto.getRoleType(),
                userInformationMapper.toEntity(dto.getUserInformation())
        );
        entity.setIdRole(dto.getIdRole());
        return entity;
    }

    public UserRoleDTO toDTOCalledByUser(UserRoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserRoleDTO.builder()
                .idRole(entity.getIdRole())
                .roleType(entity.getRoleType())
                .build();
    }

    public UserRoleDTO toNew(@NotNull UserInformationDTO userInformation, Role role) {
        if (userInformation.getIdUser() == null) {
            throw new IllegalArgumentException("User Information should be persisted");
        }
        return UserRoleDTO.builder()
                .roleType(role)
                .userInformation(userInformation)
                .build();
    }
}
