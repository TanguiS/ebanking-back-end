package fr.ensicaen.pi.gpss.backend.database.repository.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.IndividualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends JpaRepository<IndividualEntity, Integer> {
}
