package fr.ensicaen.pi.gpss.backend.database.entity.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "DirectDebitToBankAccount")
@Table(name = "`DirectDebitToBankAccount`", schema = "`Account`")
@Getter
@Setter
public class DirectDebitToBankAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idDirectDebitToBankAccount`")
    private Integer idDirectDebitToBankAccount;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idDirectDebit`", nullable = false)
    private DirectDebitEntity directDebit;
    @ManyToOne
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;

    public DirectDebitToBankAccountEntity() {
    }

    public DirectDebitToBankAccountEntity(DirectDebitEntity directDebit, BankAccountEntity bankAccount) {
        this.directDebit = directDebit;
        this.bankAccount = bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectDebitToBankAccountEntity that = (DirectDebitToBankAccountEntity) o;
        return Objects.equals(idDirectDebitToBankAccount, that.idDirectDebitToBankAccount) &&
                Objects.equals(directDebit, that.directDebit) &&
                Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDirectDebitToBankAccount, directDebit, bankAccount);
    }
}
