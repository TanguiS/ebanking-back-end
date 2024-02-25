package fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.LegalEntityDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.LegalEntityEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class LegalEntityMapper implements DTOEntityMapper<LegalEntityDTO, LegalEntityEntity> {

    @Override
    public LegalEntityDTO toDTO(LegalEntityEntity entity) {
        if (entity == null) {
            return null;
        }
        return LegalEntityDTO.builder()
                .idLegalEntity(entity.getIdLegalEntity())
                .name(entity.getName())
                .numberSiret(entity.getNumberSiret())
                .build();
    }

    @Override
    public LegalEntityEntity toEntity(LegalEntityDTO dto) {
        if (dto == null) {
            return null;
        }
        LegalEntityEntity entity = new LegalEntityEntity(
                dto.getName(),
                dto.getNumberSiret()
        );
        entity.setIdLegalEntity(dto.getIdLegalEntity());
        return entity;
    }

    public LegalEntityDTO toDashboard(LegalEntityEntity entity) {
        if (entity == null) {
            return null;
        }
        return LegalEntityDTO.builder()
                .name(entity.getName())
                .build();
    }
}
