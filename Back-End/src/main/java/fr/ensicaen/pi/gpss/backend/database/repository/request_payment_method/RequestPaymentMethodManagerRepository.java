package fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPaymentMethodManagerRepository
        extends JpaRepository<RequestPaymentMethodManagerEntity, Integer> {
}
