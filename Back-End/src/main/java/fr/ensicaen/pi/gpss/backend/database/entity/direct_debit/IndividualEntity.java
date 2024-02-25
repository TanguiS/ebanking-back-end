package fr.ensicaen.pi.gpss.backend.database.entity.direct_debit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "Individual")
@Table(name = "`Individual`", schema = "`Account`")
@Getter
@Setter
public class IndividualEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idIndividual`")
    private Integer idIndividual;
    @Basic
    @Column(name = "`FirstName`", nullable = false)
    private String firstName;
    @Basic
    @Column(name = "`LastName`", nullable = false)
    private String lastName;

    public IndividualEntity() {
    }

    public IndividualEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualEntity that = (IndividualEntity) o;
        return Objects.equals(idIndividual, that.idIndividual) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIndividual, firstName, lastName);
    }
}
