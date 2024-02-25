package fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestChequeBookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestChequeBookRepository extends CrudRepository<RequestChequeBookEntity, Integer> {
}
