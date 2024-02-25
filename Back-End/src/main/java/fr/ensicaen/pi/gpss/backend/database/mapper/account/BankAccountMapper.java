package fr.ensicaen.pi.gpss.backend.database.mapper.account;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.PaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.DirectDebitToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.PaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.beneficiary.BeneficiaryToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit.DirectDebitToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.PaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestPaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.transaction.TransactionToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledBankAccount;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.function.Function;

@Component
@Validated
public class BankAccountMapper implements DTOEntityMapper<BankAccountDTO, BankAccountEntity> {
    private final RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper;
    private final PaymentMethodManagerMapper paymentMethodManagerMapper;
    private final BeneficiaryToBankAccountMapper beneficiaryToBankAccountMapper;
    private final TransactionToBankAccountMapper transactionToBankAccountMapper;
    private final DirectDebitToBankAccountMapper directDebitToBankAccountMapper;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;

    public BankAccountMapper(
            RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper,
            PaymentMethodManagerMapper paymentMethodManagerMapper,
            BeneficiaryToBankAccountMapper beneficiaryToBankAccountMapper,
            TransactionToBankAccountMapper transactionToBankAccountMapper,
            DirectDebitToBankAccountMapper directDebitToBankAccountMapper,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
    ) {
        this.requestPaymentMethodManagerMapper = requestPaymentMethodManagerMapper;
        this.paymentMethodManagerMapper = paymentMethodManagerMapper;
        this.beneficiaryToBankAccountMapper = beneficiaryToBankAccountMapper;
        this.transactionToBankAccountMapper = transactionToBankAccountMapper;
        this.directDebitToBankAccountMapper = directDebitToBankAccountMapper;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }


    @Override
    public BankAccountDTO toDTO(BankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        BankAccountDTO.BankAccountDTOBuilder builder = BankAccountDTO.builder()
                .idBankAccount(entity.getIdBankAccount())
                .bankAccountType(entity.getBankAccountType())
                .rib(entity.getRib())
                .iban(entity.getIban())
                .amount(entity.getAmount());

        setInformation(
                entity,
                builder,
                beneficiaryToBankAccountMapper::toDTOFromBankAccount,
                paymentMethodManagerMapper::toDTOFromBankAccount,
                requestPaymentMethodManagerMapper::toDTOFromBankAccount,
                transactionToBankAccountMapper::toDTOFromBankAccount,
                directDebitToBankAccountMapper::toDTOFromBankAccount
        );

        return builder.build();
    }

    @Override
    public BankAccountEntity toEntity(BankAccountDTO dto) {
        if (dto == null) {
            return null;
        }

        BankAccountEntity entity = new BankAccountEntity(
                dto.getIban(),
                dto.getRib(),
                dto.getAmount(),
                dto.getBankAccountType(),
                null, null,null, null, null
        );
        entity.setIdBankAccount(dto.getIdBankAccount());

        if (dto.getTransactions() != null) {
            entity.setTransactions(
                    dto.getTransactions().stream()
                            .map(transactionToBankAccountMapper::toEntity)
                            .toList()
            );
        }
        if (dto.getPaymentMethodRequests() != null) {
            entity.setPaymentMethodRequests(
                    dto.getPaymentMethodRequests().stream()
                            .map(requestPaymentMethodManagerMapper::toEntity)
                            .toList()
            );
        }
        if (dto.getPaymentMethods() != null) {
            entity.setPaymentMethods(
                    dto.getPaymentMethods().stream()
                            .map(paymentMethodManagerMapper::toEntity)
                            .toList()
            );
        }
        if (dto.getBeneficiaries() != null) {
            entity.setBeneficiaries(
                    dto.getBeneficiaries().stream()
                            .map(beneficiaryToBankAccountMapper::toEntity)
                            .toList()
            );
        }
        if (dto.getDirectDebits() != null) {
            entity.setDirectDebits(
                    dto.getDirectDebits().stream()
                            .map(directDebitToBankAccountMapper::toEntity)
                            .toList()
            );
        }

        return entity;
    }

    public BankAccountDTO toNew(@NotNull PreFilledBankAccount preFilledBankAccount) {
        BankAccountIdentification iban = bankAccountIdentificationConfiguration.newEnsiBankIbanGenerator();
        return BankAccountDTO.builder()
                .amount(preFilledBankAccount.amount())
                .bankAccountType(preFilledBankAccount.bankAccountType())
                .iban(iban.getEncryptedIban())
                .rib(iban.getEncryptedRib())
                .build();
    }

    public BankAccountDTO toDashboard(@NotNull BankAccountEntity entity) {
        BankAccountIdentification iban = bankAccountIdentificationConfiguration.fromExistingIban(entity.getIban());
        BankAccountDTO.BankAccountDTOBuilder builder = BankAccountDTO.builder()
                .idBankAccount(entity.getIdBankAccount())
                .bankAccountType(entity.getBankAccountType())
                .rib(iban.getRIB())
                .iban(iban.getIban())
                .amount(entity.getAmount());

        setInformation(
                entity,
                builder,
                beneficiaryToBankAccountMapper::toDashboard,
                paymentMethodManagerMapper::toDashboard,
                requestPaymentMethodManagerMapper::toDashboard,
                transactionToBankAccountMapper::toDashboard,
                directDebitToBankAccountMapper::toDashboard
        );

        return builder.build();
    }

    private void setInformation(
            BankAccountEntity entity,
            BankAccountDTO.BankAccountDTOBuilder builder,
            Function<BeneficiaryToBankAccountEntity, BeneficiaryToBankAccountDTO> beneficiary,
            Function<PaymentMethodManagerEntity, PaymentMethodManagerDTO> paymentManager,
            Function<RequestPaymentMethodManagerEntity, RequestPaymentMethodManagerDTO> requestManager,
            Function<TransactionToBankAccountEntity, TransactionToBankAccountDTO> transaction,
            Function<DirectDebitToBankAccountEntity, DirectDebitToBankAccountDTO> directDebit
    ) {
        if (entity.getBeneficiaries() != null && !entity.getBeneficiaries().isEmpty()) {
            builder.beneficiaries(
                    entity.getBeneficiaries().stream()
                            .map(beneficiary)
                            .toList()
            );
        }
        if (entity.getPaymentMethods() != null && !entity.getPaymentMethods().isEmpty()) {
            builder.paymentMethods(
                    entity.getPaymentMethods().stream()
                            .map(paymentManager)
                            .filter(Objects::nonNull)
                            .toList()
            );
        }
        if (entity.getPaymentMethodRequests() != null && !entity.getPaymentMethodRequests().isEmpty()) {
            builder.paymentMethodRequests(
                    entity.getPaymentMethodRequests().stream()
                            .map(requestManager)
                            .toList()
            );
        }
        if (entity.getTransactions() != null && !entity.getTransactions().isEmpty()) {
            builder.transactions(
                    entity.getTransactions().stream()
                            .map(transaction)
                            .toList()
            );
        }
        if (entity.getDirectDebits() != null && !entity.getDirectDebits().isEmpty()) {
            builder.directDebits(
                    entity.getDirectDebits().stream()
                            .map(directDebit)
                            .toList()
            );
        }
    }
}
