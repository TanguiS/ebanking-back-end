package fr.ensicaen.pi.gpss.backend.database.mapper.card_product;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.SoftwareCardDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.SoftwareCardEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class SoftwareCardMapper implements DTOEntityMapper<SoftwareCardDTO, SoftwareCardEntity> {
    private final CardProductMapper cardProductMapper;

    public SoftwareCardMapper(@Lazy CardProductMapper cardProductMapper) {
        this.cardProductMapper = cardProductMapper;
    }

    @Override
    public SoftwareCardDTO toDTO(SoftwareCardEntity entity) {
        if (entity == null) {
            return null;
        }
        return SoftwareCardDTO.builder()
                .idSoftwareCard(entity.getIdSoftwareCard())
                .cardScheme(entity.getCardScheme())
                .cardProduct(cardProductMapper.toDTO(entity.getCardProduct()))
                .priorityUseLevel(entity.getPriorityUseLevel())
                .build();
    }

    @Override
    public SoftwareCardEntity toEntity(SoftwareCardDTO dto) {
        if (dto == null) {
            return null;
        }
        SoftwareCardEntity entity = new SoftwareCardEntity(
                dto.getCardScheme(),
                dto.getPriorityUseLevel(),
                cardProductMapper.toEntity(dto.getCardProduct())
        );
        entity.setIdSoftwareCard(dto.getIdSoftwareCard());
        return entity;
    }

    public SoftwareCardDTO toDTOFromCardProduct(@NotNull SoftwareCardEntity entity) {
        return SoftwareCardDTO.builder()
                .idSoftwareCard(entity.getIdSoftwareCard())
                .cardScheme(entity.getCardScheme())
                .priorityUseLevel(entity.getPriorityUseLevel())
                .build();
    }

    public SoftwareCardDTO toNew(
            @NotNull SoftwareCardDTO softwareCard, @NotNull CardProductDTO cardProduct
    ) {
        return SoftwareCardDTO.builder()
                .cardScheme(softwareCard.getCardScheme())
                .priorityUseLevel(softwareCard.getPriorityUseLevel())
                .cardProduct(cardProduct)
                .build();
    }
}
