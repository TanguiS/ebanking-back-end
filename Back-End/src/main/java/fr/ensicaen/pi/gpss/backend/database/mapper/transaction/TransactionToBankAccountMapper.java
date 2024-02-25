package fr.ensicaen.pi.gpss.backend.database.mapper.transaction;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class TransactionToBankAccountMapper
        implements DTOEntityMapper<TransactionToBankAccountDTO, TransactionToBankAccountEntity> {
    private final TransactionMapper transactionMapper;
    private final BankAccountMapper bankAccountMapper;

    public TransactionToBankAccountMapper(
            TransactionMapper transactionMapper, @Lazy BankAccountMapper bankAccountMapper
    ) {
        this.transactionMapper = transactionMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public TransactionToBankAccountDTO toDTO(TransactionToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return TransactionToBankAccountDTO.builder()
                .idTransactionToBankAccount(entity.getIdTransactionToBankAccount())
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .transaction(transactionMapper.toDTO(entity.getTransaction()))
                .build();
    }

    public TransactionToBankAccountDTO toDTOFromBankAccount(TransactionToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return TransactionToBankAccountDTO.builder()
                .idTransactionToBankAccount(entity.getIdTransactionToBankAccount())
                .transaction(transactionMapper.toDTO(entity.getTransaction()))
                .build();
    }

    @Override
    public TransactionToBankAccountEntity toEntity(TransactionToBankAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        TransactionToBankAccountEntity entity = new TransactionToBankAccountEntity(
                bankAccountMapper.toEntity(dto.getBankAccount()),
                transactionMapper.toEntity(dto.getTransaction())
        );
        entity.setIdTransactionToBankAccount(dto.getIdTransactionToBankAccount());
        return entity;
    }

    public TransactionToBankAccountDTO toDashboard(TransactionToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return TransactionToBankAccountDTO.builder()
                .idTransactionToBankAccount(entity.getIdTransactionToBankAccount())
                .transaction(transactionMapper.toDashboard(entity.getTransaction()))
                .build();
    }

    public TransactionToBankAccountDTO toNew(@NotNull BankAccountDTO bankAccount, @NotNull TransactionDTO transaction) {
        return TransactionToBankAccountDTO.builder()
                .bankAccount(bankAccount)
                .transaction(transaction)
                .build();
    }
}
