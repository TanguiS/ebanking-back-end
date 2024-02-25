package fr.ensicaen.pi.gpss.backend.database.mapper.payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.ChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.ChequeBookEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ChequeBookMapper implements DTOEntityMapper<ChequeBookDTO, ChequeBookEntity> {
    @Override
    public ChequeBookDTO toDTO(ChequeBookEntity entity) {
        if (entity == null) {
            return null;
        }
        return ChequeBookDTO.builder()
                .idChequeBook(entity.getIdChequeBook())
                .payerName(entity.getPayerName())
                .payeeName(entity.getPayeeName())
                .build();
    }

    @Override
    public ChequeBookEntity toEntity(ChequeBookDTO dto) {
        if (dto == null) {
            return null;
        }
        ChequeBookEntity entity = new ChequeBookEntity(
                dto.getPayerName(),
                dto.getPayeeName()
        );
        entity.setIdChequeBook(dto.getIdChequeBook());
        return entity;
    }

    public ChequeBookDTO toNew() {
        return ChequeBookDTO.builder()
                .payerName("deprecated")
                .payeeName("deprecated")
                .build();
    }
}
