package fr.ensicaen.pi.gpss.backend.database.entity.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "ATMAccount")
@Table(name = "`ATMAccount`", schema = "`Account`")
@Getter
@Setter

public class ATMAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idATMAccount`", nullable = false)
    private Integer idATMAccount;
    @OneToOne(mappedBy = "atmAccount")
    private AccountManagerEntity accountManager;
    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;

    public ATMAccountEntity(AccountManagerEntity accountManager, BankAccountEntity bankAccount) {
        this.accountManager = accountManager;
        this.bankAccount = bankAccount;
    }

    public ATMAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATMAccountEntity that = (ATMAccountEntity) o;
        return Objects.equals(idATMAccount, that.idATMAccount) &&
                Objects.equals(accountManager, that.accountManager) &&
                Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idATMAccount, accountManager, bankAccount);
    }
}
