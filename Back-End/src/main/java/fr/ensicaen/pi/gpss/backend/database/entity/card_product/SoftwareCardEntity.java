package fr.ensicaen.pi.gpss.backend.database.entity.card_product;

import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.database.enumerate.PriorityUseLevel;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity(name = "SoftwareCard")
@Table(name = "`SoftwareCard`", schema = "`Card`")
@Getter
@Setter
public class SoftwareCardEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idSoftwareList`", nullable = false)
    private Integer idSoftwareCard;
    @Basic
    @Column(name = "`CardScheme`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private CardScheme cardScheme;
    @Basic
    @Column(name = "`PriorityUseLevel`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private PriorityUseLevel priorityUseLevel;
    @ManyToOne
    @JoinColumn(name = "`idCardProduct`", nullable = false)
    private CardProductEntity cardProduct;

    public SoftwareCardEntity(CardScheme cardScheme, PriorityUseLevel priorityUseLevel, CardProductEntity cardProduct) {
        this.cardScheme = cardScheme;
        this.priorityUseLevel = priorityUseLevel;
        this.cardProduct = cardProduct;
    }

    public SoftwareCardEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareCardEntity that = (SoftwareCardEntity) o;
        return Objects.equals(idSoftwareCard, that.idSoftwareCard) &&
                cardScheme == that.cardScheme &&
                priorityUseLevel == that.priorityUseLevel &&
                Objects.equals(cardProduct, that.cardProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSoftwareCard, cardScheme, priorityUseLevel, cardProduct);
    }
}
