package fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestCardEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardInformationMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class RequestCardMapper implements DTOEntityMapper<RequestCardDTO, RequestCardEntity> {
    private final CardInformationMapper cardInformationMapper;
    private final RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper;

    public RequestCardMapper(
            CardInformationMapper cardInformationMapper,
            RequestPaymentMethodStatusMapper requestPaymentMethodStatusMapper
    ) {
        this.cardInformationMapper = cardInformationMapper;
        this.requestPaymentMethodStatusMapper = requestPaymentMethodStatusMapper;
    }

    @Override
    public RequestCardDTO toDTO(RequestCardEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestCardDTO.builder()
                .idRequestCard(entity.getIdRequestCard())
                .cardInformation(cardInformationMapper.toDTO(entity.getCardInformation()))
                .requestPaymentMethodStatus(
                        requestPaymentMethodStatusMapper.toDTO(entity.getRequestPaymentMethodStatusEntity())
                )
                .build();
    }

    @Override
    public RequestCardEntity toEntity(RequestCardDTO dto) {
        if (dto == null) {
            return null;
        }
        RequestCardEntity entity = new RequestCardEntity(
                cardInformationMapper.toEntity(dto.getCardInformation()),
                requestPaymentMethodStatusMapper.toEntity(dto.getRequestPaymentMethodStatus())
        );
        entity.setIdRequestCard(dto.getIdRequestCard());
        return entity;
    }

    public RequestCardDTO toNew(@NotNull CardInformationDTO cardInformation) {
        return RequestCardDTO.builder()
                .cardInformation(cardInformation)
                .requestPaymentMethodStatus(requestPaymentMethodStatusMapper.toNew())
                .build();
    }

    public RequestCardDTO toDashboard(RequestCardEntity entity) {
        if (entity == null) {
            return null;
        }
        return RequestCardDTO.builder()
                .idRequestCard(entity.getIdRequestCard())
                .requestPaymentMethodStatus(
                        requestPaymentMethodStatusMapper.toDTO(entity.getRequestPaymentMethodStatusEntity())
                )
                .build();
    }
}
