package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.CardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.CardEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardProductMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.CardMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestCardMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.payment_method.CardRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.BankAccountResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
public class CardBlockerService {
    private final ManageCardService manageCardService;
    private final ManagerRequestCardService managerRequestCardService;
    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final RequestCardMapper requestCardMapper;
    private final CardInformationMapper cardInformationMapper;
    private final CardProductMapper cardProductMapper;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountResolver bankAccountResolver;

    public CardBlockerService(
            ManageCardService manageCardService,
            ManagerRequestCardService managerRequestCardService,
            CardMapper cardMapper,
            CardRepository cardRepository,
            RequestCardMapper requestCardMapper,
            CardInformationMapper cardInformationMapper,
            CardProductMapper cardProductMapper,
            BankAccountRepository bankAccountRepository,
            BankAccountMapper bankAccountMapper,
            BankAccountResolver bankAccountResolver
    ) {
        this.manageCardService = manageCardService;
        this.managerRequestCardService = managerRequestCardService;
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
        this.requestCardMapper = requestCardMapper;
        this.cardInformationMapper = cardInformationMapper;
        this.cardProductMapper = cardProductMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountResolver = bankAccountResolver;
    }

    private boolean isCardOneOfTheClient(@NotNull CardDTO card, @NotNull UserInformationDTO user) {
        List<BankAccountDTO> currentBankAccounts = bankAccountResolver.resolveAllCurrentAccount(user);
        if (currentBankAccounts.isEmpty()) {
            return false;
        }
        return currentBankAccounts.stream()
                .map(value -> bankAccountRepository.findAllAvailableCardByBankAccount(
                        bankAccountMapper.toEntity(value)
                ))
                .anyMatch(value -> value.stream().anyMatch(
                        toMatch -> Objects.equals(toMatch.getIdCard(), card.getIdCard())
                ));
    }

    public boolean blockCard(@Valid @NotNull @PositiveOrZero Integer idCard, @NotNull UserInformationDTO user) {
        if (!cardRepository.existsByIdCardAndCardStatus(idCard, CardStatus.ACTIVATED)) {
            return true;
        }
        Optional<CardEntity> byId = cardRepository.findById(idCard);
        if (byId.isEmpty()) {
            return true;
        }
        CardDTO cardToBeBlock = cardMapper.toDTO(byId.get());
        if (cardToBeBlock.getCardStatus() == CardStatus.BLOCKED) {
            return true;
        }
        if (!isCardOneOfTheClient(cardToBeBlock, user)) {
            return true;
        }
        try {
            manageCardService.updateOnUserOrBankBlocked(cardToBeBlock);
        } catch (Exception ignored) {
            return true;
        }
        return false;
    }

    public RequestCardDTO requestCardOnCardBlocked(
            @Valid @NotNull @PositiveOrZero Integer idBlockedCard
    ) throws IllegalArgumentException {
        Optional<CardEntity> byId = cardRepository.findById(idBlockedCard);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException();
        }

        BankAccountDTO bankAccount = bankAccountMapper.toDTO(cardRepository.findBankAccountByCard(byId.get()));
        CardProductDTO cardProduct = cardProductMapper.toDTO(byId.get().getCardInformation().getCardProduct());
        final CardInformationDTO oldCardInformation = cardInformationMapper.toDTO(byId.get().getCardInformation());
        int expirationDateInYear = cardRepository.getExpirationDateInYear(byId.get());

        PreFilledCardInformation newCardInformationPreFilled = new PreFilledCardInformation(
                expirationDateInYear,
                oldCardInformation.getUpperLimitPerMonth(),
                oldCardInformation.getUpperLimitPerTransaction()
        );

        return requestCardMapper.toDashboard(
                requestCardMapper.toEntity(
                        managerRequestCardService.addNewRequest(bankAccount, cardProduct, newCardInformationPreFilled)
                )
        );
    }
}
