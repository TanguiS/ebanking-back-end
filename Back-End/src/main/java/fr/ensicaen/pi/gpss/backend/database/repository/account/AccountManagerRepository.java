package fr.ensicaen.pi.gpss.backend.database.repository.account;

import fr.ensicaen.pi.gpss.backend.database.entity.account.AccountManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountManagerRepository extends JpaRepository <AccountManagerEntity, Integer> {
    @Query(
            "SELECT bankAccount FROM BankAccount bankAccount " +
            "JOIN ClientAccount account ON account.bankAccount = bankAccount " +
            "JOIN AccountManager manager ON manager.clientAccount = account " +
            "WHERE manager.usersInformation = :client"
    )
    List<BankAccountEntity> findAllBankAccountByUserInformation(
            @Valid @NotNull @Param("client") UserInformationEntity client
    );

}
