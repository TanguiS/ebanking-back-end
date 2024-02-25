package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManageRequestChequeBookService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("clientBankAccountInitializer")
public class RequestChequeBookInitializer {
    private final ManageRequestChequeBookService manageRequestChequeBookService;
    private final UserFormComponent userFormComponent;
    private final BankAccountMapper bankAccountMapper;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final AccountManagerRepository accountManagerRepository;
    private final boolean isAllowed;

    public RequestChequeBookInitializer(
            ManageRequestChequeBookService manageRequestChequeBookService,
            UserFormComponent userFormComponent,
            BankAccountMapper bankAccountMapper,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            AccountManagerRepository accountManagerRepository,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.manageRequestChequeBookService = manageRequestChequeBookService;
        this.userFormComponent = userFormComponent;
        this.bankAccountMapper = bankAccountMapper;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        this.accountManagerRepository = accountManagerRepository;
        isAllowed = isInitializationAllowed.isAllowed();
    }


    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        BankAccountDTO bankAccount = getBankAccountDTO();
        assert bankAccount != null;
        manageRequestChequeBookService.addRequest(bankAccount);
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
