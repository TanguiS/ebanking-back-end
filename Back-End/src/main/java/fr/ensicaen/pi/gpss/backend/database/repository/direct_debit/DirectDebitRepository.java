package fr.ensicaen.pi.gpss.backend.database.repository.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.DirectDebitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectDebitRepository extends JpaRepository<DirectDebitEntity, Integer> {
}
