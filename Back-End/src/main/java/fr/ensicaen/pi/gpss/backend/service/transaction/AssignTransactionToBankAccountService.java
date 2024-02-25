package fr.ensicaen.pi.gpss.backend.service.transaction;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.transaction.TransactionToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.transaction.TransactionToBankAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
public class AssignTransactionToBankAccountService {
    private final FilterTransactionService filterTransactionService;
    private final TransactionToBankAccountMapper transactionToBankAccountMapper;
    private final TransactionToBankAccountRepository transactionToBankAccountRepository;

    public AssignTransactionToBankAccountService(
            TransactionToBankAccountMapper transactionToBankAccountMapper,
            TransactionToBankAccountRepository transactionToBankAccountRepository,
            FilterTransactionService filterTransactionService
    ) {
        this.filterTransactionService = filterTransactionService;
        this.transactionToBankAccountMapper = transactionToBankAccountMapper;
        this.transactionToBankAccountRepository = transactionToBankAccountRepository;
    }

    private boolean isBatchNotValid(Collection<TransactionDTO> transactions) {
        return transactions.stream()
                .map(TransactionDTO::getIdTransaction)
                .allMatch(Objects::isNull);
    }

    private List<TransactionToBankAccountDTO> findAssignableTransactions(
            Collection<TransactionDTO> persistedTransaction
    ) {
        List<BankAccountDTO> allBankAccount = filterTransactionService.getBankAccountDTOs();
        Map<String, TransactionDTO> mapTransactions = filterTransactionService.convertTotMapTransactions(
                persistedTransaction
        );
        List<TransactionToBankAccountDTO> tobePersisted = new ArrayList<>();
        for (BankAccountDTO bankAccount : allBankAccount) {
            List<TransactionDTO> linked = filterTransactionService.findAllByIbanBasedOnBankAccount(
                    mapTransactions, bankAccount
            );
            linked.addAll(filterTransactionService.findAllByPanBasedOnBankAccount(mapTransactions, bankAccount));
            tobePersisted.addAll(
                    linked.stream()
                            .map(value -> transactionToBankAccountMapper.toNew(bankAccount, value))
                            .toList()
            );
        }
        return tobePersisted;
    }

    public boolean assign(@Valid @NotEmpty Collection<TransactionDTO> persistedTransaction) {
        if (isBatchNotValid(persistedTransaction)) {
            return false;
        }
        List<TransactionToBankAccountDTO> tobePersisted = findAssignableTransactions(persistedTransaction);
        try {
            transactionToBankAccountRepository.saveAll(
                    tobePersisted.stream()
                            .map(transactionToBankAccountMapper::toEntity)
                            .toList()
            );
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }
}
