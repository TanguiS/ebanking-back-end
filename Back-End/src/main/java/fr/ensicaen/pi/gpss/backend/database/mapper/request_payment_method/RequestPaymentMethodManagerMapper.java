package fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class RequestPaymentMethodManagerMapper
        implements DTOEntityMapper<RequestPaymentMethodManagerDTO, RequestPaymentMethodManagerEntity> {
    private final BankAccountMapper bankAccountMapper;
    private final RequestCardMapper requestCardMapper;
    private final RequestChequeBookMapper requestChequeBookMapper;

    public RequestPaymentMethodManagerMapper(
            @Lazy BankAccountMapper bankAccountMapper,
            RequestCardMapper requestCardMapper,
            RequestChequeBookMapper requestChequeBookMapper
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.requestCardMapper = requestCardMapper;
        this.requestChequeBookMapper = requestChequeBookMapper;
    }

    @Override
    public RequestPaymentMethodManagerDTO toDTO(RequestPaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestPaymentMethodManagerDTO.builder()
                .idRequestPaymentMethodManager(entity.getIdRequestPaymentMethodManager())
                .requestChequeBook(requestChequeBookMapper.toDTO(entity.getRequestChequeBook()))
                .requestCard(requestCardMapper.toDTO(entity.getRequestCard()))
                .bankAccount(bankAccountMapper.toDTO(entity.getBankAccount()))
                .build();
    }

    public RequestPaymentMethodManagerDTO toDTOFromBankAccount(RequestPaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestPaymentMethodManagerDTO.builder()
                .idRequestPaymentMethodManager(entity.getIdRequestPaymentMethodManager())
                .requestChequeBook(requestChequeBookMapper.toDTO(entity.getRequestChequeBook()))
                .requestCard(requestCardMapper.toDTO(entity.getRequestCard()))
                .build();
    }

    @Override
    public RequestPaymentMethodManagerEntity toEntity(RequestPaymentMethodManagerDTO dto) {
        if (dto == null) {
            return null;
        }
        RequestPaymentMethodManagerEntity entity = new RequestPaymentMethodManagerEntity(
                bankAccountMapper.toEntity(dto.getBankAccount()),
                requestCardMapper.toEntity(dto.getRequestCard()),
                requestChequeBookMapper.toEntity(dto.getRequestChequeBook())
        );
        entity.setIdRequestPaymentMethodManager(dto.getIdRequestPaymentMethodManager());
        return entity;
    }

    public RequestPaymentMethodManagerDTO toNewCardRequestManager(
            @NotNull CardInformationDTO newRequest, @NotNull BankAccountDTO bankAccount
    ) {
        return RequestPaymentMethodManagerDTO.builder()
                .requestCard(requestCardMapper.toNew(newRequest))
                .bankAccount(bankAccount)
                .build();
    }

    public RequestPaymentMethodManagerDTO toNewChequeBookRequestManager(
            @NotNull RequestChequeBookDTO newRequest, @NotNull BankAccountDTO bankAccount
    ) {
        return RequestPaymentMethodManagerDTO.builder()
                .requestChequeBook(newRequest)
                .bankAccount(bankAccount)
                .build();
    }

    public RequestPaymentMethodManagerDTO toDashboard(RequestPaymentMethodManagerEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestPaymentMethodManagerDTO.builder()
                .idRequestPaymentMethodManager(entity.getIdRequestPaymentMethodManager())
                .requestChequeBook(requestChequeBookMapper.toDTO(entity.getRequestChequeBook()))
                .requestCard(requestCardMapper.toDashboard(entity.getRequestCard()))
                .build();
    }
}
