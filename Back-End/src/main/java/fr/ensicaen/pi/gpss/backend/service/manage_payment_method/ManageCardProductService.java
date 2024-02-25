package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardProductEntity;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardProductMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.CardProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class ManageCardProductService {
    private final CardProductMapper cardProductMapper;
    private final CardProductRepository cardProductRepository;

    public ManageCardProductService(
            CardProductMapper cardProductMapper,
            CardProductRepository cardProductRepository
    ) {
        this.cardProductMapper = cardProductMapper;
        this.cardProductRepository = cardProductRepository;
    }

    public boolean doesIdProductCardDoNotExist(@Valid @NotNull @PositiveOrZero Integer idProductCard) {
        return !cardProductRepository.existsById(idProductCard);
    }

    public CardProductDTO resolveById(
            @Valid @NotNull @PositiveOrZero Integer idProductCard
    ) throws IllegalArgumentException {
        Optional<CardProductEntity> byId = cardProductRepository.findById(idProductCard);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("No product card associated with the following id : " + idProductCard);
        }
        return cardProductMapper.toDTO(byId.get());
    }
}
