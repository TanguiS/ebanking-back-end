package fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestChequeBookEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class RequestChequeBookMapper implements DTOEntityMapper<RequestChequeBookDTO, RequestChequeBookEntity> {
    private final RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper;

    public RequestChequeBookMapper(RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper) {
        this.requestPaymentMethodStatusMapper = requestPaymentMethodStatusMapper;
    }

    @Override
    public RequestChequeBookDTO toDTO(RequestChequeBookEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestChequeBookDTO.builder()
                .idRequestChequeBook(entity.getIdRequestChequeBook())
                .requestPaymentMethodStatus(
                        requestPaymentMethodStatusMapper.toDTO(entity.getRequestPaymentMethodStatusEntity())
                )
                .build();
    }

    @Override
    public RequestChequeBookEntity toEntity(RequestChequeBookDTO dto) {
        if (dto == null) {
            return null;
        }
        RequestChequeBookEntity entity = new RequestChequeBookEntity(
                requestPaymentMethodStatusMapper.toEntity(dto.getRequestPaymentMethodStatus())
        );
        entity.setIdRequestChequeBook(dto.getIdRequestChequeBook());
        return entity;
    }

    public RequestChequeBookDTO toNew() {
        return RequestChequeBookDTO.builder()
                .requestPaymentMethodStatus(requestPaymentMethodStatusMapper.toNew())
                .build();
    }
}
