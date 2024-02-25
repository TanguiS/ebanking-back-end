package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodStatusDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManageChequeBookService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.UpdateOrderStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Objects;

@Configuration
@DependsOn({"requestChequeBookInitializer", "clientBankAccountInitializer"})
public class ChequeBookInitializer {
    private final ManageChequeBookService manageChequeBookService;
    private final UpdateOrderStatusService updateOrderStatusService;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final UserFormComponent userFormComponent;
    private final AccountManagerRepository accountManagerRepository;
    private final BankAccountMapper bankAccountMapper;
    private final boolean isAllowed;

    public ChequeBookInitializer(
            ManageChequeBookService manageChequeBookService,
            UpdateOrderStatusService updateOrderStatusService,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            UserFormComponent userFormComponent,
            AccountManagerRepository accountManagerRepository,
            BankAccountMapper bankAccountMapper,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.manageChequeBookService = manageChequeBookService;
        this.updateOrderStatusService = updateOrderStatusService;
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
        RequestChequeBookDTO requestChequeBook = bankAccount.getPaymentMethodRequests()
                .stream()
                .map(RequestPaymentMethodManagerDTO::getRequestChequeBook)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);

        assert requestChequeBook != null;
        RequestPaymentMethodStatusDTO methodStatus = updateOrderStatusService.onRequestPaymentMethod(
                requestChequeBook.getRequestPaymentMethodStatus()
        );
        methodStatus = updateOrderStatusService.onReceivedPaymentMethodByBank(methodStatus);
        updateOrderStatusService.onReceivedPaymentMethodByUser(methodStatus);
        manageChequeBookService.addNewOnBankReceived(requestChequeBook);
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
