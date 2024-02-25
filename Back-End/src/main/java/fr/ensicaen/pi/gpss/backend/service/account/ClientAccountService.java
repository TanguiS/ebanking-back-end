package fr.ensicaen.pi.gpss.backend.service.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.service.api_access.UserCommonServices;
import fr.ensicaen.pi.gpss.backend.service.manage_payment_method.ManageRequestChequeBookService;
import fr.ensicaen.pi.gpss.backend.service.resolver_component.BankAccountResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class ClientAccountService extends UserCommonServices {
    private final BankAccountRepository bankAccountRepository;
    private final UserInformationMapper userInformationMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ManageRequestChequeBookService manageRequestChequeBookService;
    private final BankAccountResolver bankAccountResolver;

    protected ClientAccountService(
            UserInformationRepository userRepository,
            BankAccountRepository bankAccountRepository,
            UserInformationMapper userInformationMapper,
            BankAccountMapper bankAccountMapper,
            ManageRequestChequeBookService manageRequestChequeBookService,
            BankAccountResolver bankAccountResolver
    ) {
        super(userRepository, userInformationMapper);
        this.bankAccountRepository = bankAccountRepository;
        this.userInformationMapper = userInformationMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.manageRequestChequeBookService = manageRequestChequeBookService;
        this.bankAccountResolver = bankAccountResolver;
    }

    public List<BankAccountDTO> findAllBankAccountByClientUser(@NotNull UserInformationDTO user) {
        return bankAccountRepository.findAllByUserInformation(userInformationMapper.toEntity(user)).stream()
                .map(bankAccountMapper::toDashboard)
                .toList();
    }

    private BankAccountDTO getValidatedCurrentBankAccount(
            @Valid @NotNull @PositiveOrZero Integer idCurrentBankAccount
    ) throws IllegalArgumentException {
        BankAccountDTO bankAccount;
        if (bankAccountResolver.doesBankAccountDoNotExistById(idCurrentBankAccount)) {
            throw new IllegalArgumentException("Bank Account does not exist");
        }
        bankAccount = bankAccountResolver.resolveById(idCurrentBankAccount);
        if (manageRequestChequeBookService.doesBankAccountIsNotACurrentOne(bankAccount)) {
            throw new IllegalArgumentException("Bank Account is not a Current one");
        }
        if (manageRequestChequeBookService.doesBankAccountAlreadyHaveARequest(bankAccount)) {
            throw new IllegalArgumentException("Bank Account already has a pending request");
        }
        return bankAccount;
    }

    public RequestChequeBookDTO addRequestChequeBook(
            @Valid @NotNull @PositiveOrZero Integer idCurrentBankAccount
    ) throws IllegalArgumentException {
        BankAccountDTO bankAccount = getValidatedCurrentBankAccount(idCurrentBankAccount);
        return manageRequestChequeBookService.addRequest(bankAccount);
    }
}
