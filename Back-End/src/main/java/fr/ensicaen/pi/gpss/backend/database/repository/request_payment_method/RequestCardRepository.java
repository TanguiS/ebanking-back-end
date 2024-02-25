package fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestCardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestCardRepository extends CrudRepository<RequestCardEntity, Integer> {
}
