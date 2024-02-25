package fr.ensicaen.pi.gpss.backend.database.mapper.card_product;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.AuthorizationPolicyDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.AuthorizationPolicyRequirementDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.AuthorizationPolicyEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class AuthorizationPolicyMapper implements DTOEntityMapper<AuthorizationPolicyDTO, AuthorizationPolicyEntity> {
    private final CardProductMapper cardProductMapper;

    public AuthorizationPolicyMapper(@Lazy CardProductMapper cardProductMapper) {
        this.cardProductMapper = cardProductMapper;
    }

    @Override
    public AuthorizationPolicyDTO toDTO(AuthorizationPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        AuthorizationPolicyRequirementDTO requirementDTO = getAuthorizationPolicyRequirementDTO(entity);
        return AuthorizationPolicyDTO.builder()
                .idAuthorizationPolicy(entity.getIdAuthorizationPolicy())
                .authorizationType(entity.getAuthorizationType())
                .cardProduct(cardProductMapper.toDTO(entity.getCardProduct()))
                .priorityUseLevel(entity.getPriorityUseLevel())
                .requirement(requirementDTO)
                .build();
    }

    private static AuthorizationPolicyRequirementDTO getAuthorizationPolicyRequirementDTO(AuthorizationPolicyEntity entity) {
        AuthorizationPolicyRequirementDTO requirementDTO = null;
        if (entity.getCumulativeTotalTransactionAmountUpperLimit() != null ||
                entity.getLowerConsecutiveOfflineLimit() != null ||
                entity.getUpperConsecutiveOfflineLimit() != null ||
                entity.getCumulativeTotalTransactionAmountLimit() != null) {

            requirementDTO = AuthorizationPolicyRequirementDTO.builder()
                    .cumulativeTotalTransactionAmountUpperLimit(entity.getCumulativeTotalTransactionAmountUpperLimit())
                    .lowerConsecutiveOfflineLimit(entity.getLowerConsecutiveOfflineLimit())
                    .upperConsecutiveOfflineLimit(entity.getUpperConsecutiveOfflineLimit())
                    .cumulativeTotalTransactionAmountLimit(entity.getCumulativeTotalTransactionAmountLimit())
                    .build();
        }
        return requirementDTO;
    }

    @Override
    public AuthorizationPolicyEntity toEntity(AuthorizationPolicyDTO dto) {
        if (dto == null) {
            return null;
        }
        AuthorizationPolicyEntity entity;
        try {
            entity = new AuthorizationPolicyEntity(
                    dto.getAuthorizationType(),
                    dto.getPriorityUseLevel(),
                    dto.getRequirement().getLowerConsecutiveOfflineLimit(),
                    dto.getRequirement().getUpperConsecutiveOfflineLimit(),
                    dto.getRequirement().getCumulativeTotalTransactionAmountLimit(),
                    dto.getRequirement().getCumulativeTotalTransactionAmountUpperLimit(),
                    cardProductMapper.toEntity(dto.getCardProduct())
            );
        } catch (Exception ignored) {
            entity = new AuthorizationPolicyEntity(
                    dto.getAuthorizationType(),
                    dto.getPriorityUseLevel(),
                    null, null, null, null,
                    cardProductMapper.toEntity(dto.getCardProduct())
            );
        }
        entity.setIdAuthorizationPolicy(dto.getIdAuthorizationPolicy());
        return entity;
    }

    public AuthorizationPolicyDTO toDTOFromCardProduct(@NotNull AuthorizationPolicyEntity entity) {
        AuthorizationPolicyRequirementDTO requirementDTO = AuthorizationPolicyRequirementDTO.builder()
                .cumulativeTotalTransactionAmountUpperLimit(entity.getCumulativeTotalTransactionAmountUpperLimit())
                .lowerConsecutiveOfflineLimit(entity.getLowerConsecutiveOfflineLimit())
                .upperConsecutiveOfflineLimit(entity.getUpperConsecutiveOfflineLimit())
                .cumulativeTotalTransactionAmountLimit(entity.getCumulativeTotalTransactionAmountLimit())
                .build();
        return AuthorizationPolicyDTO.builder()
                .idAuthorizationPolicy(entity.getIdAuthorizationPolicy())
                .authorizationType(entity.getAuthorizationType())
                .priorityUseLevel(entity.getPriorityUseLevel())
                .requirement(requirementDTO)
                .build();
    }

    public AuthorizationPolicyDTO toNew(
            @NotNull AuthorizationPolicyDTO authorizationPolicy, @NotNull CardProductDTO cardProduct
    ) {
        return AuthorizationPolicyDTO.builder()
                .authorizationType(authorizationPolicy.getAuthorizationType())
                .priorityUseLevel(authorizationPolicy.getPriorityUseLevel())
                .requirement(authorizationPolicy.getRequirement())
                .cardProduct(cardProduct)
                .build();
    }
}
