package fr.ensicaen.pi.gpss.backend.database.repository.card_product;

import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInformationRepository extends JpaRepository<CardInformationEntity, Integer> {
}
