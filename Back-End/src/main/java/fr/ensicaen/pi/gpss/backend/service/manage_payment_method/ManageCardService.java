package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.CardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.PaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.CardMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.PaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestCardMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.payment_method.CardRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.payment_method.PaymentMethodManagerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageCardService {
    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final RequestCardMapper requestCardMapper;
    private final PaymentMethodManagerMapper paymentMethodManagerMapper;
    private final PaymentMethodManagerRepository paymentMethodManagerRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;

    public ManageCardService(
            CardMapper cardMapper,
            CardRepository cardRepository,
            RequestCardMapper requestCardMapper,
            PaymentMethodManagerMapper paymentMethodManagerMapper,
            PaymentMethodManagerRepository paymentMethodManagerRepository,
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository
    ) {
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
        this.requestCardMapper = requestCardMapper;
        this.paymentMethodManagerMapper = paymentMethodManagerMapper;
        this.paymentMethodManagerRepository = paymentMethodManagerRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
    }

    public CardDTO addNewOnReceivedByBank(
            @Valid @NotNull RequestCardDTO requestCardResponse
    ) {
        BankAccountDTO bankAccount = bankAccountMapper.toDTO(
                bankAccountRepository.findByRequestCard(
                        requestCardMapper.toEntity(requestCardResponse)
                )
        );
        CardDTO newCard = cardMapper.toNewFromRequestCardResponse(requestCardResponse);
        PaymentMethodManagerDTO newManager = paymentMethodManagerMapper.toNewCardManager(newCard, bankAccount);
        paymentMethodManagerRepository.save(paymentMethodManagerMapper.toEntity(newManager));
        return cardMapper.toDTO(
                bankAccountRepository.findLatestCardByBankAccount(
                        bankAccountMapper.toEntity(bankAccount)
                )
        );
    }

    public List<CardDTO> addNewsOnReceivedByBank(
            @Valid @NotEmpty List<RequestCardDTO> requestCardsResponse
    ) {
        return requestCardsResponse.stream().map(this::addNewOnReceivedByBank).toList();
    }

    public CardDTO updateOnUserReceived(@Valid @NotNull CardDTO card) {
        return cardMapper.toDTO(
                cardRepository.save(
                        cardMapper.toEntity(
                                cardMapper.toUpdateOnUserReceived(card)
                        )
                )
        );
    }

    public CardDTO updateOnUserOrBankBlocked(@Valid @NotNull CardDTO card) {
        return cardMapper.toDTO(
                cardRepository.save(
                        cardMapper.toEntity(
                                cardMapper.toUpdateOnUserOrBankBlocked(card)
                        )
                )
        );
    }
}
