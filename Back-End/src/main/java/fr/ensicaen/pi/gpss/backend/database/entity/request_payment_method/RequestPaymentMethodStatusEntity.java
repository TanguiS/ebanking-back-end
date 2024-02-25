package fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "RequestPaymentMethodStatus")
@Table(name = "`RequestPaymentMethodStatus`", schema = "`RequestPaymentMethod`")
@Getter
@Setter
public class RequestPaymentMethodStatusEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idRequestPaymentMethodStatus`")
    private Integer idRequestPaymentMethodStatus;
    @Basic
    @Column(name = "`UserRequestPaymentMethodDate`", nullable = false)
    private Timestamp userRequestPaymentMethodDate;
    @Basic
    @Column(name = "`BankRequestPaymentMethodDate`")
    private Timestamp bankRequestPaymentMethodDate;
    @Basic
    @Column(name = "`BankReceivedPaymentMethod`")
    private Timestamp bankReceivedPaymentMethodDate;
    @Basic
    @Column(name = "`UserReceivedPaymentMethod`")
    private Timestamp userReceivedPaymentMethodDate;
    @Basic
    @Column(name = "`OrderStatus`", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private OrderStatus orderStatus;

    public RequestPaymentMethodStatusEntity(
            Timestamp userRequestPaymentMethodDate,
            Timestamp bankRequestPaymentMethodDate,
            Timestamp bankReceivedPaymentMethodDate,
            Timestamp userReceivedPaymentMethodDate,
            OrderStatus orderStatus
    ) {
        this.userRequestPaymentMethodDate = userRequestPaymentMethodDate;
        this.bankRequestPaymentMethodDate = bankRequestPaymentMethodDate;
        this.bankReceivedPaymentMethodDate = bankReceivedPaymentMethodDate;
        this.userReceivedPaymentMethodDate = userReceivedPaymentMethodDate;
        this.orderStatus = orderStatus;
    }

    public RequestPaymentMethodStatusEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestPaymentMethodStatusEntity that = (RequestPaymentMethodStatusEntity) o;
        return Objects.equals(idRequestPaymentMethodStatus, that.idRequestPaymentMethodStatus) &&
                Objects.equals(userRequestPaymentMethodDate, that.userRequestPaymentMethodDate) &&
                Objects.equals(bankRequestPaymentMethodDate, that.bankRequestPaymentMethodDate) &&
                Objects.equals(bankReceivedPaymentMethodDate, that.bankReceivedPaymentMethodDate) &&
                Objects.equals(userReceivedPaymentMethodDate, that.userReceivedPaymentMethodDate) &&
                orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idRequestPaymentMethodStatus,
                userRequestPaymentMethodDate,
                bankRequestPaymentMethodDate,
                bankReceivedPaymentMethodDate,
                userReceivedPaymentMethodDate,
                orderStatus
        );
    }
}
