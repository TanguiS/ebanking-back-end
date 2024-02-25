package fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPaymentMethodStatusRepository extends JpaRepository<RequestPaymentMethodStatusEntity, Integer> {
}
