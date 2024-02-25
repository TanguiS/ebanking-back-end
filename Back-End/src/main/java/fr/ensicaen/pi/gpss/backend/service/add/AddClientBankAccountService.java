package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.database.dto.account.AccountManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.ClientAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.AccountManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.ClientAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddClientBankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final ClientAccountMapper clientAccountMapper;
    private final AccountManagerMapper accountManagerMapper;
    private final AccountManagerRepository accountManagerRepository;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;

    public AddClientBankAccountService(
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            ClientAccountMapper clientAccountMapper,
            AccountManagerMapper accountManagerMapper,
            AccountManagerRepository accountManagerRepository,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.clientAccountMapper = clientAccountMapper;
        this.accountManagerMapper = accountManagerMapper;
        this.accountManagerRepository = accountManagerRepository;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
    }

    private void validateUserInformation(
            @Valid @NotNull UserInformationDTO userInformation
    ) throws IllegalArgumentException {
        if (!userInformationRepository.existsById(userInformation.getIdUser())) {
            throw new IllegalArgumentException("User does not exists");
        }
        if (userInformation.getRole() == null) {
            throw new IllegalArgumentException("Banker is not an official & registered user");
        }
        if (userInformation.getRole().getRoleType() != Role.ROLE_CLIENT) {
            throw new IllegalArgumentException("User is not a client");
        }
    }

    private void validateBankAccount(
            @Valid @NotNull PreFilledBankAccount bankAccount, @Valid @NotNull UserInformationDTO userInformation
    ) throws IllegalArgumentException {
        if (bankAccountRepository.countAllByTypeAccountAndUserInformation(
                BankAccountType.SAVING_ACCOUNT, userInformationMapper.toEntity(userInformation)
        ) != 0) {
            throw new IllegalArgumentException("Can't have more than one Saving account");
        }
        if (bankAccount.bankAccountType() == BankAccountType.ATM_ACCOUNT) {
            throw new IllegalArgumentException("Can't have an ATM account");
        }
    }

    public BankAccountDTO addNew(
            @Valid @NotNull PreFilledBankAccount preFilledBankAccount, @Valid @NotNull UserInformationDTO userInformation
    ) {
        validateUserInformation(userInformation);
        validateBankAccount(preFilledBankAccount, userInformation);
        BankAccountDTO newBankAccount = bankAccountMapper.toNew(preFilledBankAccount);
        ClientAccountDTO clientAccount = clientAccountMapper.toNew(newBankAccount);
        AccountManagerDTO manager = accountManagerMapper.toNewClient(clientAccount, userInformation);
        AccountManagerEntity saved = accountManagerRepository.save(accountManagerMapper.toEntity(manager));
        return bankAccountMapper.toDashboard(saved.getClientAccount().getBankAccount());
    }
}
