package fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.DirectDebitEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class DirectDebitMapper implements DTOEntityMapper<DirectDebitDTO, DirectDebitEntity> {
    private final LegalEntityMapper legalEntityMapper;
    private final IndividualMapper individualMapper;

    public DirectDebitMapper(LegalEntityMapper legalEntityMapper, IndividualMapper individualMapper) {
        this.legalEntityMapper = legalEntityMapper;
        this.individualMapper = individualMapper;
    }

    @Override
    public DirectDebitDTO toDTO(DirectDebitEntity entity) {
        if (entity == null) {
            return null;
        }
        return DirectDebitDTO.builder()
                .idDirectDebit(entity.getIdDirectDebit())
                .recurrence(entity.getRecurrence())
                .iban(entity.getIban())
                .legalEntity(legalEntityMapper.toDTO(entity.getLegalEntity()))
                .individual(individualMapper.toDTO(entity.getIndividual()))
                .build();
    }

    @Override
    public DirectDebitEntity toEntity(DirectDebitDTO dto) {
        if (dto == null) {
            return null;
        }
        DirectDebitEntity entity = new DirectDebitEntity(
                dto.getIban(),
                dto.getRecurrence(),
                individualMapper.toEntity(dto.getIndividual()),
                legalEntityMapper.toEntity(dto.getLegalEntity())
        );
        entity.setIdDirectDebit(dto.getIdDirectDebit());
        return entity;
    }

    public DirectDebitDTO toDashboard(DirectDebitEntity entity) {
        if (entity == null) {
            return null;
        }
        return DirectDebitDTO.builder()
                .idDirectDebit(entity.getIdDirectDebit())
                .recurrence(entity.getRecurrence())
                .legalEntity(legalEntityMapper.toDashboard(entity.getLegalEntity()))
                .individual(individualMapper.toDashboard(entity.getIndividual()))
                .build();
    }
}
