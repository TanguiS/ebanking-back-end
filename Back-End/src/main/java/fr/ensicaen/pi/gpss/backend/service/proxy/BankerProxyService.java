package fr.ensicaen.pi.gpss.backend.service.proxy;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import fr.ensicaen.pi.gpss.backend.payload.response.banker.BankerDashboardDTO;
import fr.ensicaen.pi.gpss.backend.service.account.BankerAccountService;
import fr.ensicaen.pi.gpss.backend.service.add.AddClientBankAccountService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.CardBlockerService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManageCardProductService;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManagerRequestCardService;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.BankAccountResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Validated
public class BankerProxyService {
    private final CardBlockerService cardBlockerService;
    private final BankerAccountService bankerAccountService;
    private final AddClientBankAccountService addClientBankAccountService;
    private final ManageCardProductService manageCardProductService;
    private final ManagerRequestCardService managerRequestCardService;
    private final UserInformationRepository userInformationRepository;
    private final UserInformationMapper userInformationMapper;
    private final BankAccountResolver bankAccountResolver;

    public BankerProxyService(
            CardBlockerService cardBlockerService,
            BankerAccountService bankerAccountService,
            AddClientBankAccountService addClientBankAccountService,
            ManageCardProductService manageCardProductService,
            ManagerRequestCardService managerRequestCardService,
            UserInformationRepository userInformationRepository,
            UserInformationMapper userInformationMapper,
            BankAccountResolver bankAccountResolver
    ) {
        this.cardBlockerService = cardBlockerService;
        this.bankerAccountService = bankerAccountService;
        this.addClientBankAccountService = addClientBankAccountService;
        this.manageCardProductService = manageCardProductService;
        this.managerRequestCardService = managerRequestCardService;
        this.userInformationRepository = userInformationRepository;
        this.userInformationMapper = userInformationMapper;
        this.bankAccountResolver = bankAccountResolver;
    }

    private void validateBankersClient(Integer idClient, UserInformationDTO banker) {
        String errMessage;
        if (bankerAccountService.isIdClientNotOneOfTheBankersClient(banker, idClient)) {
            errMessage = "The specified id client is not one of the banker's client";
            throw new IllegalArgumentException(errMessage);
        }
    }

    public BankerDashboardDTO getBankerDashboard(@Valid @NotNull UserInformationDTO banker) {
        try {
            return bankerAccountService.findAllBankersClient(banker);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not get the dashboard with the given banker");
        }
    }

    public RequestCardDTO blockClientsCard(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCard,
            @Valid @NotNull UserInformationDTO banker
    ) throws IllegalArgumentException {
        String errMessage;
        validateBankersClient(idClient, banker);
        if (cardBlockerService.blockCard(
                idCard,
                userInformationMapper.toDTO(userInformationRepository.findById(idClient).get())
        )) {
            errMessage = "The card does not exists or already blocked or could not update";
            throw new IllegalArgumentException(errMessage);
        }
        try {
            return cardBlockerService.requestCardOnCardBlocked(idCard);
        } catch (Exception e) {
            errMessage = "Could not create a new card request - but card is blocked !";
            throw new IllegalArgumentException(errMessage);
        }
    }

    public BankAccountDTO addBankAccountToClient(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @RequestBody PreFilledBankAccount preFilledBankAccount,
            @Valid @NotNull UserInformationDTO banker
    ) throws IllegalArgumentException {
        String errMessage;
        validateBankersClient(idClient, banker);
        try {
            return  addClientBankAccountService.addNew(
                    preFilledBankAccount,
                    bankerAccountService.resolveClient(banker, idClient)
            );
        } catch (Exception e) {
            errMessage = "Could not create a new bank account";
            throw new IllegalArgumentException(errMessage);
        }
    }

    private BankAccountDTO getValidatedCurrentBankAccount(
            Integer idCurrentBankAccount
    ) throws IllegalArgumentException {
        String errMessage;
        BankAccountDTO currentBankAccount;
        if (bankAccountResolver.doesBankAccountDoNotExistById(idCurrentBankAccount)) {
            errMessage = "The specified id current bank account does not exist";
            throw new IllegalArgumentException(errMessage);
        }
        currentBankAccount = bankAccountResolver.resolveById(idCurrentBankAccount);
        if (managerRequestCardService.doesBankAccountAlreadyHaveARequest(currentBankAccount)) {
            errMessage = "The specified bank account already has a pending request";
            throw new IllegalArgumentException(errMessage);
        }
        if (managerRequestCardService.doesBankAccountIsNotACurrentOne(currentBankAccount)) {
            errMessage = "The specified bank account is not a current bank account";
            throw new IllegalArgumentException(errMessage);
        }
        return currentBankAccount;
    }

    public RequestCardDTO addRequestCardToClient(
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idClient,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idProductCard,
            @Valid @NotNull @PositiveOrZero @PathVariable Integer idCurrentBankAccount,
            @Valid @NotNull @RequestBody PreFilledCardInformation preFilledCardInformation,
            @Valid @NotNull UserInformationDTO banker
    ) {
        String errMessage;
        BankAccountDTO currentBankAccount;
        validateBankersClient(idClient, banker);
        if (manageCardProductService.doesIdProductCardDoNotExist(idProductCard)) {
            errMessage = "The specified id product card does not exist";
            throw new IllegalArgumentException(errMessage);
        }
        currentBankAccount = getValidatedCurrentBankAccount(idCurrentBankAccount);
        try {
            return  managerRequestCardService.addNewRequest(
                    currentBankAccount, manageCardProductService.resolveById(idProductCard), preFilledCardInformation
            );
        } catch (Exception e) {
            errMessage = "Bad body or specified bank account is not a current one";
            throw new IllegalArgumentException(errMessage);
        }
    }
}
