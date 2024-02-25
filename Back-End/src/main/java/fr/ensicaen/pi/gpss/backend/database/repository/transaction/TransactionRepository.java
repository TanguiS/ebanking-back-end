package fr.ensicaen.pi.gpss.backend.database.repository.transaction;

import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Currency;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query(value =
            "SELECT transaction FROM Transaction transaction " +
            "WHERE transaction.transactionDate = :transactionDate " +
            "AND transaction.amount = :amount " +
            "AND transaction.transactionType = :transactionType " +
            "AND transaction.accountingDirection = :accountingDirection " +
            "AND transaction.currency = :currency"
    )
    Optional<TransactionEntity> findByCollectedTransaction(
            @Param(value = "transactionDate") @NonNull Timestamp transactionDate,
            @Param(value = "amount") @NonNull Integer amount,
            @Param(value = "transactionType") @NonNull TransactionType transactionType,
            @Param(value = "accountingDirection") @NonNull AccountingDirection accountingDirection,
            @Param(value = "currency") @NonNull Currency currency
    );

    @Query(
            "SELECT t1, t2 FROM Transaction t1, Transaction t2 " +
            "WHERE t1.amount = t2.amount " +
            "AND t1.currency = t2.currency " +
            "AND t1.transactionDate = t2.transactionDate " +
            "AND t1.transactionType = t2.transactionType " +
            "AND t1.transactionStatus = t2.transactionStatus " +
            "AND (t1.accountingDirection = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection.DEBIT " +
                "AND t2.accountingDirection = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection.CREDIT) "
    )
    List<TransactionEntity[]> findAllTransactionDebtorWithTheirCreditor(Pageable pageable);
}
