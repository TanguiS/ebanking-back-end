package fr.ensicaen.pi.gpss.backend.database.repository.beneficiary;

import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends CrudRepository<BeneficiaryEntity, Integer> {
}
