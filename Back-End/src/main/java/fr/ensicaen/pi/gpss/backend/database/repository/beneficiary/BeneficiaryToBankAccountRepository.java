package fr.ensicaen.pi.gpss.backend.database.repository.beneficiary;

import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryToBankAccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryToBankAccountRepository extends CrudRepository<BeneficiaryToBankAccountEntity, Integer> {
}
