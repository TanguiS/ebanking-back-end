package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.AuthorizationPolicyEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.SoftwareCardEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.AuthorizationPolicyMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardProductMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.SoftwareCardMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.AuthorizationPolicyRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.CardProductRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.SoftwareCardRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class AddCardProductService {
    private final CardProductRepository cardProductRepository;
    private final CardProductMapper cardProductMapper;
    private final AuthorizationPolicyRepository authorizationPolicyRepository;
    private final AuthorizationPolicyMapper authorizationPolicyMapper;
    private final SoftwareCardRepository softwareCardRepository;
    private final SoftwareCardMapper softwareCardMapper;

    public AddCardProductService(
            CardProductRepository cardProductRepository,
            CardProductMapper cardProductMapper,
            AuthorizationPolicyRepository authorizationPolicyRepository,
            AuthorizationPolicyMapper authorizationPolicyMapper,
            SoftwareCardRepository softwareCardRepository,
            SoftwareCardMapper softwareCardMapper
    ) {
        this.cardProductRepository = cardProductRepository;
        this.cardProductMapper = cardProductMapper;
        this.authorizationPolicyRepository = authorizationPolicyRepository;
        this.authorizationPolicyMapper = authorizationPolicyMapper;
        this.softwareCardRepository = softwareCardRepository;
        this.softwareCardMapper = softwareCardMapper;
    }

    private void validateCardProduct(@Valid @NotNull CardProductDTO cardProduct) throws IllegalArgumentException {
        if (cardProductRepository.countByNameIsIgnoreCase(cardProduct.getName()) != 0) {
            throw new IllegalArgumentException("Card Product already exists");
        }
    }

    private void addSoftwareCards(CardProductDTO cardProduct, CardProductDTO cardProductDTO) {
        List<SoftwareCardEntity> softwareCards = cardProduct.getSoftwareCards()
                .stream()
                .map(dto -> softwareCardMapper.toNew(dto, cardProductDTO))
                .map(softwareCardMapper::toEntity)
                .toList();
        softwareCardRepository.saveAll(softwareCards);
    }

    private void addAuthorizationPolicies(CardProductDTO cardProduct, CardProductDTO cardProductDTO) {
        List<AuthorizationPolicyEntity> authorizationPolicies = cardProduct.getAuthorizationPolicies()
                .stream()
                .map(dto -> authorizationPolicyMapper.toNew(dto, cardProductDTO))
                .map(authorizationPolicyMapper::toEntity)
                .toList();
        authorizationPolicyRepository.saveAll(authorizationPolicies);
    }

    public CardProductDTO addNew(@Valid @NotNull CardProductDTO cardProduct) {
        validateCardProduct(cardProduct);
        CardProductDTO cardProductDTO = cardProductMapper.toDTO(
                cardProductRepository.save(cardProductMapper.toEntity(cardProduct))
        );

        addAuthorizationPolicies(cardProduct, cardProductDTO);
        addSoftwareCards(cardProduct, cardProductDTO);
        return cardProductMapper.toDTO(cardProductRepository.findByNameIsIgnoreCase(cardProductDTO.getName()));
    }


}
