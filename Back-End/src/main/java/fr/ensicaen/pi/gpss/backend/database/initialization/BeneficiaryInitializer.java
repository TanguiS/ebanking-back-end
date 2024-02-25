package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBeneficiary;
import fr.ensicaen.pi.gpss.backend.service.add.AddBeneficiaryService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"userFormComponent", "clientBankAccountInitializer"})
public class BeneficiaryInitializer {
    private final AddBeneficiaryService addBeneficiaryService;
    private final UserFormComponent userFormComponent;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;
    private final BankAccountMapper bankAccountMapper;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final AccountManagerRepository accountManagerRepository;
    private final boolean isAllowed;

    public BeneficiaryInitializer(
            AddBeneficiaryService addBeneficiaryService,
            UserFormComponent userFormComponent,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration,
            BankAccountMapper bankAccountMapper,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            AccountManagerRepository accountManagerRepository,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.addBeneficiaryService = addBeneficiaryService;
        this.userFormComponent = userFormComponent;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
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

        BankAccountIdentification iban = bankAccountIdentificationConfiguration.newEnsiBankIbanGenerator();
        PreFilledBeneficiary input = PreFilledBeneficiary.builder()
                .firstName("Joan")
                .lastName("Reynaud")
                .iban(iban.getIban())
                .build();
        assert bankAccount != null;
        addBeneficiaryService.addNew(input,bankAccount);

        iban = bankAccountIdentificationConfiguration.newEnsiBankIbanGenerator();
        input = PreFilledBeneficiary.builder()
                .firstName("Ndiaga")
                .lastName("Faye")
                .iban(iban.getIban())
                .build();
        addBeneficiaryService.addNew(input,bankAccount);
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
