package fr.ensicaen.pi.gpss.backend.database.mapper.card_product;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardProductEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class CardProductMapper implements DTOEntityMapper<CardProductDTO, CardProductEntity> {
    private final SoftwareCardMapper softwareCardMapper;
    private final AuthorizationPolicyMapper authorizationPolicyMapper;

    public CardProductMapper(
            SoftwareCardMapper softwareCardMapper, AuthorizationPolicyMapper authorizationPolicyMapper
    ) {
        this.softwareCardMapper = softwareCardMapper;
        this.authorizationPolicyMapper = authorizationPolicyMapper;
    }

    @Override
    public CardProductDTO toDTO(CardProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return CardProductDTO.builder()
                .idCardProduct(entity.getIdCardProduct())
                .cardType(entity.getCardType())
                .name(entity.getName())
                .numberOfContactlessTransactionBeforeAskingPin(
                        entity.getNumberOfContactlessTransactionBeforeAskingPin()
                )
                .contactlessUpperLimitPerTransaction(entity.getContactlessUpperLimitPerTransaction())
                .softwareCards(entity.getSoftwareCards().stream()
                        .map(softwareCardMapper::toDTOFromCardProduct)
                        .toList()
                )
                .authorizationPolicies(entity.getAuthorizationPolicies().stream()
                        .map(authorizationPolicyMapper::toDTOFromCardProduct)
                        .toList()
                )
                .build();
    }

    @Override
    public CardProductEntity toEntity(CardProductDTO dto) {
        if (dto == null) {
            return null;
        }
        CardProductEntity entity = new CardProductEntity(
                dto.getName(),
                dto.getCardType(),
                dto.getContactlessUpperLimitPerTransaction(),
                dto.getNumberOfContactlessTransactionBeforeAskingPin(),
                dto.getSoftwareCards().stream()
                        .map(softwareCardMapper::toEntity)
                        .toList(),
                dto.getAuthorizationPolicies().stream()
                        .map(authorizationPolicyMapper::toEntity)
                        .toList()
        );
        entity.setIdCardProduct(dto.getIdCardProduct());
        return entity;
    }
}
