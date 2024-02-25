package fr.ensicaen.pi.gpss.backend.database.mapper.beneficiary;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class BeneficiaryToBankAccountMapper implements
        DTOEntityMapper<BeneficiaryToBankAccountDTO, BeneficiaryToBankAccountEntity> {
    private final BeneficiaryMapper beneficiaryMapper;
    private final BankAccountMapper bankAccountMapper;

    public BeneficiaryToBankAccountMapper(
            BeneficiaryMapper beneficiaryMapper, @Lazy BankAccountMapper bankAccountMapper
    ) {
        this.beneficiaryMapper = beneficiaryMapper;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BeneficiaryToBankAccountDTO toDTO(BeneficiaryToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return BeneficiaryToBankAccountDTO.builder()
                .idBeneficiaryToBankAccount(entity.getIdBeneficiaryToBankAccount())
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .beneficiary(beneficiaryMapper.toDTO(entity.getBeneficiary()))
                .build();
    }

    public BeneficiaryToBankAccountDTO toDTOFromBankAccount(BeneficiaryToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return BeneficiaryToBankAccountDTO.builder()
                .idBeneficiaryToBankAccount(entity.getIdBeneficiaryToBankAccount())
                .beneficiary(beneficiaryMapper.toDTO(entity.getBeneficiary()))
                .build();
    }

    @Override
    public BeneficiaryToBankAccountEntity toEntity(BeneficiaryToBankAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        BeneficiaryToBankAccountEntity entity = new BeneficiaryToBankAccountEntity(
                beneficiaryMapper.toEntity(dto.getBeneficiary()),
                bankAccountMapper.toEntity(dto.getBankAccount())
        );
        entity.setIdBeneficiaryToBankAccount(entity.getIdBeneficiaryToBankAccount());
        return entity;
    }

    public BeneficiaryToBankAccountDTO toNew(
            @NotNull BeneficiaryDTO newBeneficiary, @NotNull BankAccountDTO bankAccount
    ) {
        return BeneficiaryToBankAccountDTO.builder()
                .bankAccount(bankAccount)
                .beneficiary(newBeneficiary)
                .build();
    }

    public BeneficiaryToBankAccountDTO toDashboard(BeneficiaryToBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return BeneficiaryToBankAccountDTO.builder()
                .idBeneficiaryToBankAccount(entity.getIdBeneficiaryToBankAccount())
                .beneficiary(beneficiaryMapper.toDashboard(entity.getBeneficiary()))
                .build();
    }
}
