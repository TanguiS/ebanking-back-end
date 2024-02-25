package fr.ensicaen.pi.gpss.backend.security.user_details;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserInformationRepository userInformationRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInformationEntity> users = userInformationRepository.findByEmailIsLikeIgnoreCase(username);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found, specified username aka email : " + username);
        }

        return UserDetailsImplementation.build(users.get());
    }
}
