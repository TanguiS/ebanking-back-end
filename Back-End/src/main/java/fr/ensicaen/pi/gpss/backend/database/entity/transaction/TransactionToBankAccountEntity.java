package fr.ensicaen.pi.gpss.backend.database.entity.transaction;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "TransactionToBankAccount")
@Table(name = "`TransactionToBankAccount`", schema = "`Account`")
@Getter
@Setter
public class TransactionToBankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idTransactionToBankAccount`")
    private Integer idTransactionToBankAccount;
    @ManyToOne
    @JoinColumn(name = "`idBankAccount`")
    private BankAccountEntity bankAccount;
    @OneToOne
    @JoinColumn(name = "`idTransaction`")
    private TransactionEntity transaction;

    public TransactionToBankAccountEntity(BankAccountEntity bankAccount, TransactionEntity transaction) {
        this.bankAccount = bankAccount;
        this.transaction = transaction;
    }

    public TransactionToBankAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionToBankAccountEntity that = (TransactionToBankAccountEntity) o;
        return Objects.equals(idTransactionToBankAccount, that.idTransactionToBankAccount) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransactionToBankAccount, bankAccount, transaction);
    }
}
