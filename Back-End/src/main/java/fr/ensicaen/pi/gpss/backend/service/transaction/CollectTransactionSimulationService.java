package fr.ensicaen.pi.gpss.backend.service.transaction;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.transaction.TransactionMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import fr.ensicaen.pi.gpss.backend.simulation.generate_transaction.CollectedTransactionTuple;
import fr.ensicaen.pi.gpss.backend.simulation.generate_transaction.RandomBankAccountIdentificationComponent;
import fr.ensicaen.pi.gpss.backend.simulation.generate_transaction.RandomPrimaryAccountNumberComponent;
import fr.ensicaen.pi.gpss.backend.simulation.generate_transaction.RandomTransactionValues;
import fr.ensicaen.pi.gpss.backend.tools.security.AesCypher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CollectTransactionSimulationService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionMapper transactionMapper;
    private final AesCypher panCypher;
    private final AesCypher ibanCypher;
    private final RandomTransactionValues transactionRandomizer;

    public CollectTransactionSimulationService(
            BankAccountRepository bankAccountRepository,
            TransactionMapper transactionMapper,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration,
            PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionMapper = transactionMapper;
        panCypher = primaryAccountNumberConfiguration.getPanCypher();
        ibanCypher = bankAccountIdentificationConfiguration.getIbanCypher();
        Random randomizer = new SecureRandom();
        RandomPrimaryAccountNumberComponent panRandomizer = new RandomPrimaryAccountNumberComponent(
                randomizer, primaryAccountNumberConfiguration, bankAccountIdentificationConfiguration
        );
        RandomBankAccountIdentificationComponent ibanRandomizer = new RandomBankAccountIdentificationComponent(
                randomizer, bankAccountIdentificationConfiguration
        );
        this.transactionRandomizer = new RandomTransactionValues(randomizer, panRandomizer, ibanRandomizer);
    }

    public List<TransactionDTO> generateTransactions(int numberOfTransactions) {
        List<String> availableNonEncryptedIban = ibanCypher.decryptAll(
                bankAccountRepository.findAllAvailableIban(), EncryptedDataPrefix.IBAN.prefix()
        );
        List<String> availableNonEncryptedPan = panCypher.decryptAll(
                bankAccountRepository.findAllAvailablePan(), EncryptedDataPrefix.PAN.prefix()
        );
        List<CollectedTransaction> transactions = new ArrayList<>();
        for (int i = 0; i < numberOfTransactions; i++) {
            CollectedTransactionTuple transaction = transactionRandomizer.shuffleCollectedTransaction(
                    availableNonEncryptedIban, availableNonEncryptedPan
            );
            transactions.add(transaction.credit());
            transactions.add(transaction.debit());
        }
        return transactions.stream().map(transactionMapper::toNewFromToBeCollectedTransaction).toList();
    }
}
