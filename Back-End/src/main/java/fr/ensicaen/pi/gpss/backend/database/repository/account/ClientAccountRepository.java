package fr.ensicaen.pi.gpss.backend.database.repository.account;

import fr.ensicaen.pi.gpss.backend.database.entity.account.ClientAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccountEntity, Integer> {
}
