package fr.ensicaen.pi.gpss.backend.service.transaction;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumber;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.CardMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class FilterTransactionService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final CardMapper cardMapper;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;
    private final PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration;

    public FilterTransactionService(
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            CardMapper cardMapper,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration,
            PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.cardMapper = cardMapper;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
        this.primaryAccountNumberConfiguration = primaryAccountNumberConfiguration;
    }

    public List<TransactionDTO> findAllByIbanBasedOnBankAccount(
            @Valid @NotEmpty Map<String, TransactionDTO> mapTransactions,
            @Valid @NotNull BankAccountDTO bankAccount
    ) {
        BankAccountIdentification iban = bankAccountIdentificationConfiguration
                .fromExistingIban(bankAccount.getIban());
        return mapTransactions.keySet().stream()
                .filter(EncryptedDataPrefix::isEncryptedIban)
                .filter(value -> bankAccountIdentificationConfiguration.fromExistingIban(value).equals(iban))
                .map(mapTransactions::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<TransactionDTO> findAllByPanBasedOnBankAccount(
            @Valid @NotEmpty Map<String, TransactionDTO> mapTransactions,
            @Valid @NotNull BankAccountDTO bankAccount
    ) {
        if (bankAccountRepository.countAvailableCardByBankAccount(bankAccountMapper.toEntity(bankAccount)) == 0) {
            return new ArrayList<>();
        }
        List<PrimaryAccountNumber> cards = bankAccountRepository.findAllAvailableCardByBankAccount(
                        bankAccountMapper.toEntity(bankAccount)).stream()
                .map(cardMapper::toDTO)
                .map(value -> primaryAccountNumberConfiguration.fromExistingPan(value.getCardInformation().getPan()))
                .collect(Collectors.toCollection(ArrayList::new));
        if (cards.isEmpty()) {
            return new ArrayList<>();
        }
        return mapTransactions.keySet().stream()
                .filter(EncryptedDataPrefix::isEncryptedPan)
                .filter(value -> cards.contains(primaryAccountNumberConfiguration.fromExistingPan(value)))
                .map(mapTransactions::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Map<String, TransactionDTO> convertTotMapTransactions(
            @Valid @NotEmpty Collection<TransactionDTO> transactions
    ) {
        return transactions.stream()
                .collect(Collectors.toMap(
                        transaction -> transaction.getIban() != null ? transaction.getIban() : transaction.getPan(),
                        transaction -> transaction
                ));
    }

    public List<BankAccountDTO> getBankAccountDTOs() {
        return bankAccountRepository.findAll()
                .stream()
                .map(bankAccountMapper::toDTO)
                .toList();
    }
}
