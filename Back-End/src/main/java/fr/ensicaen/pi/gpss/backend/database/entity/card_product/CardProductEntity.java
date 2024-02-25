package fr.ensicaen.pi.gpss.backend.database.entity.card_product;

import fr.ensicaen.pi.gpss.backend.database.enumerate.CardType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "CardProduct")
@Table(name = "`CardProduct`", schema = "`Card`")
@Getter
@Setter
public class CardProductEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idCardProduct`")
    private Integer idCardProduct;
    @Basic
    @Column(name = "`Name`", nullable = false)
    private String name;
    @Basic
    @Column(name = "`CardType`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private CardType cardType;
    @Basic
    @Column(name = "`ContactlessUpperLimitPerTransaction`", nullable = false)
    private Integer contactlessUpperLimitPerTransaction;
    @Basic
    @Column(name = "`NumberOfContactlessTransactionBeforeAskingPIN`", nullable = false)
    private Integer numberOfContactlessTransactionBeforeAskingPin;
    @OneToMany(
            targetEntity = SoftwareCardEntity.class,
            mappedBy = "cardProduct",
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    private List<SoftwareCardEntity> softwareCards = new ArrayList<>();
    @OneToMany(
            targetEntity = AuthorizationPolicyEntity.class,
            mappedBy = "cardProduct",
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    private List<AuthorizationPolicyEntity> authorizationPolicies = new ArrayList<>();

    public CardProductEntity(
            String name,
            CardType cardType,
            Integer contactlessUpperLimitPerTransaction,
            Integer numberOfContactlessTransactionBeforeAskingPin,
            List<SoftwareCardEntity> softwareCards,
            List<AuthorizationPolicyEntity> authorizationPolicies
    ) {
        this.name = name;
        this.cardType = cardType;
        this.contactlessUpperLimitPerTransaction = contactlessUpperLimitPerTransaction;
        this.numberOfContactlessTransactionBeforeAskingPin = numberOfContactlessTransactionBeforeAskingPin;
        this.softwareCards = softwareCards;
        this.authorizationPolicies = authorizationPolicies;
    }

    public CardProductEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardProductEntity that = (CardProductEntity) o;
        return Objects.equals(idCardProduct, that.idCardProduct) &&
                Objects.equals(name, that.name) &&
                cardType == that.cardType &&
                Objects.equals(contactlessUpperLimitPerTransaction, that.contactlessUpperLimitPerTransaction) &&
                Objects.equals(
                        numberOfContactlessTransactionBeforeAskingPin,
                        that.numberOfContactlessTransactionBeforeAskingPin
                ) &&
                Objects.equals(softwareCards, that.softwareCards) &&
                Objects.equals(authorizationPolicies, that.authorizationPolicies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idCardProduct,
                name,
                cardType,
                contactlessUpperLimitPerTransaction,
                numberOfContactlessTransactionBeforeAskingPin,
                softwareCards,
                authorizationPolicies
        );
    }
}
