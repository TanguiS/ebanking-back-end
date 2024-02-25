package fr.ensicaen.pi.gpss.backend.database.mapper.beneficiary;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBeneficiary;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class BeneficiaryMapper implements DTOEntityMapper<BeneficiaryDTO, BeneficiaryEntity> {
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;

    public BeneficiaryMapper(BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration) {
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }

    @Override
    public BeneficiaryDTO toDTO(BeneficiaryEntity entity) {
        if (entity == null) {
            return null;
        }
        return BeneficiaryDTO.builder()
                .idBeneficiary(entity.getIdBeneficiary())
                .rib(entity.getRib())
                .iban(entity.getIban())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .idBankAccount(entity.getIdBankAccount())
                .build();
    }

    @Override
    public BeneficiaryEntity toEntity(BeneficiaryDTO dto) {
        if (dto == null) {
            return null;
        }
        BeneficiaryEntity entity = new BeneficiaryEntity(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getIban(),
                dto.getRib(),
                dto.getIdBankAccount()
        );
        entity.setIdBeneficiary(dto.getIdBeneficiary());
        return entity;
    }

    public BeneficiaryDTO toNew(@NotNull PreFilledBeneficiary preFilled, @NotNull BankAccountDTO bankAccount) {
        BankAccountIdentification iban = bankAccountIdentificationConfiguration.fromExistingIban(preFilled.iban());
        return BeneficiaryDTO.builder()
                .rib(iban.getEncryptedRib())
                .iban(iban.getEncryptedIban())
                .firstName(preFilled.firstName())
                .lastName(preFilled.lastName())
                .idBankAccount(bankAccount.getIdBankAccount())
                .build();
    }

    public BeneficiaryDTO toDashboard(BeneficiaryEntity entity) {
        if (entity == null) {
            return null;
        }
        return BeneficiaryDTO.builder()
                .idBeneficiary(entity.getIdBeneficiary())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }
}
