package fr.ensicaen.pi.gpss.backend.database.mapper.payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.CardDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestCardDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.CardEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardInformationMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class CardMapper implements DTOEntityMapper<CardDTO, CardEntity> {
    private final CardInformationMapper cardInformationMapper;

    public CardMapper(CardInformationMapper cardInformationMapper) {
        this.cardInformationMapper = cardInformationMapper;
    }

    @Override
    public CardDTO toDTO(CardEntity entity) {
        if (entity == null) {
            return null;
        }
        return CardDTO.builder()
                .idCard(entity.getIdCard())
                .cardStatus(entity.getCardStatus())
                .cardInformation(cardInformationMapper.toDTO(entity.getCardInformation()))
                .build();
    }

    @Override
    public CardEntity toEntity(CardDTO dto) {
        if (dto == null) {
            return null;
        }
        CardEntity entity = new CardEntity(
                dto.getCardStatus(),
                cardInformationMapper.toEntity(dto.getCardInformation())
        );
        entity.setIdCard(dto.getIdCard());
        return entity;
    }

    public CardDTO toNewFromRequestCardResponse(@NotNull RequestCardDTO requestCard) {
        return CardDTO.builder()
                .cardStatus(CardStatus.DISABLE)
                .cardInformation(requestCard.getCardInformation())
                .build();
    }

    public CardDTO toUpdateOnUserReceived(@NotNull CardDTO card) {
        return CardDTO.builder()
                .idCard(card.getIdCard())
                .cardStatus(CardStatus.ACTIVATED)
                .cardInformation(card.getCardInformation())
                .build();
    }

    public CardDTO toUpdateOnUserOrBankBlocked(@NotNull CardDTO card) {
        return CardDTO.builder()
                .idCard(card.getIdCard())
                .cardStatus(CardStatus.BLOCKED)
                .cardInformation(card.getCardInformation())
                .build();
    }

    public CardDTO toDashboard(CardEntity entity) {
        if (entity == null) {
            return null;
        }
        return CardDTO.builder()
                .idCard(entity.getIdCard())
                .cardStatus(entity.getCardStatus())
                .cardInformation(cardInformationMapper.toDashBoard(entity.getCardInformation()))
                .build();
    }
}
