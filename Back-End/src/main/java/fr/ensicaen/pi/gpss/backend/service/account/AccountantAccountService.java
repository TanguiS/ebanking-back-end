package fr.ensicaen.pi.gpss.backend.service.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.transaction.TransactionMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.transaction.TransactionRepository;
import fr.ensicaen.pi.gpss.backend.payload.response.accountant.AccountantDashboardDTO;
import fr.ensicaen.pi.gpss.backend.payload.response.accountant.TransactionTupleDTO;
import fr.ensicaen.pi.gpss.backend.service.transaction.FilterTransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class AccountantAccountService {
    private final FilterTransactionService filterTransactionService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public AccountantAccountService(
            FilterTransactionService filterTransactionService,
            TransactionMapper transactionMapper,
            TransactionRepository transactionRepository
    ) {
        this.filterTransactionService = filterTransactionService;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
    }

    public AccountantDashboardDTO getAccountantDashboard(
            @Valid @NotNull @Positive Integer numberOfItemPerPage,
            @Valid @NotNull @PositiveOrZero Integer pageNumber
    ) throws IllegalArgumentException {
        int ceil = (int) Math.ceil((transactionRepository.count()) / 2.d / numberOfItemPerPage);
        if (pageNumber >= ceil) {
            throw new IllegalArgumentException("Specified page number must not be higher than max capacity");
        }
        Map<TransactionDTO, TransactionDTO> allTransactions = findTransactionMapOnDebtor(
                numberOfItemPerPage, pageNumber
        );
        Set<TransactionDTO> ensiBankTransactions = findEnsiBankTransaction(allTransactions);
        List<TransactionTupleDTO> transactionTuples = formatToTransactionTuple(allTransactions, ensiBankTransactions);
        return new AccountantDashboardDTO(
                transactionTuples,
                ceil
        );
    }

    private List<TransactionTupleDTO> formatToTransactionTuple(
            Map<TransactionDTO, TransactionDTO> allTransactions, Set<TransactionDTO> ensiBankTransactions
    ) {
        return allTransactions.entrySet().stream()
                .map(entry -> {
                    boolean isEnsiBank = ensiBankTransactions.contains(entry.getKey());
                    TransactionDTO key = transactionMapper.toAccountantDashboard(
                            transactionMapper.toEntity(entry.getKey()), isEnsiBank
                    );
                    isEnsiBank = ensiBankTransactions.contains(entry.getValue());
                    TransactionDTO value = transactionMapper.toAccountantDashboard(
                            transactionMapper.toEntity(entry.getValue()), isEnsiBank
                    );
                    return Map.entry(key, value);
                })
                .map(entry -> new TransactionTupleDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Set<TransactionDTO> findEnsiBankTransaction(Map<TransactionDTO, TransactionDTO> allTransactions) {
        Set<TransactionDTO> input = new HashSet<>(allTransactions.keySet());
        input.addAll(new HashSet<>(allTransactions.values()));
        Map<String, TransactionDTO> mapTransactionsOnKey = filterTransactionService.convertTotMapTransactions(
                input
        );
        Set<TransactionDTO> ensiBankTransactions = new HashSet<>();
        for (BankAccountDTO bankAccount : filterTransactionService.getBankAccountDTOs()) {
            ensiBankTransactions.addAll(filterTransactionService.findAllByIbanBasedOnBankAccount(
                    mapTransactionsOnKey, bankAccount
            ));
            ensiBankTransactions.addAll(filterTransactionService.findAllByPanBasedOnBankAccount(
                    mapTransactionsOnKey, bankAccount
            ));
        }
        return ensiBankTransactions;
    }

    private Map<TransactionDTO, TransactionDTO> findTransactionMapOnDebtor(
            Integer numberOfItemPerPage, Integer pageNumber
    ) {
        Pageable pageable = PageRequest.of(pageNumber, numberOfItemPerPage);
        return transactionRepository
                .findAllTransactionDebtorWithTheirCreditor(pageable)
                .stream()
                .map(value -> new TransactionDTO[]{
                        transactionMapper.toDTO(value[0]), transactionMapper.toDTO(value[1])
                })
                .collect(Collectors.toMap(
                        value -> value[0],
                        value -> value[1]
                ));
    }
}
