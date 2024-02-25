package fr.ensicaen.pi.gpss.backend.database.initialization;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardProductMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.CardProductRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManagerRequestCardService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn({"cardProductInitializer", "clientBankAccountInitializer"})
public class RequestCardInitializer {
    private final ManagerRequestCardService managerRequestCardService;
    private final BankAccountMapper bankAccountMapper;
    private final CardProductMapper cardProductMapper;
    private final CardProductRepository cardProductRepository;
    private final UserInformationMapper userInformationMapper;
    private final AccountManagerRepository accountManagerRepository;
    private final UserInformationRepository userInformationRepository;
    private final UserFormComponent userFormComponent;
    private final boolean isAllowed;

    public RequestCardInitializer(
            ManagerRequestCardService managerRequestCardService,
            BankAccountMapper bankAccountMapper,
            CardProductMapper cardProductMapper,
            CardProductRepository cardProductRepository,
            UserInformationMapper userInformationMapper,
            AccountManagerRepository accountManagerRepository,
            UserInformationRepository userInformationRepository,
            UserFormComponent userFormComponent,
            IsInitializationAllowed isInitializationAllowed
    ) {
        this.managerRequestCardService = managerRequestCardService;
        this.bankAccountMapper = bankAccountMapper;
        this.cardProductMapper = cardProductMapper;
        this.cardProductRepository = cardProductRepository;
        this.userInformationMapper = userInformationMapper;
        this.accountManagerRepository = accountManagerRepository;
        this.userInformationRepository = userInformationRepository;
        this.userFormComponent = userFormComponent;
        isAllowed = isInitializationAllowed.isAllowed();
    }

    @PostConstruct
    public void initiate() {
        if (!isAllowed) {
            return;
        }
        BankAccountDTO firstCurrentAccount = getBankAccountDTO();
        visaElectronRequest(firstCurrentAccount);
        visaGoldRequest(firstCurrentAccount);
        mastercardPlusRequest(firstCurrentAccount);
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

    private void mastercardPlusRequest(BankAccountDTO firstCurrentAccount) {
        CardProductDTO productCard = cardProductMapper.toDTO(
                cardProductRepository.findByNameIsIgnoreCase("MASTERCARD PLUS")
        );
        PreFilledCardInformation preFilledInformation = new PreFilledCardInformation(
                2, 9000000, 1000000
        );
        managerRequestCardService.addNewRequest(firstCurrentAccount, productCard, preFilledInformation);
    }

    private void visaElectronRequest(BankAccountDTO firstCurrentAccount) {
        CardProductDTO productCard = cardProductMapper.toDTO(
                cardProductRepository.findByNameIsIgnoreCase("VISA ELECTRON")
        );
        PreFilledCardInformation preFilledInformation = new PreFilledCardInformation(
                4, 300000, 50000
        );
        managerRequestCardService.addNewRequest(firstCurrentAccount, productCard, preFilledInformation);
    }

    private void visaGoldRequest(BankAccountDTO firstCurrentAccount) {
        CardProductDTO productCard = cardProductMapper.toDTO(
                cardProductRepository.findByNameIsIgnoreCase("VISA GOLD")
        );
        PreFilledCardInformation preFilledInformation = new PreFilledCardInformation(
                5, 1100000, 800000
        );
        managerRequestCardService.addNewRequest(firstCurrentAccount, productCard, preFilledInformation);
    }
}
