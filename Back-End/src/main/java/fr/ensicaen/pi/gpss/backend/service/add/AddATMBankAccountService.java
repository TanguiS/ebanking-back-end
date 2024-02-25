package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.database.dto.account.ATMAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.AccountManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserRoleDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Role;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.ATMAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.AccountManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserRoleMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.AccountManagerRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserInformationRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.user.UserRoleRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddATMBankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final ATMAccountMapper atmAccountMapper;
    private final AccountManagerMapper accountManagerMapper;
    private final AccountManagerRepository accountManagerRepository;
    private final UserInformationMapper userInformationMapper;
    private final UserInformationRepository userInformationRepository;
    private final UserRoleMapper userRoleMapper;
    private final UserRoleRepository userRoleRepository;

    public AddATMBankAccountService(
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            ATMAccountMapper atmAccountMapper,
            AccountManagerMapper accountManagerMapper,
            AccountManagerRepository accountManagerRepository,
            UserInformationMapper userInformationMapper,
            UserInformationRepository userInformationRepository,
            UserRoleMapper userRoleMapper,
            UserRoleRepository userRoleRepository
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.atmAccountMapper = atmAccountMapper;
        this.accountManagerMapper = accountManagerMapper;
        this.accountManagerRepository = accountManagerRepository;
        this.userInformationMapper = userInformationMapper;
        this.userInformationRepository = userInformationRepository;
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
    }

    public BankAccountDTO addNew(@Valid @PositiveOrZero int initialAmount) {
        UserInformationDTO atm = userInformationMapper.toNewATM(
                bankAccountRepository.countAllByBankAccountType(BankAccountType.ATM_ACCOUNT)
        );
        atm = userInformationMapper.toDTO(
                userInformationRepository.save(
                        userInformationMapper.toEntity(atm)
                )
        );
        UserRoleDTO role = userRoleMapper.toNew(atm, Role.ROLE_ATM);
        role = userRoleMapper.toDTO(
                userRoleRepository.save(
                        userRoleMapper.toEntity(role)
                )
        );
        atm = role.getUserInformation();


        BankAccountDTO newBankAccount = bankAccountMapper.toNew(
                new PreFilledBankAccount(initialAmount, BankAccountType.ATM_ACCOUNT)
        );
        ATMAccountDTO atmAccount = atmAccountMapper.toNew(newBankAccount);
        AccountManagerDTO manager = accountManagerMapper.toNewATMManager(atmAccount, atm);
        AccountManagerEntity saved = accountManagerRepository.save(accountManagerMapper.toEntity(manager));
        return bankAccountMapper.toDTO(saved.getAtmAccount().getBankAccount());
    }
}
