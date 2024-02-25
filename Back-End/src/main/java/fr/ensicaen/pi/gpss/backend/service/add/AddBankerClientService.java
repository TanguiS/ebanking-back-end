package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.database.dto.account.AccountManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankerAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.AccountManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankerAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddBankerClientService {
    private final AccountManagerMapper accountManagerMapper;
    private final AccountManagerRepository accountManagerRepository;
    private final BankerAccountMapper bankerAccountMapper;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;

    public AddBankerClientService(
            AccountManagerMapper accountManagerMapper,
            AccountManagerRepository accountManagerRepository,
            BankerAccountMapper bankerAccountMapper,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository
    ) {
        this.accountManagerMapper = accountManagerMapper;
        this.accountManagerRepository = accountManagerRepository;
        this.bankerAccountMapper = bankerAccountMapper;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
    }

    private void validateUser(
            @Valid @NotNull UserInformationDTO banker, @Valid @NotBlank String errorMessage
    ) throws IllegalArgumentException {
        if (userInformationRepository.countAllByEmailIsLikeIgnoreCase(banker.getEmail()) == 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (banker.getRole() == null) {
            throw new IllegalArgumentException("user is not an official & registered user");
        }
    }

    private void validateBanker(@Valid @NotNull UserInformationDTO banker) throws IllegalArgumentException {
        if (banker.getRole().getRoleType() != Role.ROLE_BANKER) {
            throw new IllegalArgumentException("Banker should be a banker");
        }
    }

    public UserInformationDTO addNewClient(
            @Valid @NotNull UserInformationDTO banker, @Valid @NotNull UserInformationDTO client
    ) {
        validateUser(banker, "Banker does not exists");
        validateBanker(banker);
        validateUser(client, "Client does not exists");
        BankerAccountDTO newBankerAccount = bankerAccountMapper.toNewBankerClient(client);
        AccountManagerDTO newBankerManager = accountManagerMapper.toNewBanker(newBankerAccount, banker);
        accountManagerMapper.toDTO(accountManagerRepository.save(accountManagerMapper.toEntity(newBankerManager)));
        UserInformationEntity user = userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                banker.getEmail()
        );
        return userInformationMapper.toDTO(user);
    }


}
