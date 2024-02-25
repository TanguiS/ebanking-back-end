package fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodStatusDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodStatusEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Validated
public class RequestPaymentMethodStatusMapper
        implements DTOEntityMapper<RequestPaymentMethodStatusDTO, RequestPaymentMethodStatusEntity> {
    @Override
    public RequestPaymentMethodStatusDTO toDTO(RequestPaymentMethodStatusEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestPaymentMethodStatusDTO.builder()
                .idRequestPaymentMethodStatus(entity.getIdRequestPaymentMethodStatus())
                .userReceivedPaymentMethodDate(entity.getUserReceivedPaymentMethodDate())
                .bankRequestPaymentMethodDate(entity.getBankRequestPaymentMethodDate())
                .bankReceivedPaymentMethodDate(entity.getBankReceivedPaymentMethodDate())
                .orderStatus(entity.getOrderStatus())
                .userRequestPaymentMethodDate(entity.getUserRequestPaymentMethodDate())
                .build();
    }

    @Override
    public RequestPaymentMethodStatusEntity toEntity(RequestPaymentMethodStatusDTO dto) {
        if (dto == null) {
            return null;
        }
        RequestPaymentMethodStatusEntity entity = new RequestPaymentMethodStatusEntity(
                dto.getUserRequestPaymentMethodDate(),
                dto.getBankRequestPaymentMethodDate(),
                dto.getBankReceivedPaymentMethodDate(),
                dto.getUserReceivedPaymentMethodDate(),
                dto.getOrderStatus()
        );
        entity.setIdRequestPaymentMethodStatus(dto.getIdRequestPaymentMethodStatus());
        return entity;
    }

    public RequestPaymentMethodStatusDTO toNew() {
        return RequestPaymentMethodStatusDTO.builder()
                .orderStatus(OrderStatus.NOT_ORDERED)
                .userRequestPaymentMethodDate(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))
                .build();
    }

    public RequestPaymentMethodStatusDTO toUpdateOnRequest(
            @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return RequestPaymentMethodStatusDTO.builder()
                .idRequestPaymentMethodStatus(requestPaymentMethodStatus.getIdRequestPaymentMethodStatus())
                .bankRequestPaymentMethodDate(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))
                .orderStatus(OrderStatus.ORDERED)
                .userRequestPaymentMethodDate(requestPaymentMethodStatus.getUserRequestPaymentMethodDate())
                .build();
    }

    public RequestPaymentMethodStatusDTO toUpdateOnReceivedByBank(
            @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return RequestPaymentMethodStatusDTO.builder()
                .idRequestPaymentMethodStatus(requestPaymentMethodStatus.getIdRequestPaymentMethodStatus())
                .bankRequestPaymentMethodDate(requestPaymentMethodStatus.getBankRequestPaymentMethodDate())
                .bankReceivedPaymentMethodDate(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))
                .orderStatus(OrderStatus.RECEIVED)
                .userRequestPaymentMethodDate(requestPaymentMethodStatus.getUserRequestPaymentMethodDate())
                .build();
    }

    public RequestPaymentMethodStatusDTO toUpdateOnReceivedByUser(
            @NotNull RequestPaymentMethodStatusDTO requestPaymentMethodStatus
    ) {
        return RequestPaymentMethodStatusDTO.builder()
                .idRequestPaymentMethodStatus(requestPaymentMethodStatus.getIdRequestPaymentMethodStatus())
                .userReceivedPaymentMethodDate(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))
                .bankRequestPaymentMethodDate(requestPaymentMethodStatus.getBankRequestPaymentMethodDate())
                .bankReceivedPaymentMethodDate(requestPaymentMethodStatus.getBankReceivedPaymentMethodDate())
                .orderStatus(requestPaymentMethodStatus.getOrderStatus())
                .userRequestPaymentMethodDate(requestPaymentMethodStatus.getUserRequestPaymentMethodDate())
                .build();
    }
}
