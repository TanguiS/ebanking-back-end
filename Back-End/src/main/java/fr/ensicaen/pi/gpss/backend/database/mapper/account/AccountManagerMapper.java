package fr.ensicaen.pi.gpss.backend.database.mapper.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.ATMAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.AccountManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankerAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.ClientAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class AccountManagerMapper implements DTOEntityMapper<AccountManagerDTO, AccountManagerEntity> {
    private final BankerAccountMapper bankerAccountMapper;
    private final ClientAccountMapper clientAccountMapper;
    private final ATMAccountMapper atmAccountMapper;
    private final UserInformationMapper userInformationMapper;

    public AccountManagerMapper(
            @Lazy BankerAccountMapper bankerAccountMapper,
            @Lazy ClientAccountMapper clientAccountMapper,
            @Lazy ATMAccountMapper atmAccountMapper,
            @Lazy UserInformationMapper userInformationMapper
    ) {
        this.bankerAccountMapper = bankerAccountMapper;
        this.clientAccountMapper = clientAccountMapper;
        this.atmAccountMapper = atmAccountMapper;
        this.userInformationMapper = userInformationMapper;
    }

    @Override
    public AccountManagerDTO toDTO(AccountManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return AccountManagerDTO.builder()
                .idAccountManager(entity.getIdAccountManager())
                .bankerAccount(bankerAccountMapper.toDTO(entity.getBankerAccount()))
                .clientAccount(clientAccountMapper.toDTO(entity.getClientAccount()))
                .atmAccount(atmAccountMapper.toDTO(entity.getAtmAccount()))
                .usersInformation(userInformationMapper.toDTO(entity.getUsersInformation()))
                .build();
    }

    public AccountManagerDTO toDTOFromUserInformation(AccountManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return AccountManagerDTO.builder()
                .idAccountManager(entity.getIdAccountManager())
                .bankerAccount(bankerAccountMapper.toDTO(entity.getBankerAccount()))
                .clientAccount(clientAccountMapper.toDTOFromAccountManager(entity.getClientAccount()))
                .atmAccount(atmAccountMapper.toDTOFromAccountManager(entity.getAtmAccount()))
                .build();
    }

    @Override
    public AccountManagerEntity toEntity(AccountManagerDTO dto) {
        if (dto == null) {
            return null;
        }
        AccountManagerEntity entity = new AccountManagerEntity(
                clientAccountMapper.toEntity(dto.getClientAccount()),
                bankerAccountMapper.toEntity(dto.getBankerAccount()),
                userInformationMapper.toEntity(dto.getUsersInformation()),
                atmAccountMapper.toEntity(dto.getAtmAccount())
        );
        entity.setIdAccountManager(dto.getIdAccountManager());
        return entity;
    }

    public AccountManagerDTO toNewBanker(
            @NotNull BankerAccountDTO bankerAccount, @NotNull UserInformationDTO bankerInformation
    ) {
        return AccountManagerDTO.builder()
                .bankerAccount(bankerAccount)
                .usersInformation(bankerInformation)
                .build();
    }

    public AccountManagerDTO toNewClient(
            @NotNull ClientAccountDTO clientAccount, @NotNull UserInformationDTO clientInformation
    ) {
        return AccountManagerDTO.builder()
                .clientAccount(clientAccount)
                .usersInformation(clientInformation)
                .build();
    }

    public AccountManagerDTO toNewATMManager(
            @NotNull ATMAccountDTO atmAccount, @NotNull UserInformationDTO atmInformation
    ) {
        return AccountManagerDTO.builder()
                .atmAccount(atmAccount)
                .usersInformation(atmInformation)
                .build();
    }
}
