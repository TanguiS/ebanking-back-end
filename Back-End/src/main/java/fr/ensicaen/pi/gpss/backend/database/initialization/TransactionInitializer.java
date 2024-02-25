package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import fr.ensicaen.pi.gpss.backend.service.transaction.CollectTransactionService;
import fr.ensicaen.pi.gpss.backend.service.transaction.CollectTransactionSimulationService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TransactionInitializer {
    private final CollectTransactionService collectTransactionService;
    private final CollectTransactionSimulationService collectTransactionSimulationService;
    private final boolean isAllowed;

    public TransactionInitializer(
            CollectTransactionService collectTransactionService,
            CollectTransactionSimulationService collectTransactionSimulationService,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.collectTransactionService = collectTransactionService;
        this.collectTransactionSimulationService = collectTransactionSimulationService;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        List<TransactionDTO> dtos = collectTransactionSimulationService.generateTransactions(25);
        List<CollectedTransaction> transactions = dtos.stream()
                .map(transaction -> new CollectedTransaction(
                        transaction.getId(),
                        transaction.getAccountingDirection(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getTransactionActor(),
                        transaction.getTransactionDate().toString()
                ))
                .toList();
        collectTransactionService.collectTransactions(transactions);
    }
}
