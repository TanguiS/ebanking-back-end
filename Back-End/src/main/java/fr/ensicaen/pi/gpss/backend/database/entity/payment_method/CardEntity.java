package fr.ensicaen.pi.gpss.backend.database.entity.payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity(name = "Card")
@Table(name = "`Card`", schema = "`PaymentMethod`")
@Getter
@Setter
public class CardEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idCard`")
    private Integer idCard;
    @Basic
    @Column(name = "`CardStatus`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private CardStatus cardStatus;
    @OneToOne
    @JoinColumn(name = "`idCardInformation`", nullable = false)
    private CardInformationEntity cardInformation;

    public CardEntity(CardStatus cardStatus, CardInformationEntity cardInformation) {
        this.cardStatus = cardStatus;
        this.cardInformation = cardInformation;
    }

    public CardEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardEntity that = (CardEntity) o;
        return Objects.equals(idCard, that.idCard) &&
                cardStatus == that.cardStatus &&
                Objects.equals(cardInformation, that.cardInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCard, cardStatus, cardInformation);
    }
}
