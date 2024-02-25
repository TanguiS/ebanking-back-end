package fr.ensicaen.pi.gpss.backend.database.entity.card_product;

import fr.ensicaen.pi.gpss.backend.database.enumerate.AuthorizationType;
import fr.ensicaen.pi.gpss.backend.database.enumerate.PriorityUseLevel;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity(name = "AuthorizationPolicy")
@Table(name = "`AuthorizationPolicy`", schema = "`Card`")
@Getter
@Setter
public class AuthorizationPolicyEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idAuthorizationPolicy`", nullable = false)
    private Integer idAuthorizationPolicy;
    @Basic
    @Column(name = "`AuthorizationType`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private AuthorizationType authorizationType;
    @Basic
    @Column(name = "`PriorityUseLevel`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private PriorityUseLevel priorityUseLevel;
    @Basic
    @Column(name = "`LowerConsecutiveOfflineLimit`")
    private Integer lowerConsecutiveOfflineLimit;
    @Basic
    @Column(name = "`UpperConsecutiveOfflineLimit`")
    private Integer upperConsecutiveOfflineLimit;
    @Basic
    @Column(name = "`CumulativeTotalTransactionAmountLimit`")
    private Integer cumulativeTotalTransactionAmountLimit;
    @Basic
    @Column(name = "`CumulativeTotalTransactionAmountUpperLimit`")
    private Integer cumulativeTotalTransactionAmountUpperLimit;
    @ManyToOne
    @JoinColumn(name = "`idCardProduct`", nullable = false)
    private CardProductEntity cardProduct;

    public AuthorizationPolicyEntity(
            AuthorizationType authorizationType,
            PriorityUseLevel priorityUseLevel,
            Integer lowerConsecutiveOfflineLimit,
            Integer upperConsecutiveOfflineLimit,
            Integer cumulativeTotalTransactionAmountLimit,
            Integer cumulativeTotalTransactionAmountUpperLimit,
            CardProductEntity cardProduct
    ) {
        this.authorizationType = authorizationType;
        this.priorityUseLevel = priorityUseLevel;
        this.lowerConsecutiveOfflineLimit = lowerConsecutiveOfflineLimit;
        this.upperConsecutiveOfflineLimit = upperConsecutiveOfflineLimit;
        this.cumulativeTotalTransactionAmountLimit = cumulativeTotalTransactionAmountLimit;
        this.cumulativeTotalTransactionAmountUpperLimit = cumulativeTotalTransactionAmountUpperLimit;
        this.cardProduct = cardProduct;
    }

    public AuthorizationPolicyEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationPolicyEntity that = (AuthorizationPolicyEntity) o;
        return Objects.equals(idAuthorizationPolicy, that.idAuthorizationPolicy) &&
                authorizationType == that.authorizationType &&
                priorityUseLevel == that.priorityUseLevel &&
                Objects.equals(lowerConsecutiveOfflineLimit, that.lowerConsecutiveOfflineLimit) &&
                Objects.equals(upperConsecutiveOfflineLimit, that.upperConsecutiveOfflineLimit) &&
                Objects.equals(cumulativeTotalTransactionAmountLimit, that.cumulativeTotalTransactionAmountLimit) &&
                Objects.equals(
                        cumulativeTotalTransactionAmountUpperLimit, that.cumulativeTotalTransactionAmountUpperLimit
                ) &&
                Objects.equals(cardProduct, that.cardProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idAuthorizationPolicy,
                authorizationType,
                priorityUseLevel,
                lowerConsecutiveOfflineLimit,
                upperConsecutiveOfflineLimit,
                cumulativeTotalTransactionAmountLimit,
                cumulativeTotalTransactionAmountUpperLimit,
                cardProduct
        );
    }
}
