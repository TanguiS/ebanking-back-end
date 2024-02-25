package fr.ensicaen.pi.gpss.backend.service.api_access;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import jakarta.validation.constraints.Email;

import java.util.Optional;

public abstract class UserCommonServices {
    private final UserInformationRepository userInformationRepository;
    private final UserInformationMapper userInformationMapper;

    protected UserCommonServices(
            UserInformationRepository userRepository,
            UserInformationMapper userInformationMapper
    ) {
        userInformationRepository = userRepository;
        this.userInformationMapper = userInformationMapper;
    }

    public boolean isClientExists(@Email String email) {
        return  userInformationRepository.countAllByEmailIsLikeIgnoreCase(email) == 1;
    }

    protected UserInformationDTO getClientByEmail(@Email String email) throws IllegalArgumentException {
        Optional<UserInformationEntity> user = findClientByMail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("No client found");
        }
        return userInformationMapper.toDTO(user.get());
    }

    private Optional<UserInformationEntity> findClientByMail(@Email String email) {
        return Optional.ofNullable(userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(email));
    }


}
