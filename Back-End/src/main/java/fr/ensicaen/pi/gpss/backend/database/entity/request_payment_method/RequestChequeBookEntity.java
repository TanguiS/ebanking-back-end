package fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "RequestChequeBook")
@Table(name = "`RequestChequeBook`", schema = "`RequestPaymentMethod`")
@Getter
@Setter
public class RequestChequeBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idRequestChequeBook`")
    private Integer idRequestChequeBook;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idRequestPaymentMethodStatus`", nullable = false)
    private RequestPaymentMethodStatusEntity requestPaymentMethodStatusEntity;

    public RequestChequeBookEntity(RequestPaymentMethodStatusEntity requestPaymentMethodStatusEntity) {
        this.requestPaymentMethodStatusEntity = requestPaymentMethodStatusEntity;
    }

    public RequestChequeBookEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestChequeBookEntity that = (RequestChequeBookEntity) o;
        return Objects.equals(idRequestChequeBook, that.idRequestChequeBook) &&
                Objects.equals(requestPaymentMethodStatusEntity, that.requestPaymentMethodStatusEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRequestChequeBook, requestPaymentMethodStatusEntity);
    }
}
