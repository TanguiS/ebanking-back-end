package fr.ensicaen.pi.gpss.backend.database.mapper.transaction;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.IbanOrPan;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionStatus;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.expection_handler.services.exception.IllegalServicesArgumentException;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import fr.ensicaen.pi.gpss.backend.payload.request.RequestID;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;

@Component
@Validated
public class TransactionMapper implements DTOEntityMapper<TransactionDTO, TransactionEntity> {
    @Override
    public TransactionDTO toDTO(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return TransactionDTO.builder()
                .idTransaction(entity.getIdTransaction())
                .pan(entity.getPan())
                .iban(entity.getIban())
                .amount(entity.getAmount())
                .clearedDate(entity.getClearedDate())
                .transactionDate(entity.getTransactionDate())
                .transactionType(entity.getTransactionType())
                .transactionStatus(entity.getTransactionStatus())
                .currency(entity.getCurrency())
                .accountingDirection(entity.getAccountingDirection())
                .build();
    }

    @Override
    public TransactionEntity toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity(
                dto.getAmount(),
                dto.getCurrency(),
                dto.getTransactionDate(),
                dto.getTransactionType(),
                dto.getAccountingDirection(),
                dto.getClearedDate(),
                dto.getPan(),
                dto.getIban(),
                dto.getTransactionStatus()
        );
        entity.setIdTransaction(dto.getIdTransaction());
        return entity;
    }

    public TransactionDTO toNewFromCollectedTransactionWithEncryptedActor(
            @NotNull CollectedTransaction collectedTransaction, @IbanOrPan String transactionActor
    ) throws IllegalServicesArgumentException {
        TransactionDTO.TransactionDTOBuilder builder = TransactionDTO.builder()
                .transactionDate(Timestamp.valueOf(collectedTransaction.transactionDate()))
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(collectedTransaction.transactionType())
                .amount(collectedTransaction.amount())
                .accountingDirection(collectedTransaction.accountingDirection())
                .currency(collectedTransaction.currency());
        if (EncryptedDataPrefix.isEncryptedIban(transactionActor)) {
            builder.iban(transactionActor);
        } else if (EncryptedDataPrefix.isEncryptedPan(transactionActor)) {
            builder.pan(transactionActor);
        } else {
            throw new IllegalServicesArgumentException(
                    RequestID.COLLECTED_TRANSACTION ,
                    "The given transaction actor is not an IBAN or PAN"
            );
        }
        return builder.build();
    }

    public TransactionDTO toNewFromToBeCollectedTransaction(@NotNull CollectedTransaction toBeCollectedTransaction) {
        return TransactionDTO.builder()
                .transactionDate(Timestamp.valueOf(toBeCollectedTransaction.transactionDate()))
                .currency(toBeCollectedTransaction.currency())
                .transactionType(toBeCollectedTransaction.transactionType())
                .amount(toBeCollectedTransaction.amount())
                .accountingDirection(toBeCollectedTransaction.accountingDirection())
                .transactionActor(toBeCollectedTransaction.transactionActor())
                .id(toBeCollectedTransaction.id())
                .build();
    }

    public TransactionDTO toDashboard(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        return TransactionDTO.builder()
                .idTransaction(entity.getIdTransaction())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .transactionType(entity.getTransactionType())
                .currency(entity.getCurrency())
                .accountingDirection(entity.getAccountingDirection())
                .transactionStatus(entity.getTransactionStatus())
                .build();
    }

    public TransactionDTO toAccountantDashboard(TransactionEntity entity, boolean isEnsiBank) {
        if (entity == null) {
            return null;
        }
        return TransactionDTO.builder()
                .idTransaction(entity.getIdTransaction())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .transactionType(entity.getTransactionType())
                .currency(entity.getCurrency())
                .transactionStatus(entity.getTransactionStatus())
                .isEnsiBank(isEnsiBank)
                .build();
    }
}
