package fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardInformationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "RequestCard")
@Table(name = "`RequestCard`", schema = "`RequestPaymentMethod`")
@Getter
@Setter
public class RequestCardEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idRequestCard`")
    private Integer idRequestCard;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idRequestPaymentMethodStatus`", nullable = false)
    private RequestPaymentMethodStatusEntity requestPaymentMethodStatusEntity;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idCardInformation`", nullable = false)
    private CardInformationEntity cardInformation;

    public RequestCardEntity(
            CardInformationEntity cardInformation,
            RequestPaymentMethodStatusEntity requestPaymentMethodStatusEntity
    ) {
        this.cardInformation = cardInformation;
        this.requestPaymentMethodStatusEntity = requestPaymentMethodStatusEntity;
    }

    public RequestCardEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestCardEntity that = (RequestCardEntity) o;
        return Objects.equals(idRequestCard, that.idRequestCard) &&
                Objects.equals(requestPaymentMethodStatusEntity, that.requestPaymentMethodStatusEntity) &&
                Objects.equals(cardInformation, that.cardInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRequestCard, requestPaymentMethodStatusEntity, cardInformation);
    }
}
