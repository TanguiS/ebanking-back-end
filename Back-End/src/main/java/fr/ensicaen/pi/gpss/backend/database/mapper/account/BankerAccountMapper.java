package fr.ensicaen.pi.gpss.backend.database.mapper.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankerAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.account.BankerAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class BankerAccountMapper implements DTOEntityMapper<BankerAccountDTO, BankerAccountEntity> {
    private final UserInformationMapper userInformationMapper;

    public BankerAccountMapper(@Lazy UserInformationMapper userInformationMapper) {
        this.userInformationMapper = userInformationMapper;
    }

    @Override
    public BankerAccountDTO toDTO(BankerAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return BankerAccountDTO.builder()
                .idBankerAccount(entity.getIdBankerAccount())
                .userInformation(userInformationMapper.toDTOFromBankerAccount(entity.getUserInformation()))
                .build();
    }

    @Override
    public BankerAccountEntity toEntity(BankerAccountDTO dto) {
        if (dto == null) {
            return null;
        }
        BankerAccountEntity entity = new BankerAccountEntity(userInformationMapper.toEntity(dto.getUserInformation()));
        entity.setIdBankerAccount(dto.getIdBankerAccount());
        return entity;
    }

    public BankerAccountDTO toNewBankerClient(@NotNull UserInformationDTO client) {
        return BankerAccountDTO.builder()
                .userInformation(client)
                .build();
    }
}
