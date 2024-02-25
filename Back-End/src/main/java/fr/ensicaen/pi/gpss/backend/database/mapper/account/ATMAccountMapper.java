package fr.ensicaen.pi.gpss.backend.database.mapper.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.ATMAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.ATMAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ATMAccountMapper implements DTOEntityMapper<ATMAccountDTO, ATMAccountEntity> {
    private final AccountManagerMapper accountManagerMapper;
    private final BankAccountMapper bankAccountMapper;

    public ATMAccountMapper(
            @Lazy AccountManagerMapper accountManagerMapper,
            BankAccountMapper bankAccountMapper
    ) {
        this.accountManagerMapper = accountManagerMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public ATMAccountDTO toDTO(ATMAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return ATMAccountDTO.builder()
                .idATMAccount(entity.getIdATMAccount())
                .accountManager(accountManagerMapper.toDTO(entity.getAccountManager()))
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public ATMAccountDTO toDTOFromAccountManager(ATMAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return ATMAccountDTO.builder()
                .idATMAccount(entity.getIdATMAccount())
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public ATMAccountDTO toNew(@NotNull BankAccountDTO bankAccount) {
        return ATMAccountDTO.builder()
                .bankAccount(bankAccount)
                .build();
    }

    @Override
    public ATMAccountEntity toEntity(ATMAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        ATMAccountEntity entity = new ATMAccountEntity(
                accountManagerMapper.toEntity(dto.getAccountManager()),
                bankAccountMapper.toEntity(dto.getBankAccount())
        );
        entity.setIdATMAccount(entity.getIdATMAccount());
        return entity;
    }
}
