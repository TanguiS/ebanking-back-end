package fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.DirectDebitToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DirectDebitToBankAccountMapper
        implements DTOEntityMapper<DirectDebitToBankAccountDTO, DirectDebitToBankAccountEntity> {
    private final DirectDebitMapper directDebitMapper;
    private final BankAccountMapper bankAccountMapper;

    public DirectDebitToBankAccountMapper(
            DirectDebitMapper directDebitMapper,
            @Lazy BankAccountMapper bankAccountMapper
    ) {
        this.directDebitMapper = directDebitMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public DirectDebitToBankAccountDTO toDTO(DirectDebitToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return DirectDebitToBankAccountDTO.builder()
                .idDirectDebitToBankAccount(entity.getIdDirectDebitToBankAccount())
                .directDebit(directDebitMapper.toDTO(entity.getDirectDebit()))
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public DirectDebitToBankAccountDTO toDTOFromBankAccount(DirectDebitToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return DirectDebitToBankAccountDTO.builder()
                .idDirectDebitToBankAccount(entity.getIdDirectDebitToBankAccount())
                .directDebit(directDebitMapper.toDTO(entity.getDirectDebit()))
                .build();
    }

    @Override
    public DirectDebitToBankAccountEntity toEntity(DirectDebitToBankAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        DirectDebitToBankAccountEntity entity = new DirectDebitToBankAccountEntity(
                directDebitMapper.toEntity(dto.getDirectDebit()),
                bankAccountMapper.toEntity(dto.getBankAccount())
        );
        entity.setIdDirectDebitToBankAccount(dto.getIdDirectDebitToBankAccount());
        return entity;
    }

    public DirectDebitToBankAccountDTO toNew(
            @NotNull DirectDebitDTO directDebit, @NotNull BankAccountDTO bankAccount
    ) {
        return DirectDebitToBankAccountDTO.builder()
                .directDebit(directDebit)
                .bankAccount(bankAccount)
                .build();
    }

    public DirectDebitToBankAccountDTO toDashboard(DirectDebitToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return DirectDebitToBankAccountDTO.builder()
                .idDirectDebitToBankAccount(entity.getIdDirectDebitToBankAccount())
                .directDebit(directDebitMapper.toDashboard(entity.getDirectDebit()))
                .build();
    }
}
