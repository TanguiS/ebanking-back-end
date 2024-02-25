package fr.ensicaen.pi.gpss.backend.database.entity.payment_method;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "ChequeBook")
@Table(name = "`ChequeBook`", schema = "`PaymentMethod`")
@Getter
@Setter
public class ChequeBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "`idCheque`")
    private Integer idChequeBook;
    @Basic
    @Column(name = "`PayerName`")
    private String payerName;
    @Basic
    @Column(name = "`PayeeName`")
    private String payeeName;

    public ChequeBookEntity(String payerName, String payeeName) {
        this.payerName = payerName;
        this.payeeName = payeeName;
    }

    public ChequeBookEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChequeBookEntity that = (ChequeBookEntity) o;
        return Objects.equals(idChequeBook, that.idChequeBook) &&
                Objects.equals(payerName, that.payerName) &&
                Objects.equals(payeeName, that.payeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChequeBook, payerName, payeeName);
    }
}
