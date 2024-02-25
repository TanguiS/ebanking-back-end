package fr.ensicaen.pi.gpss.backend.database.entity.direct_debit;

import fr.ensicaen.pi.gpss.backend.database.enumerate.Schedule;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity(name = "DirectDebit")
@Table(name = "`DirectDebit`", schema = "`Account`")
@Getter
@Setter
public class  DirectDebitEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idDirectDebit`")
    private Integer idDirectDebit;
    @Basic
    @Column(name = "`IBAN`", nullable = false)
    private String iban;
    @Column(name = "`Recurrence`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private Schedule recurrence;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idIndividual`")
    private IndividualEntity individual;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idLegalEntity`")
    private LegalEntityEntity legalEntity;

    public DirectDebitEntity() {
    }

    public DirectDebitEntity(
            String iban, Schedule recurrence, IndividualEntity individual, LegalEntityEntity legalEntity
    ) {
        this.iban = iban;
        this.recurrence = recurrence;
        this.individual = individual;
        this.legalEntity = legalEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectDebitEntity that = (DirectDebitEntity) o;
        return Objects.equals(idDirectDebit, that.idDirectDebit) &&
                Objects.equals(iban, that.iban) &&
                recurrence == that.recurrence &&
                Objects.equals(individual, that.individual) &&
                Objects.equals(legalEntity, that.legalEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDirectDebit, iban, recurrence, individual, legalEntity);
    }
}
