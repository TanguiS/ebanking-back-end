package fr.ensicaen.pi.gpss.backend.service.api_access;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.UserLoginForm;
import fr.ensicaen.pi.gpss.backend.tools.security.PasswordEncoderBean;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class LoginUserService extends UserCommonServices {
    private final PasswordEncoder passwordEncoder;
    private final UserInformationMapper userInformationMapper;

    public LoginUserService(
            UserInformationRepository userRepository,
            PasswordEncoderBean passwordEncoderBean,
            UserInformationMapper userInformationMapper,
            UserInformationMapper userInformationMapper1
    ) {
        super(userRepository, userInformationMapper);
        passwordEncoder = passwordEncoderBean.getPasswordEncoder();
        this.userInformationMapper = userInformationMapper1;
    }


    public boolean checkPassword(@Valid @NotNull UserLoginForm userData) {
        UserInformationDTO user = super.getClientByEmail(userData.email());
        return passwordEncoder.matches(userData.password(), user.getPassword());
    }

    public boolean hasAuthorizedStatus(@Valid @NonNull UserLoginForm userData) {
        UserInformationDTO user = super.getClientByEmail(userData.email());
        return user.getRequestStatus().id() == Status.AUTHORIZED.id();
    }

    public UserInformationDTO getOutput(@Valid @NotNull UserLoginForm userData) {
        UserInformationDTO user = super.getClientByEmail(userData.email());
        return userInformationMapper.toBasicDashboard(
                userInformationMapper.toEntity(user)
        );
    }
}
