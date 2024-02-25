package fr.ensicaen.pi.gpss.backend.database.mapper.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.ClientAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.ClientAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ClientAccountMapper implements DTOEntityMapper<ClientAccountDTO, ClientAccountEntity> {
    private final AccountManagerMapper accountManagerMapper;
    private final BankAccountMapper bankAccountMapper;

    public ClientAccountMapper(@Lazy AccountManagerMapper accountManagerMapper, BankAccountMapper bankAccountMapper) {
        this.accountManagerMapper = accountManagerMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public ClientAccountDTO toDTO(ClientAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return ClientAccountDTO.builder()
                .idClientAccount(entity.getIdClientAccount())
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .accountManager(accountManagerMapper.toDTO(entity.getAccountManager()))
                .build();
    }

    @Override
    public ClientAccountEntity toEntity(ClientAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        ClientAccountEntity entity = new ClientAccountEntity(
                accountManagerMapper.toEntity(dto.getAccountManager()),
                bankAccountMapper.toEntity(dto.getBankAccount())
        );
        entity.setIdClientAccount(dto.getIdClientAccount());
        return entity;
    }

    public ClientAccountDTO toDTOFromAccountManager(ClientAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return ClientAccountDTO.builder()
                .idClientAccount(entity.getIdClientAccount())
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public ClientAccountDTO toNew(@NotNull BankAccountDTO bankAccount) {
        return ClientAccountDTO.builder()
                .bankAccount(bankAccount)
                .build();
    }
}
