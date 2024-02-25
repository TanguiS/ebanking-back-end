package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import fr.ensicaen.pi.gpss.backend.service.add.AddClientBankAccountService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientBankAccountInitializer {
    private final UserFormComponent userFormComponent;
    private final AddClientBankAccountService addClientBankAccountService;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final boolean isAllowed;

    public ClientBankAccountInitializer(
            UserFormComponent userFormComponent,
            AddClientBankAccountService addClientBankAccountService,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.userFormComponent = userFormComponent;
        this.addClientBankAccountService = addClientBankAccountService;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        initiateBabylon();
        initiateBabylonJunior();
    }

    private void initiateBabylonJunior() {
        UserInformationDTO client = userInformationMapper.toDTO(
                userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                        userFormComponent.getBabylonJuniorClientForm().email()
                )
        );
        youthPassbookAccount(client);
    }

    private void initiateBabylon() {
        UserInformationDTO client = userInformationMapper.toDTO(
                userInformationRepository.findByEmailIsLikeIgnoreCaseWithAllProperties(
                        userFormComponent.getBabylonClientForm().email()
                )
        );
        currentAccount(client);
        savingAccount(client);
    }

    private void createAccount(int amount, BankAccountType type, UserInformationDTO client) {
        PreFilledBankAccount preFilledBankAccount = new PreFilledBankAccount(amount, type);
        addClientBankAccountService.addNew(preFilledBankAccount, client);
    }

    private void savingAccount(UserInformationDTO client) {
        createAccount(877268, BankAccountType.SAVING_ACCOUNT, client);
    }

    private void currentAccount(UserInformationDTO client) {
        createAccount(100000, BankAccountType.CURRENT_ACCOUNT, client);
    }

    private void youthPassbookAccount(UserInformationDTO client) {
        createAccount(50000, BankAccountType.YOUTH_PASSBOOK, client);
    }
}
