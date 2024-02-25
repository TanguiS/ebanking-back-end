package fr.ensicaen.pi.gpss.backend.database.mapper.payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.CardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.ChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.PaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.PaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class PaymentMethodManagerMapper
        implements DTOEntityMapper<PaymentMethodManagerDTO, PaymentMethodManagerEntity> {
    private final BankAccountMapper bankAccountMapper;
    private final CardMapper cardMapper;
    private final ChequeBookMapper chequeBookMapper;

    public PaymentMethodManagerMapper(
            @Lazy BankAccountMapper bankAccountMapper, CardMapper cardMapper, ChequeBookMapper chequeBookMapper
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.cardMapper = cardMapper;
        this.chequeBookMapper = chequeBookMapper;
    }

    @Override
    public PaymentMethodManagerDTO toDTO(PaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return PaymentMethodManagerDTO.builder()
                .idPaymentMethodManager(entity.getIdPaymentMethodManager())
                .card(cardMapper.toDTO(entity.getCard()))
                .chequeBook(chequeBookMapper.toDTO(entity.getChequeBook()))
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public PaymentMethodManagerDTO toDTOFromBankAccount(PaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return PaymentMethodManagerDTO.builder()
                .idPaymentMethodManager(entity.getIdPaymentMethodManager())
                .card(cardMapper.toDTO(entity.getCard()))
                .chequeBook(chequeBookMapper.toDTO(entity.getChequeBook()))
                .build();
    }

    @Override
    public PaymentMethodManagerEntity toEntity(PaymentMethodManagerDTO dto) {
        if (dto == null) {
            return null;
        }
        PaymentMethodManagerEntity entity = new PaymentMethodManagerEntity(
                bankAccountMapper.toEntity(dto.getBankAccount()),
                chequeBookMapper.toEntity(dto.getChequeBook()),
                cardMapper.toEntity(dto.getCard())
        );
        entity.setIdPaymentMethodManager(dto.getIdPaymentMethodManager());
        return entity;
    }

    public PaymentMethodManagerDTO toNewCardManager(@NotNull CardDTO card, @NotNull BankAccountDTO bankAccount) {
        return PaymentMethodManagerDTO.builder()
                .card(card)
                .bankAccount(bankAccount)
                .build();
    }

    public PaymentMethodManagerDTO toNewChequeBookManager(
            @NotNull ChequeBookDTO chequeBook, @NotNull BankAccountDTO bankAccount
    ) {
        return PaymentMethodManagerDTO.builder()
                .chequeBook(chequeBook)
                .bankAccount(bankAccount)
                .build();
    }

    public PaymentMethodManagerDTO toDashboard(PaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getChequeBook() != null) {
            return null;
        }
        return PaymentMethodManagerDTO.builder()
                .idPaymentMethodManager(entity.getIdPaymentMethodManager())
                .card(cardMapper.toDashboard(entity.getCard()))
                .build();
    }
}
