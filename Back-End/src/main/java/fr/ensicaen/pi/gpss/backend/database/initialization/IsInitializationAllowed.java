package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IsInitializationAllowed {
    private final UserInformationRepository userInformationRepository;
    private boolean isAllowed;

    public IsInitializationAllowed(UserInformationRepository userInformationRepository) {
        this.userInformationRepository = userInformationRepository;
    }

    @PostConstruct
    public void initiate() {
        isAllowed = userInformationRepository.count() == 0;
    }

    @Bean
    public boolean isAllowed() {
        return isAllowed;
    }
}
