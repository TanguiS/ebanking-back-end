package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.CardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodStatusDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManageCardService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.UpdateOrderStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;
import java.util.Objects;

@Configuration
@DependsOn({"requestCardInitializer", "clientBankAccountInitializer"})
public class CardInitializer {
    private final ManageCardService manageCardService;
    private final UpdateOrderStatusService orderStatusService;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final UserFormComponent userFormComponent;
    private final AccountManagerRepository accountManagerRepository;
    private final BankAccountMapper bankAccountMapper;
    private final boolean isAllowed;

    public CardInitializer(
            ManageCardService manageCardService,
            UpdateOrderStatusService orderStatusService,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            UserFormComponent userFormComponent,
            AccountManagerRepository accountManagerRepository,
            BankAccountMapper bankAccountMapper,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.manageCardService = manageCardService;
        this.orderStatusService = orderStatusService;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        this.userFormComponent = userFormComponent;
        this.accountManagerRepository = accountManagerRepository;
        this.bankAccountMapper = bankAccountMapper;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        BankAccountDTO bankAccount = getBankAccountDTO();
        assert bankAccount != null;
        List<RequestCardDTO> requestsCard = bankAccount.getPaymentMethodRequests()
                .stream()
                .map(RequestPaymentMethodManagerDTO::getRequestCard)
                .filter(Objects::nonNull)
                .toList();

        initiateVISAElectron(requestsCard);
        initiateVISAGold(requestsCard);
        initiateMASTERCARDPlus(requestsCard);
    }

    private static RequestCardDTO getRequestFromName(List<RequestCardDTO> requestsCard, String vsaElectron) {
        RequestCardDTO requestCard = requestsCard.stream()
                .filter(value -> Objects.equals(
                        value.getCardInformation().getCardProduct().getName(), vsaElectron
                ))
                .findAny()
                .orElse(null);
        assert requestCard != null;
        return requestCard;
    }

    private void initiateVISAGold(List<RequestCardDTO> requestsCard) {
        RequestCardDTO visaGold = getRequestFromName(requestsCard, "VISA GOLD");
        received(visaGold);
    }

    private void initiateVISAElectron(List<RequestCardDTO> requestsCard) {
        RequestCardDTO visaElectron = getRequestFromName(requestsCard, "VISA ELECTRON");
        block(visaElectron);
    }

    private void initiateMASTERCARDPlus(List<RequestCardDTO> requestsCard) {
        RequestCardDTO mastercardPlus = getRequestFromName(requestsCard, "MASTERCARD PLUS");
        ordered(mastercardPlus);
    }

    private RequestPaymentMethodStatusDTO ordered(RequestCardDTO mastercardPlus) {
        return orderStatusService.onRequestPaymentMethod(
                mastercardPlus.getRequestPaymentMethodStatus()
        );
    }

    private CardDTO received(RequestCardDTO visaElectron) {
        RequestPaymentMethodStatusDTO methodStatus = ordered(visaElectron);
        methodStatus = orderStatusService.onReceivedPaymentMethodByBank(methodStatus);
        orderStatusService.onReceivedPaymentMethodByUser(methodStatus);
        CardDTO card = manageCardService.addNewOnReceivedByBank(visaElectron);
        return manageCardService.updateOnUserReceived(card);
    }

    private void block(RequestCardDTO visaElectron) {
        CardDTO card = received(visaElectron);
        card = manageCardService.updateOnUserReceived(card);
        manageCardService.updateOnUserOrBankBlocked(card);
    }

    private BankAccountDTO getBankAccountDTO() {
        UserInformationDTO client = userInformationMapper.toDTO(
                userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                        userFormComponent.getBabylonClientForm().email()
                )
        );
        return accountManagerRepository
                .findAllBankAccountByUserInformation(userInformationMapper.toEntity(client))
                .stream()
                .map(bankAccountMapper::toDTO)
                .filter(value -> value.getBankAccountType() == BankAccountType.CURRENT_ACCOUNT)
                .findFirst()
                .orElse(null);
    }
}
