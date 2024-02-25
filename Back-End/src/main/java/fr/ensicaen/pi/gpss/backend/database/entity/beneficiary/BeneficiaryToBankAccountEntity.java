package fr.ensicaen.pi.gpss.backend.database.entity.beneficiary;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "BeneficiaryToBankAccount")
@Table(name = "`BeneficiaryToBankAccount`", schema = "`Account`")
@Getter
@Setter
public class BeneficiaryToBankAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idBeneficiaryToBankAccount`")
    private Integer idBeneficiaryToBankAccount;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idBeneficiary`")
    private BeneficiaryEntity beneficiary;
    @ManyToOne
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;

    public BeneficiaryToBankAccountEntity(BeneficiaryEntity beneficiary, BankAccountEntity bankAccount) {
        this.beneficiary = beneficiary;
        this.bankAccount = bankAccount;
    }

    public BeneficiaryToBankAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficiaryToBankAccountEntity that = (BeneficiaryToBankAccountEntity) o;
        return Objects.equals(idBeneficiaryToBankAccount, that.idBeneficiaryToBankAccount) &&
                Objects.equals(beneficiary, that.beneficiary) &&
                Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBeneficiaryToBankAccount, beneficiary, bankAccount);
    }
}
