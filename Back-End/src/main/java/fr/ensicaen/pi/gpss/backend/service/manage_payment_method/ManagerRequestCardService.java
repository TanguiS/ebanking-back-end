package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestCardMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestPaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method.RequestPaymentMethodManagerRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ManagerRequestCardService {
    private final RequestCardMapper requestCardMapper;
    private final CardInformationMapper cardInformationMapper;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper;
    private final RequestPaymentMethodManagerRepository requestPaymentMethodManagerRepository;

    public ManagerRequestCardService(
            RequestCardMapper requestCardMapper,
            CardInformationMapper cardInformationMapper,
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper,
            RequestPaymentMethodManagerRepository requestPaymentMethodManagerRepository
    ) {
        this.requestCardMapper = requestCardMapper;
        this.cardInformationMapper = cardInformationMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.requestPaymentMethodManagerMapper = requestPaymentMethodManagerMapper;
        this.requestPaymentMethodManagerRepository = requestPaymentMethodManagerRepository;
    }

    public boolean doesBankAccountAlreadyHaveARequest(@Valid @NotNull BankAccountDTO bankAccount) {
        return bankAccountRepository.countNotOrderedRequestCardByBankAccount(
                bankAccountMapper.toEntity(bankAccount)
        ) != 0;
    }

    public boolean doesBankAccountIsNotACurrentOne(@Valid @NotNull BankAccountDTO bankAccount) {
        return bankAccount.getBankAccountType() != BankAccountType.CURRENT_ACCOUNT;
    }

    public RequestCardDTO addNewRequest(
            @Valid @NotNull BankAccountDTO bankAccount,
            @Valid @NotNull CardProductDTO cardProduct,
            @Valid @NotNull PreFilledCardInformation preFilledCardInformation
    ) {
        CardInformationDTO newCard = cardInformationMapper.toNew(cardProduct, bankAccount, preFilledCardInformation);
        RequestPaymentMethodManagerDTO newCardManager = requestPaymentMethodManagerMapper.toNewCardRequestManager(
                newCard, bankAccount
        );

        requestPaymentMethodManagerRepository.save(
                requestPaymentMethodManagerMapper.toEntity(newCardManager)
        );

        return requestCardMapper.toDashboard(
                bankAccountRepository.findLatestUserCardRequestByBankAccount(
                        bankAccountMapper.toEntity(bankAccount)
                )
        );
    }
}
