package fr.ensicaen.pi.gpss.backend.database.entity.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "ClientAccount")
@Table(name = "`ClientAccount`", schema = "`Account`")
@Getter
@Setter
public class ClientAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idClientAccount`")
    private Integer idClientAccount;
    @OneToOne(mappedBy = "clientAccount")
    private AccountManagerEntity accountManager;
    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;

    public ClientAccountEntity(AccountManagerEntity accountManager, BankAccountEntity bankAccount) {
        this.accountManager = accountManager;
        this.bankAccount = bankAccount;
    }

    public ClientAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAccountEntity that = (ClientAccountEntity) o;
        return Objects.equals(idClientAccount, that.idClientAccount) &&
                Objects.equals(accountManager, that.accountManager) &&
                Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClientAccount, accountManager, bankAccount);
    }
}
