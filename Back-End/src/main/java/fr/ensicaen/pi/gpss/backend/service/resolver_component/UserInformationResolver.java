package fr.ensicaen.pi.gpss.backend.service.resolver_component;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UserInformationResolver {
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpServletRequest;

    public UserInformationResolver(
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            JwtUtils jwtUtils,
            HttpServletRequest httpServletRequest
    ) {
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        this.jwtUtils = jwtUtils;
        this.httpServletRequest = httpServletRequest;
    }

    public UserInformationDTO resolve(){
        return userInformationMapper.toDTO(
                userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                        jwtUtils.getUsernameFromJwtToken(jwtUtils.parseJwt(httpServletRequest))
                )
        );
    }
}
