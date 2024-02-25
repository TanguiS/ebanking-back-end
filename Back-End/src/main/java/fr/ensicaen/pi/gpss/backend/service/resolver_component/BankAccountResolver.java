package fr.ensicaen.pi.gpss.backend.service.resolver_component;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Component
@Validated
public class BankAccountResolver {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final UserInformationMapper userInformationMapper;

    public BankAccountResolver(
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            UserInformationMapper userInformationMapper
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.userInformationMapper = userInformationMapper;
    }

    public boolean doesBankAccountDoNotExistById(@Valid @NotNull @PositiveOrZero Integer idBankAccount) {
        return !bankAccountRepository.existsById(idBankAccount);
    }

    public BankAccountDTO resolveById(
            @Valid @NotNull @PositiveOrZero Integer idBankAccount
    ) throws IllegalArgumentException {
        Optional<BankAccountEntity> byId = bankAccountRepository.findById(idBankAccount);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return bankAccountMapper.toDTO(byId.get());
    }

    public List<BankAccountDTO> resolveAllByUserInformation(@NotNull UserInformationDTO user) {
        List<BankAccountEntity> all = bankAccountRepository.findAllByUserInformation(
                userInformationMapper.toEntity(user)
        );
        return all.stream()
                .map(bankAccountMapper::toDTO)
                .toList();
    }

    public List<BankAccountDTO> resolveAllCurrentAccount(@NotNull UserInformationDTO user) {
        return resolveAllByUserInformation(user).stream()
                .filter(value -> value.getBankAccountType() == BankAccountType.CURRENT_ACCOUNT)
                .toList();
    }
}
