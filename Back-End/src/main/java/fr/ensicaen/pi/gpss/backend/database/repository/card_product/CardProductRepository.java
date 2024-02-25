package fr.ensicaen.pi.gpss.backend.database.repository.card_product;

import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardProductEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductRepository extends JpaRepository<CardProductEntity, Integer> {
    CardProductEntity findByNameIsIgnoreCase(@NotBlank @Param(value = "name") String name);

    int countByNameIsIgnoreCase(
        @NotBlank @Param(value = "name") String name
    );
}
