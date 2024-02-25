package fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.IndividualDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.IndividualEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class IndividualMapper implements DTOEntityMapper<IndividualDTO, IndividualEntity> {

    @Override
    public IndividualDTO toDTO(IndividualEntity entity) {
        if (entity == null) {
            return null;
        }
        return IndividualDTO.builder()
                .idIndividual(entity.getIdIndividual())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .build();
    }

    @Override
    public IndividualEntity toEntity(IndividualDTO dto) {
        if (dto == null) {
            return null;
        }
        IndividualEntity entity = new IndividualEntity(
                dto.getFirstName(),
                dto.getLastName()
        );
        entity.setIdIndividual(dto.getIdIndividual());
        return entity;
    }

    public IndividualDTO toDashboard(IndividualEntity entity) {
        if (entity == null) {
            return null;
        }
        return IndividualDTO.builder()
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .build();
    }
}
