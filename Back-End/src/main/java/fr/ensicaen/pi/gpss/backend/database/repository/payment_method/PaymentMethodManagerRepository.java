package fr.ensicaen.pi.gpss.backend.database.repository.payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.PaymentMethodManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodManagerRepository extends JpaRepository<PaymentMethodManagerEntity, Integer> {
}
