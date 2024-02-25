package fr.ensicaen.pi.gpss.backend.database.entity.card_product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "CardInformation")
@Table(name = "`CardInformation`", schema = "`Card`")
@Getter
@Setter
public class CardInformationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idCardInformation`")
    private Integer idCardInformation;
    @Basic
    @Column(name = "`PAN`", nullable = false)
    private String pan;
    @Basic
    @Column(name = "`ExpirationCardDate`", nullable = false)
    private Timestamp expirationCardDate;
    @Basic
    @Column(name = "`CVX2`", nullable = false)
    private Integer cvx2;
    @Basic
    @Column(name = "`UpperLimitPerMonth`", nullable = false)
    private Integer upperLimitPerMonth;
    @Basic
    @Column(name = "`UpperLimitPerTransaction`", nullable = false)
    private Integer upperLimitPerTransaction;
    @OneToOne
    @JoinColumn(name = "`idCardProduct`", nullable = false)
    private CardProductEntity cardProduct;

    public CardInformationEntity(
            String pan,
            Timestamp expirationCardDate,
            Integer cvx2,
            Integer upperLimitPerMonth,
            Integer upperLimitPerTransaction,
            CardProductEntity cardProduct
    ) {
        this.pan = pan;
        this.expirationCardDate = expirationCardDate;
        this.cvx2 = cvx2;
        this.upperLimitPerMonth = upperLimitPerMonth;
        this.upperLimitPerTransaction = upperLimitPerTransaction;
        this.cardProduct = cardProduct;
    }

    public CardInformationEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardInformationEntity that = (CardInformationEntity) o;
        return Objects.equals(idCardInformation, that.idCardInformation) &&
                Objects.equals(pan, that.pan) &&
                Objects.equals(expirationCardDate, that.expirationCardDate) &&
                Objects.equals(cvx2, that.cvx2) &&
                Objects.equals(upperLimitPerMonth, that.upperLimitPerMonth) &&
                Objects.equals(upperLimitPerTransaction, that.upperLimitPerTransaction) &&
                Objects.equals(cardProduct, that.cardProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idCardInformation,
                pan,
                expirationCardDate,
                cvx2,
                upperLimitPerMonth,
                upperLimitPerTransaction,
                cardProduct
        );
    }
}
