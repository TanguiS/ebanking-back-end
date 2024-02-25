package fr.ensicaen.pi.gpss.backend.database.entity.beneficiary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "Beneficiary")
@Table(name = "`Beneficiary`", schema = "`Account`")
@Getter
@Setter
public class BeneficiaryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idBeneficiary`")
    private Integer idBeneficiary;
    @Basic
    @Column(name = "`FirstName`")
    private String firstName;
    @Basic
    @Column(name = "`LastName`")
    private String lastName;
    @Basic
    @Column(name = "`IBAN`")
    private String iban;
    @Basic
    @Column(name = "`RIB`")
    private String rib;
    @Column(name = "`idBankAccount`")
    private Integer idBankAccount;

    public BeneficiaryEntity(
            String firstName,
            String lastName,
            String iban,
            String rib,
            Integer idBankAccount
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.iban = iban;
        this.rib = rib;
        this.idBankAccount = idBankAccount;
    }

    public BeneficiaryEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficiaryEntity that = (BeneficiaryEntity) o;
        return Objects.equals(idBeneficiary, that.idBeneficiary) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(iban, that.iban) &&
                Objects.equals(rib, that.rib) &&
                Objects.equals(idBankAccount, that.idBankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBeneficiary, firstName, lastName, iban, rib, idBankAccount);
    }
}
