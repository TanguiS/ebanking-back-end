package fr.ensicaen.pi.gpss.backend.service.transaction;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.mapper.transaction.TransactionMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.transaction.TransactionRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CollectTransactionService {
    private final AssignTransactionToBankAccountService assignTransactionToBankAccountService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;
    private final PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration;

    public CollectTransactionService(
            AssignTransactionToBankAccountService assignTransactionToBankAccountService,
            TransactionMapper transactionMapper,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration,
            PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration,
            TransactionRepository transactionRepository
    ) {
        this.assignTransactionToBankAccountService = assignTransactionToBankAccountService;
        this.transactionMapper = transactionMapper;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
        this.primaryAccountNumberConfiguration = primaryAccountNumberConfiguration;
        this.transactionRepository = transactionRepository;
    }

    private boolean doesOriginalCollectedTransactionsHaveAnOppositeOne(
            CollectedTransaction original, Collection<CollectedTransaction> transactions
    ) {
        boolean hasOppositeTransaction = false;
        for (CollectedTransaction transaction : transactions) {
            if (original.isAccountingOpposite(transaction)) {
                hasOppositeTransaction = true;
                break;
            }
        }
        return hasOppositeTransaction;
    }

    private boolean areAccountingDirectionsCorrect(Collection<CollectedTransaction> transactions) {
        if (transactions.size() % 2 != 0) {
            return false;
        }
        int countDebit = 0;
        for (CollectedTransaction transaction : transactions) {
            if (transaction.accountingDirection() != AccountingDirection.DEBIT) {
                continue;
            }
            if (!doesOriginalCollectedTransactionsHaveAnOppositeOne(transaction, transactions)) {
                return false;
            }
            countDebit++;
        }
        return transactions.size() - countDebit == transactions.size() / 2;
    }

    private boolean areTransactionsDateConsistent(Collection<CollectedTransaction> transactions) {
        final Timestamp collectTime = new Timestamp(new Date().getTime());
        for (CollectedTransaction transaction : transactions) {
            if (collectTime.before(Timestamp.valueOf(transaction.transactionDate()))) {
                return false;
            }
        }
        return true;
    }

    private boolean areTransactionsProcessable(Collection<CollectedTransaction> transactions) {
        for (CollectedTransaction transaction : transactions) {
            try {
                bankAccountIdentificationConfiguration.fromExistingIban(transaction.transactionActor());
                continue;
            } catch (IllegalArgumentException ignored) {
                //IBAN not validated, try with PAN
            }
            try {
                primaryAccountNumberConfiguration.fromExistingPan(transaction.transactionActor());
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }
        return true;
    }

    private String encryptTransactionActor(String nonEncryptedTransactionActor) throws IllegalArgumentException {
        if (EncryptedDataPrefix.isEncryptedIban(nonEncryptedTransactionActor)) {
            throw new IllegalArgumentException("The given actor is an encrypted IBAN");
        }
        if (EncryptedDataPrefix.isEncryptedPan(nonEncryptedTransactionActor)) {
            throw new IllegalArgumentException("The given actor is an encrypted PAN");
        }
        String encryptedActor;
        try {
            encryptedActor = bankAccountIdentificationConfiguration
                    .fromExistingIban(nonEncryptedTransactionActor)
                    .getEncryptedIban();
        } catch (Exception ignored) {
            try {
                encryptedActor = primaryAccountNumberConfiguration
                        .fromExistingPan(nonEncryptedTransactionActor)
                        .getEncryptedPan();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        return encryptedActor;
    }

    private void validateCollectedTransactionBatch(
            Collection<CollectedTransaction> transactions
    ) throws IllegalArgumentException {
        if (!areTransactionsDateConsistent(transactions)) {
            throw new IllegalArgumentException(
                    "This batch of transaction has an incorrect timestamp, " +
                    "at least one of them is after the server time");
        }
        if (!areAccountingDirectionsCorrect(transactions)) {
            throw new IllegalArgumentException(
                    "This batch of transaction has an incorrect number of accounting direction, " +
                    "it should be 50% credit, 50% debit " +
                    "and at least one of the transactions does not have an opposite one"
            );
        }
        if (!areTransactionsProcessable(transactions)) {
            throw new IllegalArgumentException(
                    "At least one of the transmitted transactions has an incorrect debtor/creditor identifier, " +
                    "either IBAN or PAN"
            );
        }
    }

    private List<TransactionEntity> formatCollectedTransactionToPersistentEntities(
            Collection<CollectedTransaction> transactions
    ) {
        return transactions.stream()
                .map(transactionToBeCollected -> {
                    String encryptedActor;
                    try {
                        encryptedActor = encryptTransactionActor(transactionToBeCollected.transactionActor());
                    } catch (IllegalArgumentException ignored) {
                        encryptedActor = transactionToBeCollected.transactionActor();
                    } catch (RuntimeException ignored) {
                        throw new IllegalArgumentException();
                    }
                    return transactionMapper.toNewFromCollectedTransactionWithEncryptedActor(
                            transactionToBeCollected, encryptedActor
                    );
                })
                .filter(Objects::nonNull)
                .map(transactionMapper::toEntity)
                .toList();
    }

    public boolean collectTransactions(
            @Valid @NotEmpty Collection<CollectedTransaction> transactions
    ) throws IllegalArgumentException {
        validateCollectedTransactionBatch(transactions);
        try {
            List<TransactionEntity> toBePersisted = formatCollectedTransactionToPersistentEntities(transactions);
            List<TransactionDTO> persisted = transactionRepository.saveAll(toBePersisted).stream()
                    .map(transactionMapper::toDTO)
                    .toList();
            if (!assignTransactionToBankAccountService.assign(persisted)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
