package fr.ensicaen.pi.gpss.backend.database.entity.direct_debit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "LegalEntity")
@Table(name = "`LegalEntity`", schema = "`Account`")
@Getter
@Setter
public class LegalEntityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idLegalEntity`")
    private Integer idLegalEntity;
    @Basic
    @Column(name = "`Name`", nullable = false)
    private String name;
    @Basic
    @Column(name = "`NumberSiret`", nullable = false)
    private String numberSiret;

    public LegalEntityEntity() {
    }

    public LegalEntityEntity(String name, String numberSiret) {
        this.name = name;
        this.numberSiret = numberSiret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegalEntityEntity that = (LegalEntityEntity) o;
        return Objects.equals(idLegalEntity, that.idLegalEntity) &&
                Objects.equals(name, that.name) &&
                Objects.equals(numberSiret, that.numberSiret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLegalEntity, name, numberSiret);
    }
}
