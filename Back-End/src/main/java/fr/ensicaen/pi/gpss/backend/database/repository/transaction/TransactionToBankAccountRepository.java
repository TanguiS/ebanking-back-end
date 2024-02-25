package fr.ensicaen.pi.gpss.backend.database.repository.transaction;

import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionToBankAccountEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionToBankAccountRepository extends CrudRepository<TransactionToBankAccountEntity, Integer> {
    @Query(
            "SELECT transactionToBankAccount FROM TransactionToBankAccount transactionToBankAccount " +
            "WHERE transactionToBankAccount.transaction IN :transactions"
    )
    List<TransactionToBankAccountEntity> findByTransactions(
            @Valid @NotEmpty @Param("transactions") List<TransactionEntity> transactions
    );
}
