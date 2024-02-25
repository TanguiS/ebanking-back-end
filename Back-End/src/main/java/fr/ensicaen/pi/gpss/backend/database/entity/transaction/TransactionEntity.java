package fr.ensicaen.pi.gpss.backend.database.entity.transaction;

import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Currency;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionStatus;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "Transaction")
@Table(name = "`Transaction`", schema = "`Account`")
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idTransaction`")
    private Integer idTransaction;
    @Column(name = "`Amount`")
    private Integer amount;
    @Column(name = "`Currency`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private Currency currency;
    @Column(name = "`TransactionDate`")
    private Timestamp transactionDate;
    @Column(name = "`TransactionType`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private TransactionType transactionType;
    @Column(name = "`TransactionAccountingDirection`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private AccountingDirection accountingDirection;
    @Column(name = "`ClearedDate`")
    private Timestamp clearedDate;
    @Column(name = "`PAN`")
    private String pan;
    @Column(name = "`IBAN`")
    private String iban;
    @Column(name = "`TransactionStatus`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private TransactionStatus transactionStatus;

    public TransactionEntity(
            Integer amount,
            Currency currency,
            Timestamp transactionDate,
            TransactionType transactionType,
            AccountingDirection accountingDirection,
            Timestamp clearedDate,
            String pan,
            String iban,
            TransactionStatus transactionStatus
    ) {
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.accountingDirection = accountingDirection;
        this.clearedDate = clearedDate;
        this.pan = pan;
        this.iban = iban;
        this.transactionStatus = transactionStatus;
    }

    public TransactionEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(idTransaction, that.idTransaction) &&
                Objects.equals(amount, that.amount) &&
                currency == that.currency &&
                Objects.equals(transactionDate, that.transactionDate) &&
                transactionType == that.transactionType &&
                accountingDirection == that.accountingDirection &&
                Objects.equals(clearedDate, that.clearedDate) &&
                Objects.equals(pan, that.pan) &&
                Objects.equals(iban, that.iban) && transactionStatus == that.transactionStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idTransaction,
                amount,
                currency,
                transactionDate,
                transactionType,
                accountingDirection,
                clearedDate,
                pan,
                iban,
                transactionStatus
        );
    }
}
