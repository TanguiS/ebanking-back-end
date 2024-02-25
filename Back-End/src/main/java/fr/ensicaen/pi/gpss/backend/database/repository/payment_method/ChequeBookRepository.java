package fr.ensicaen.pi.gpss.backend.database.repository.payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.ChequeBookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChequeBookRepository extends CrudRepository<ChequeBookEntity, Integer> {
}
