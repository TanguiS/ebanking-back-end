package fr.ensicaen.pi.gpss.backend.database.repository.account;

import fr.ensicaen.pi.gpss.backend.database.entity.account.ATMAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ATMAccountRepository extends JpaRepository<ATMAccountEntity, Integer> {
}
