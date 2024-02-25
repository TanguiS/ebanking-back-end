package fr.ensicaen.pi.gpss.backend.database.repository.card_product;

import fr.ensicaen.pi.gpss.backend.database.entity.card_product.AuthorizationPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationPolicyRepository extends JpaRepository<AuthorizationPolicyEntity, Integer> {
}
