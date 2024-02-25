package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.beneficiary.BeneficiaryMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.beneficiary.BeneficiaryToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.beneficiary.BeneficiaryToBankAccountRepository;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBeneficiary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddBeneficiaryService {
    private final BeneficiaryMapper beneficiaryMapper;
    private final BeneficiaryToBankAccountMapper beneficiaryToBankAccountMapper;
    private final BeneficiaryToBankAccountRepository beneficiaryToBankAccountRepository;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;

    public AddBeneficiaryService(
            BeneficiaryMapper beneficiaryMapper,
            BeneficiaryToBankAccountMapper beneficiaryToBankAccountMapper,
            BeneficiaryToBankAccountRepository beneficiaryToBankAccountRepository,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
    ) {
        this.beneficiaryMapper = beneficiaryMapper;
        this.beneficiaryToBankAccountMapper = beneficiaryToBankAccountMapper;
        this.beneficiaryToBankAccountRepository = beneficiaryToBankAccountRepository;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }

    private boolean bankAccountAlreadyHasThisBeneficiary(BeneficiaryDTO beneficiary, BankAccountDTO bankAccount) {
        BankAccountIdentification iban = bankAccountIdentificationConfiguration.fromExistingIban(
                beneficiary.getIban()
        );
        if (bankAccount.getBeneficiaries() == null) {
            return false;
        }
        return bankAccount.getBeneficiaries().stream()
                .map(value -> bankAccountIdentificationConfiguration.fromExistingIban(
                        value.getBeneficiary().getIban())
                )
                .anyMatch(value -> value.equals(iban)
                );
    }

    private boolean bankAccountIsNotValid(BankAccountDTO bankAccount) {
        return bankAccount.getBankAccountType() != BankAccountType.CURRENT_ACCOUNT;
    }

    public BeneficiaryDTO addNew(
            @Valid @NotNull PreFilledBeneficiary preFilledBeneficiary, @Valid @NotNull BankAccountDTO bankAccount
    ) throws IllegalArgumentException {
        if (bankAccountIsNotValid(bankAccount)) {
            throw new IllegalArgumentException();
        }

        BeneficiaryDTO beneficiary = beneficiaryMapper.toNew(preFilledBeneficiary, bankAccount);
        if (bankAccountAlreadyHasThisBeneficiary(beneficiary, bankAccount)) {
            throw new IllegalArgumentException();
        }

        BeneficiaryToBankAccountDTO dto = beneficiaryToBankAccountMapper.toDTO(
                beneficiaryToBankAccountRepository.save(
                        beneficiaryToBankAccountMapper.toEntity(
                                beneficiaryToBankAccountMapper.toNew(beneficiary, bankAccount)
                        )
                )
        );

        return beneficiaryMapper.toDashboard(
                beneficiaryMapper.toEntity(
                        dto.getBeneficiary()
                )
        );
    }
}
